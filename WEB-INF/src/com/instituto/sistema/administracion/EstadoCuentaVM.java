package com.instituto.sistema.administracion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

import com.instituto.modelo.Alumno;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.ConvenioAlumno;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class EstadoCuentaVM extends TemplateViewModelLocal {

	private Alumno alumnoSelected;
	private CursoVigente cursoVigenteSelected;
	private List<EstadoCuenta> lEstadosCuentas;
	private List<CobranzaDetalle> lCobranzasDetalles;

	@Init(superclass = true)
	public void initEstadoCuentaVM() {

	}

	@AfterCompose(superclass = true)
	public void afterComposeEstadoCuentaVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub

	}

	// Seccion Buscar alumno

	// seccion buscador

	private List<Object[]> lAlumnosbuscarOri;
	private List<Object[]> lAlumnosBuscar;
	private String buscarAlumno = "";

	@Command
	@NotifyChange("lAlumnosBuscar")
	public void filtrarAlumnoBuscar() {

		this.lAlumnosBuscar = this.filtrarListaObject(buscarAlumno, this.lAlumnosbuscarOri);

	}

	@Command
	@NotifyChange("*")
	public void generarListaBuscarAlumno() {
		
		this.buscarAlumno="";
		this.buscarCursoVigente="";

		String sqlBuscarAlumno = this.um.getSql("buscarAlumno.sql").replace("?1",
				this.getCurrentSede().getSedeid() + "");

		this.lAlumnosBuscar = this.reg.sqlNativo(sqlBuscarAlumno);
		this.lAlumnosbuscarOri = this.lAlumnosBuscar;
	}

	@Command
	@NotifyChange("*")
	public void onSelectAlumno(@BindingParam("id") long id) {

		this.alumnoSelected = this.reg.getObjectById(Alumno.class.getName(), id);
		this.buscarAlumno = alumnoSelected.getFullNombre();
		this.cursoVigenteSelected = null;
		this.buscarCursoVigente = "";
		this.lEstadosCuentas = null;

	}

	// fin alumno buscador

	// buscar curso vigente

	private List<Object[]> lCursosVigentesbuscarOri;
	private List<Object[]> lCursosVigentesBuscar;
	private String buscarCursoVigente = "";

	@Command
	@NotifyChange("lCursosVigentesBuscar")
	public void filtrarCursoVigenteBuscar() {

		this.lCursosVigentesBuscar = this.filtrarListaObject(buscarCursoVigente, this.lCursosVigentesbuscarOri);

	}

	@Command
	@NotifyChange({"lCursosVigentesBuscar","buscarCursoVigente"})
	public void generarListaBuscarCursoVigente() {

		if (this.alumnoSelected == null) {

			return;

		}
		
		this.buscarCursoVigente="";

		String sqlBuscarCursoVigente = this.um.getSql("buscarCursoVigentePorAlumno.sql").replace("?1",
				this.alumnoSelected.getAlumnoid() + "");

		this.lCursosVigentesBuscar = this.reg.sqlNativo(sqlBuscarCursoVigente);
		this.lCursosVigentesbuscarOri = this.lCursosVigentesBuscar;
	}

	@Command
	@NotifyChange({ "buscarCursoVigente", "lEstadosCuentas", "cursoVigenteSelected" })
	public void onSelectCursoVigente(@BindingParam("id") long id) {

		this.cursoVigenteSelected = this.reg.getObjectById(CursoVigente.class.getName(), id);
		this.buscarCursoVigente = cursoVigenteSelected.getCurso().getCurso();

		this.lEstadosCuentas = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(),
				"cursoVigenteid = " + this.cursoVigenteSelected.getCursovigenteid() + " AND alumnoid = "
						+ this.alumnoSelected.getAlumnoid(),
				"vencimiento asc");

	}
	
	@Command
	@NotifyChange({ "lCobranzasDetalles" })
	public void refrescarCobranzaDetalle(@BindingParam("estadoCuenta") EstadoCuenta estadoCuenta) {
		
		this.lCobranzasDetalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(),
				"estadocuentaid = " + estadoCuenta.getEstadocuentaid(), null);
		
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

	public Alumno getAlumnoSelected() {
		return alumnoSelected;
	}

	public void setAlumnoSelected(Alumno alumnoSelected) {
		this.alumnoSelected = alumnoSelected;
	}

	public CursoVigente getCursoVigenteSelected() {
		return cursoVigenteSelected;
	}

	public void setCursoVigenteSelected(CursoVigente cursoVigenteSelected) {
		this.cursoVigenteSelected = cursoVigenteSelected;
	}

	public List<EstadoCuenta> getlEstadosCuentas() {
		return lEstadosCuentas;
	}

	public void setlEstadosCuentas(List<EstadoCuenta> lEstadosCuentas) {
		this.lEstadosCuentas = lEstadosCuentas;
	}

	public List<Object[]> getlAlumnosBuscar() {
		return lAlumnosBuscar;
	}

	public void setlAlumnosBuscar(List<Object[]> lAlumnosBuscar) {
		this.lAlumnosBuscar = lAlumnosBuscar;
	}

	public String getBuscarAlumno() {
		return buscarAlumno;
	}

	public void setBuscarAlumno(String buscarAlumno) {
		this.buscarAlumno = buscarAlumno;
	}

	public List<Object[]> getlCursosVigentesBuscar() {
		return lCursosVigentesBuscar;
	}

	public void setlCursosVigentesBuscar(List<Object[]> lCursosVigentesBuscar) {
		this.lCursosVigentesBuscar = lCursosVigentesBuscar;
	}

	public String getBuscarCursoVigente() {
		return buscarCursoVigente;
	}

	public void setBuscarCursoVigente(String buscarCursoVigente) {
		this.buscarCursoVigente = buscarCursoVigente;
	}

	public List<CobranzaDetalle> getlCobranzasDetalles() {
		return lCobranzasDetalles;
	}

	public void setlCobranzasDetalles(List<CobranzaDetalle> lCobranzasDetalles) {
		this.lCobranzasDetalles = lCobranzasDetalles;
	}
}
