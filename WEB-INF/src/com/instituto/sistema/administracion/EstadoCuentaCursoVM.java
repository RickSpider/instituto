package com.instituto.sistema.administracion;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import com.doxacore.components.finder.FinderInterface;
import com.doxacore.components.finder.FinderModel;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class EstadoCuentaCursoVM extends TemplateViewModelLocal implements FinderInterface {

	private CursoVigente cursoVigenteSelected;
	private List<Object[]> lEstadosCuentasSumaGeneral;
	private List<Object[]> lEstadosCuentas;

	private double saldoTotal = 0;
	private double saldoVencido = 0;

	@Init(superclass = true)
	public void initEstadoCuentaCursoVM() {

		inicializarFinders();

	}

	@AfterCompose(superclass = true)
	public void afterComposeEstadEstadoCuentaCursoVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub

	}

	private FinderModel cursoVigenteFinder;

	@NotifyChange("*")
	@Override
	public void inicializarFinders() {

		String sqlCursoVigente = this.um.getSql("buscarCursoVigente.sql").replace("?1",
				this.getCurrentSede().getSedeid() + "");
		cursoVigenteFinder = new FinderModel("cursoVigente", sqlCursoVigente);

	}

	@Override
	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.cursoVigenteFinder.getNameFinder()) == 0) {

			this.cursoVigenteFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cursoVigenteFinder, "listFinder");

		}

	}

	@Command
	@Override
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cursoVigenteFinder.getNameFinder()) == 0) {

			this.cursoVigenteFinder
					.setListFinder(this.filtrarListaObject(filter, this.cursoVigenteFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.cursoVigenteFinder, "listFinder");

		}

	}

	@Command
	@NotifyChange("*")
	@Override
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cursoVigenteFinder.getNameFinder()) == 0) {

			this.cursoVigenteSelected = this.reg.getObjectById(CursoVigente.class.getName(), id);

		}

		calcularSaldos();
		refrescarEstadosCuenta();

	}

	private void calcularSaldos() {

		if (this.cursoVigenteSelected == null) {

			this.saldoTotal = 0;
			this.saldoVencido = 0;

			return;

		}

		String sqlSaldoTotal = this.um.getSql("estadoCuentaCurso/saldoTotalCursoVigente.sql").replace("?1",
				this.cursoVigenteSelected.getCursovigenteid() + "");

		List<Object[]> result = this.reg.sqlNativo(sqlSaldoTotal);
		this.saldoTotal = (double) result.get(0)[1];

		String sqlSaldoVencido = this.um.getSql("estadoCuentaCurso/saldoVencidoCursoVigente.sql").replace("?1",
				this.cursoVigenteSelected.getCursovigenteid() + "");

		List<Object[]> result2 = this.reg.sqlNativo(sqlSaldoVencido);
		this.saldoVencido = (double) result2.get(0)[1];

	}

	private void refrescarEstadosCuenta() {

		String sqlEstadoCuentaGeneral = this.um.getSql("estadoCuentaCurso/estadoCuentaSumaGeneral.sql").replace("?1",
				this.cursoVigenteSelected.getCursovigenteid() + "");

		this.lEstadosCuentasSumaGeneral = this.reg.sqlNativo(sqlEstadoCuentaGeneral);

	}

	@Command
	@NotifyChange("lEstadosCuentas")
	public void refrescarCobranzaDetallada(@BindingParam("conceptoid") Long conceptoid, @BindingParam("periodo") long periodo) {

		String sqlEstadoCuenta = this.um.getSql("estadoCuentaCurso/estadoCuentaGeneral.sql").replace("?1",
				this.cursoVigenteSelected.getCursovigenteid() + "").replace("?2", String.valueOf(conceptoid)).replace("?3", periodo+"");

		this.lEstadosCuentas = this.reg.sqlNativo(sqlEstadoCuenta);
		
	}
	
	private Window modal;
	
	private List<CobranzaDetalle> lCobranzasDetalles;
	
	@Command
	public void modalEstadoCuentaCurso(@BindingParam("estadocuentaid") long estadocuentaid) {

		this.lCobranzasDetalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(),
				"estadocuentaid = " + estadocuentaid, null);


		modal = (Window) Executions.createComponents("/instituto/zul/administracion/estadoCuentaCursoModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	public void verComprobante(@BindingParam("cobranza") Cobranza cobranza) {
		
		if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) == 0) {
			
			Executions.getCurrent().sendRedirect("/instituto/zul/administracion/reciboReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
			
		}
		
		if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
			
			Executions.getCurrent().sendRedirect("/instituto/zul/administracion/facturaReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
			
		}

	}


	public CursoVigente getCursoVigenteSelected() {
		return cursoVigenteSelected;
	}

	public void setCursoVigenteSelected(CursoVigente cursoVigenteSelected) {
		this.cursoVigenteSelected = cursoVigenteSelected;
	}

	public double getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(double saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public double getSaldoVencido() {
		return saldoVencido;
	}

	public void setSaldoVencido(double saldoVencido) {
		this.saldoVencido = saldoVencido;
	}

	public FinderModel getCursoVigenteFinder() {
		return cursoVigenteFinder;
	}

	public void setCursoVigenteFinder(FinderModel cursoVigenteFinder) {
		this.cursoVigenteFinder = cursoVigenteFinder;
	}

	public List<Object[]> getlEstadosCuentas() {
		return lEstadosCuentas;
	}

	public void setlEstadosCuentas(List<Object[]> lEstadosCuentas) {
		this.lEstadosCuentas = lEstadosCuentas;
	}

	public List<Object[]> getlEstadosCuentasSumaGeneral() {
		return lEstadosCuentasSumaGeneral;
	}

	public void setlEstadosCuentasSumaGeneral(List<Object[]> lEstadosCuentasSumaGeneral) {
		this.lEstadosCuentasSumaGeneral = lEstadosCuentasSumaGeneral;
	}

	public List<CobranzaDetalle> getlCobranzasDetalles() {
		return lCobranzasDetalles;
	}

	public void setlCobranzasDetalles(List<CobranzaDetalle> lCobranzasDetalles) {
		this.lCobranzasDetalles = lCobranzasDetalles;
	}

}
