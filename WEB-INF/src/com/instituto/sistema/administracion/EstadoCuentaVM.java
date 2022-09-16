package com.instituto.sistema.administracion;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.instituto.modelo.Alumno;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.Concepto;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteMateria;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class EstadoCuentaVM extends TemplateViewModelLocal {

	private Alumno alumnoSelected;
	private CursoVigente cursoVigenteSelected;
	private List<EstadoCuenta> lEstadosCuentas;
	private List<CobranzaDetalle> lCobranzasDetalles;
	
	private EstadoCuenta estadoCuentaSelected;
	
	private boolean opCrearEstadoCuenta;

	@Init(superclass = true)
	public void initEstadoCuentaVM() {

	}

	@AfterCompose(superclass = true)
	public void afterComposeEstadoCuentaVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearEstadoCuenta = this.operacionHabilitada(ParamsLocal.OP_CREAR_ESTADOCUENTA);

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

		refrescarEstadosCuentas();

	}
	
	@NotifyChange({"lEstadosCuentas"})
	public void refrescarEstadosCuentas() {
		
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

	//Agregar estado cuenta
	
	private Window modal;
	
	@Command
	public void modalEstadoCuenta() {
		
		if (!this.opCrearEstadoCuenta) {
			return;			
		}
		
		if (this.alumnoSelected == null) {
			this.mensajeInfo("Debes seleccionar un alumno.");
			return;
			
		} 
		
		if (this.cursoVigenteSelected == null) {
			
			this.mensajeInfo("Debes seleccionar un curso.");
			return;
		}
		
		this.buscarConcepto = "";
		
		this.estadoCuentaSelected = new EstadoCuenta();
		
		this.estadoCuentaSelected.setAlumno(this.alumnoSelected);
		this.estadoCuentaSelected.setCursoVigente(this.cursoVigenteSelected);
		this.estadoCuentaSelected.setCargaManual(true);
		

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/estadoCuentaModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	

	@Command
	@NotifyChange("lEstadosCuentas")
	public void guardar() {
		
		this.save(this.estadoCuentaSelected);
		
		Notification.show("El Estado de cuenta fue actualizado.");
		
		this.modal.detach();
		
		refrescarEstadosCuentas();
		
		
	}
	
	//Buscar concepto
	private List<Object[]> lConceptosbuscarOri;
	private List<Object[]> lConceptosBuscar;
	private Concepto buscarSelectedConcepto;
	private String buscarConcepto = "";

	@Command
	@NotifyChange("lConceptosBuscar")
	public void filtrarConceptoBuscar() {

		this.lConceptosBuscar = this.filtrarListaObject(buscarConcepto, this.lConceptosbuscarOri);

	}

	@Command
	@NotifyChange("lConceptosBuscar")
	public void generarListaBuscarConcepto() {

		String sqlBuscarConcepto = this.um.getSql("buscarConcepto.sql");

		this.lConceptosBuscar = this.reg.sqlNativo(sqlBuscarConcepto);
		this.lConceptosbuscarOri = this.lConceptosBuscar;
	}

	@Command
	@NotifyChange("buscarConcepto")
	public void onSelectConcepto(@BindingParam("id") long id) {

		this.buscarSelectedConcepto = this.reg.getObjectById(Concepto.class.getName(), id);
		this.buscarConcepto = buscarSelectedConcepto.getConcepto();
		this.estadoCuentaSelected.setConcepto(buscarSelectedConcepto);

	}

	//Fin agregar estado cuenta
	
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

	public EstadoCuenta getEstadoCuentaSelected() {
		return estadoCuentaSelected;
	}

	public void setEstadoCuentaSelected(EstadoCuenta estadoCuentaSelected) {
		this.estadoCuentaSelected = estadoCuentaSelected;
	}

	public List<Object[]> getlConceptosBuscar() {
		return lConceptosBuscar;
	}

	public void setlConceptosBuscar(List<Object[]> lConceptosBuscar) {
		this.lConceptosBuscar = lConceptosBuscar;
	}

	public String getBuscarConcepto() {
		return buscarConcepto;
	}

	public void setBuscarConcepto(String buscarConcepto) {
		this.buscarConcepto = buscarConcepto;
	}

	public boolean isOpCrearEstadoCuenta() {
		return opCrearEstadoCuenta;
	}

	public void setOpCrearEstadoCuenta(boolean opCrearEstadoCuenta) {
		this.opCrearEstadoCuenta = opCrearEstadoCuenta;
	}
}
