package com.instituto.sistema.abm;

import java.util.List;

import org.zkoss.bind.BindUtils;
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
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.Persona;

import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class AlumnoVM extends TemplateViewModelLocal {

	private List<Object[]> lAlumnos;
	private List<Object[]> lAlumnosOri;
	private Alumno alumnoSelected;

	private boolean opCrearAlumno;
	private boolean opEditarAlumno;
	private boolean opBorrarAlumno;


	@Init(superclass = true)
	public void initAlumnoVM() {

		cargarAlumnos();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeAlumnoVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearAlumno = this.operacionHabilitada(ParamsLocal.OP_CREAR_ALUMNO);
		this.opEditarAlumno = this.operacionHabilitada(ParamsLocal.OP_EDITAR_ALUMNO);
		this.opBorrarAlumno = this.operacionHabilitada(ParamsLocal.OP_BORRAR_ALUMNO);
	
	}

	private void cargarAlumnos() {
		
		String sql = this.um.getSql("alumno/alumnos.sql").replace("?1", this.getCurrentSede().getSedeid()+"");
		
		this.lAlumnos = this.reg.sqlNativo(sql);

		/*this.lAlumnos = this.reg.getAllObjectsByCondicionOrder(Alumno.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid(), "Alumnoid asc");*/
		this.lAlumnosOri = this.lAlumnos;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[8]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lAlumnos")
	public void filtrarAlumno() {

	//	this.lAlumnos = this.filtrarLT(this.filtroColumns, this.lAlumnosOri);
		this.lAlumnos = this.filtrarListaObject(this.filtroColumns, this.lAlumnosOri);

	}

	// fin seccion

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalAlumnoAgregar() {

		if (!this.opCrearAlumno)
			return;

		this.editar = false;
		modalAlumno(-1);

	}

	@Command
	public void modalAlumno(@BindingParam("alumnoid") long alumnoid) {

		if (alumnoid != -1) {

			if (!this.opEditarAlumno)
				return;

			this.alumnoSelected = this.reg.getObjectById(Alumno.class.getName(), alumnoid);
			this.buscarPersona = this.alumnoSelected.getPersona().getNombreCompleto();
			this.editar = true;
			this.buscarPersonaFacturacion = "";

			if (this.alumnoSelected.getPersonaFacturacion() != null) {

				this.buscarPersonaFacturacion = this.alumnoSelected.getPersonaFacturacion().getRazonSocial();

			}

		} else {

			alumnoSelected = new Alumno();
			alumnoSelected.setSede(getCurrentSede());
			this.buscarPersona = "";
			this.buscarPersonaFacturacion = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/abm/alumnoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	@Command
	public void modalAlumnoPersona(@BindingParam("alumnoid") long alumnoid) {

		this.alumnoSelected = this.reg.getObjectById(Alumno.class.getName(), alumnoid);
		this.buscarPersona = this.alumnoSelected.getPersona().getNombreCompleto();

		modal = (Window) Executions.createComponents("/instituto/zul/abm/alumnoPersonaModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private List<CursoVigenteAlumno> lCursosVigentesAlumnos;

	@Command
	public void modalAlumnoCurso(@BindingParam("alumnoid") long alumnoid) {

		this.alumnoSelected = this.reg.getObjectById(Alumno.class.getName(), alumnoid);
		this.buscarPersona = this.alumnoSelected.getPersona().getNombreCompleto();

		lCursosVigentesAlumnos = this.reg.getAllObjectsByCondicionOrder(CursoVigenteAlumno.class.getName(),
				"alumnoid = " + alumnoid, "cursovigenteid asc");

		modal = (Window) Executions.createComponents("/instituto/zul/abm/alumnoCursoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		if (this.alumnoSelected.getPersona() == null) {

			return false;

		}

		if (this.alumnoSelected.getSede() == null) {

			return false;

		}

		return true;
	}

	private boolean inactivar() {
		
		String sql ="select coalesce( sum(monto),0 )as monto,coalesce( sum(pago),0 )as pago,coalesce( sum(monto -pago),0 ) as diff from estadoscuentas\r\n" + 
				"where alumnoid = "+this.alumnoSelected.getAlumnoid()+";";
		
		
		List<Object[]> totalEstadoCuenta = this.reg.sqlNativo(sql);
		
		
		if (Double.parseDouble(totalEstadoCuenta.get(0)[2].toString()) > 0) {
			
			return false;
			
		}
		
		return true;
	}
	
	@Command
	@NotifyChange("lAlumnos")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}
		
		if (this.alumnoSelected.getAlumnoid() != null && !this.alumnoSelected.isActivo() ) {
			
			if (!this.inactivar()) {
				
				this.mensajeError("No se puede inactivar al alumno posee estados de cuentas.");
				return;
				
			}
			
		}

		this.alumnoSelected.setSede(this.getCurrentSede());

		this.save(alumnoSelected);

		this.alumnoSelected = null;

		this.cargarAlumnos();

		this.modal.detach();

		if (editar) {

			Notification.show("El Alumno fue Actualizado.");
			this.editar = false;
		} else {

			Notification.show("El Alumno fue agregado.");
		}

	}

	// fin modal

	@Command
	public void borrarAlumnoConfirmacion(@BindingParam("alumnoid") final long alumnoid) {

		if (!this.opBorrarAlumno)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarAlumno(reg.getObjectById(Alumno.class.getName(), alumnoid));

				}

			}

		};

		this.mensajeEliminar("La Alumno sera eliminada. \n Continuar?", event);
	}

	private void borrarAlumno(Alumno alumno) {

		this.reg.deleteObject(alumno);

		this.cargarAlumnos();

		BindUtils.postNotifyChange(null, null, this, "lAlumnos");

	}

	// seccion buscarPersona

	private List<Object[]> lPersonasBuscarOri = null;
	private List<Object[]> lPersonasBuscar = null;
	private Persona buscarSelectedPersona = null;
	private String buscarPersona = "";

	@Command
	@NotifyChange("lPersonasBuscar")
	public void filtrarPersonaBuscar() {

		this.lPersonasBuscar = this.filtrarListaObject(buscarPersona, this.lPersonasBuscarOri);

	}

	@Command
	@NotifyChange("lPersonasBuscar")
	public void generarListaBuscarPersona() {

		String sqlPersona = this.um.getSql("buscarPersonaNotAlumno.sql");
		
		sqlPersona = sqlPersona.replace("##SEDEID##", this.getCurrentSede().getSedeid()+"");

		this.lPersonasBuscar = this.reg.sqlNativo(sqlPersona);

		this.lPersonasBuscarOri = this.lPersonasBuscar;
	}

	@Command
	@NotifyChange("buscarPersona")
	public void onSelectPersona(@BindingParam("id") long id) {

		this.buscarSelectedPersona = this.reg.getObjectById(Persona.class.getName(), id);
		this.buscarPersona = this.buscarSelectedPersona.getNombreCompleto();
		//this.alumnoSelected.setAlumnoid(buscarSelectedPersona.getPersonaid());
		this.alumnoSelected.setPersona(buscarSelectedPersona);

	}

	// fin buscador de Persona

	// seccion buscarPersonaFacturacion

	private List<Object[]> lPersonasFacturacionBuscarOri = null;
	private List<Object[]> lPersonasFacturacionBuscar = null;
	private Persona buscarSelectedPersonaFacturacion = null;
	private String buscarPersonaFacturacion = "";

	@Command
	@NotifyChange("lPersonasFacturacionBuscar")
	public void filtrarPersonaFacturacionBuscar() {

		this.lPersonasFacturacionBuscar = this.filtrarListaObject(buscarPersonaFacturacion,
				this.lPersonasFacturacionBuscarOri);

	}

	@Command
	@NotifyChange("lPersonasFacturacionBuscar")
	public void generarListaBuscarPersonaFacturacion() {

		String sqlPersonaFacturacion = this.um.getSql("buscarPersonaFacturacion.sql");

		this.lPersonasFacturacionBuscar = this.reg.sqlNativo(sqlPersonaFacturacion);

		this.lPersonasFacturacionBuscarOri = this.lPersonasFacturacionBuscar;
	}

	@Command
	@NotifyChange("lPersonasFacturacionBuscar")
	public void onSelectPersonaFacturacion(@BindingParam("id") long id) {

		this.buscarSelectedPersonaFacturacion = this.reg.getObjectById(Persona.class.getName(), id);
		this.buscarPersonaFacturacion = this.buscarSelectedPersonaFacturacion.getRazonSocial();
		this.alumnoSelected.setPersonaFacturacion(buscarSelectedPersonaFacturacion);

	}

	// fin buscador de PersonaFacturacion
	

	

	public Alumno getAlumnoSelected() {
		return alumnoSelected;
	}

	public void setAlumnoSelected(Alumno alumnoSelected) {
		this.alumnoSelected = alumnoSelected;
	}

	public boolean isOpCrearAlumno() {
		return opCrearAlumno;
	}

	public void setOpCrearAlumno(boolean opCrearAlumno) {
		this.opCrearAlumno = opCrearAlumno;
	}

	public boolean isOpEditarAlumno() {
		return opEditarAlumno;
	}

	public void setOpEditarAlumno(boolean opEditarAlumno) {
		this.opEditarAlumno = opEditarAlumno;
	}

	public boolean isOpBorrarAlumno() {
		return opBorrarAlumno;
	}

	public void setOpBorrarAlumno(boolean opBorrarAlumno) {
		this.opBorrarAlumno = opBorrarAlumno;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public List<Object[]> getlPersonasBuscar() {
		return lPersonasBuscar;
	}

	public void setlPersonasBuscar(List<Object[]> lPersonasBuscar) {
		this.lPersonasBuscar = lPersonasBuscar;
	}

	public String getBuscarPersona() {
		return buscarPersona;
	}

	public void setBuscarPersona(String buscarPersona) {
		this.buscarPersona = buscarPersona;
	}

	public List<CursoVigenteAlumno> getlCursosVigentesAlumnos() {
		return lCursosVigentesAlumnos;
	}

	public void setlCursosVigentesAlumnos(List<CursoVigenteAlumno> lCursosVigentesAlumnos) {
		this.lCursosVigentesAlumnos = lCursosVigentesAlumnos;
	}

	public List<Object[]> getlPersonasFacturacionBuscar() {
		return lPersonasFacturacionBuscar;
	}

	public void setlPersonasFacturacionBuscar(List<Object[]> lPersonasFacturacionBuscar) {
		this.lPersonasFacturacionBuscar = lPersonasFacturacionBuscar;
	}

	public String getBuscarPersonaFacturacion() {
		return buscarPersonaFacturacion;
	}

	public void setBuscarPersonaFacturacion(String buscarPersonaFacturacion) {
		this.buscarPersonaFacturacion = buscarPersonaFacturacion;
	}

	public List<Object[]> getlAlumnos() {
		return lAlumnos;
	}

	public void setlAlumnos(List<Object[]> lAlumnos) {
		this.lAlumnos = lAlumnos;
	}
}
