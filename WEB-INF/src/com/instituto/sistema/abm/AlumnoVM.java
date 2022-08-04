package com.instituto.sistema.abm;

import java.text.SimpleDateFormat;
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

import com.doxacore.TemplateViewModel;
import com.doxacore.modelo.Ciudad;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.AlumnoEntidad;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.Entidad;
import com.instituto.modelo.Persona;
import com.instituto.modelo.Sede;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class AlumnoVM extends TemplateViewModelLocal {

	private List<Alumno> lAlumnos;
	private List<Alumno> lAlumnosOri;
	private Alumno alumnoSelected;

	private boolean opCrearAlumno;
	private boolean opEditarAlumno;
	private boolean opBorrarAlumno;
	private boolean opCrearAlumnoEntidad;
	private boolean opBorrarAlumnoEntidad;

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
		this.opCrearAlumnoEntidad = this.operacionHabilitada(ParamsLocal.OP_CREAR_ALUMNOENTIDAD);
		this.opBorrarAlumnoEntidad = this.operacionHabilitada(ParamsLocal.OP_BORRAR_ALUMNOENTIDAD);
	}

	private void cargarAlumnos() {

		this.lAlumnos = this.reg.getAllObjectsByCondicionOrder(Alumno.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid(), "Alumnoid asc");
		this.lAlumnosOri = this.lAlumnos;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[6]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lAlumnos")
	public void filtrarAlumno() {

		this.lAlumnos = this.filtrarLT(this.filtroColumns, this.lAlumnosOri);

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

	@Command
	@NotifyChange("lAlumnos")
	public void guardar() {

		if (!verificarCampos()) {
			return;
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
	public void borrarAlumnoConfirmacion(@BindingParam("alumno") final Alumno alumno) {

		if (!this.opBorrarAlumno)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarAlumno(alumno);

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

		this.lPersonasBuscar = this.reg.sqlNativo(sqlPersona);

		this.lPersonasBuscarOri = this.lPersonasBuscar;
	}

	@Command
	@NotifyChange("buscarPersona")
	public void onSelectPersona(@BindingParam("id") long id) {

		this.buscarSelectedPersona = this.reg.getObjectById(Persona.class.getName(), id);
		this.buscarPersona = this.buscarSelectedPersona.getNombreCompleto();
		this.alumnoSelected.setAlumnoid(buscarSelectedPersona.getPersonaid());
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
	
	//seccion AlumnoEntidad
	
	private String cuenta;
	private List<AlumnoEntidad> lAlumnoEntidades;
	
	@Command
	@NotifyChange({"lAlumnoEntidades","cuenta","buscarEntidad"})
	public void modalAlumnoEntidad(@BindingParam("alumnoid") long alumnoid) {

		this.alumnoSelected = this.reg.getObjectById(Alumno.class.getName(), alumnoid);
		
		this.buscarEntidad="";
		this.cuenta="";
		this.buscarSelectedEntidad=null;
		
		this.cargarAlumnoEntidades();

		modal = (Window) Executions.createComponents("/instituto/zul/abm/alumnoEntidadModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	public void cerrarVentana() {
		
		this.modal.detach();
		
	}
	
	@Command
	@NotifyChange({"buscarEntidad","cuenta","lAlumnoEntidades"})
	public void agregarAlumnoEntidad() {
		
		if (!this.isOpCrearAlumnoEntidad()) {
			
			this.mensajeInfo("No tienes Permiso para agregar una entidad al alumno");
			return;
			
		}
		
		if (this.alumnoSelected == null) {
			return;
		}
		
		if (this.buscarSelectedEntidad == null) {
			
			return;
		}
		
		if (this.cuenta.length() < 1) {
			
			return;
		}
		
		AlumnoEntidad alumnoEntidad = new AlumnoEntidad();
		alumnoEntidad.setAlumno(this.alumnoSelected);
		alumnoEntidad.setEntidad(this.buscarSelectedEntidad);
		alumnoEntidad.setCuenta(this.cuenta);
		
		this.save(alumnoEntidad);
		
		this.buscarEntidad="";
		this.cuenta="";
		this.buscarSelectedEntidad=null;
		
		this.cargarAlumnoEntidades();
		
		
	}
	
	
	@NotifyChange("lAlumnoEntidades")
	private void cargarAlumnoEntidades() {
		
		this.lAlumnoEntidades = this.reg.getAllObjectsByCondicionOrder(AlumnoEntidad.class.getName(),
				"alumnoid = " + this.alumnoSelected.getAlumnoid(), "entidadid asc");
		
	}
	
	@Command
	public void borrarEntidadConfirmacion(@BindingParam("alumnoEntidad") final AlumnoEntidad ca) {

		if (!this.opBorrarAlumnoEntidad) {

			this.mensajeError("No tienes permisos para Borrar Entidades a los Alumnos.");

			return;

		}

		this.mensajeEliminar("La Entidad sera removida"
				+" \n Continuar?", new EventListener() {

					@Override
					public void onEvent(Event evt) throws Exception {

						if (evt.getName().equals(Messagebox.ON_YES)) {

							borrarAlumnoEntidad(ca);

						}

					}

				});

	}

	private void borrarAlumnoEntidad(AlumnoEntidad ae) {

		this.reg.deleteObject(ae);

		this.cargarAlumnoEntidades();

		BindUtils.postNotifyChange(null, null, this, "lAlumnoEntidades");

	}
	
	// seccion buscarEntidad

	private List<Object[]> lEntidadesBuscarOri = null;
	private List<Object[]> lEntidadesBuscar = null;
	private Entidad buscarSelectedEntidad = null;
	private String buscarEntidad = "";

	@Command
	@NotifyChange("lEntidadesBuscar")
	public void filtrarEntidadBuscar() {

		this.lEntidadesBuscar = this.filtrarListaObject(buscarEntidad, this.lEntidadesBuscarOri);

	}

	@Command
	@NotifyChange("lEntidadesBuscar")
	public void generarListaBuscarEntidad() {

		String sqlEntidad = this.um.getSql("buscarEntidad.sql");

		this.lEntidadesBuscar = this.reg.sqlNativo(sqlEntidad);

		this.lEntidadesBuscarOri = this.lEntidadesBuscar;
	}

	@Command
	@NotifyChange({"lEntidadesBuscar","buscarEntidad"})
	public void onSelectEntidad(@BindingParam("id") long id) {

		this.buscarSelectedEntidad = this.reg.getObjectById(Entidad.class.getName(), id);
		this.buscarEntidad = this.buscarSelectedEntidad.getEntidad();

	}
	
	


	// fin buscador de Entidad

	public List<Alumno> getlAlumnos() {
		return lAlumnos;
	}

	public void setlAlumnos(List<Alumno> lAlumnos) {
		this.lAlumnos = lAlumnos;
	}

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

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public List<Object[]> getlEntidadesBuscar() {
		return lEntidadesBuscar;
	}

	public void setlEntidadesBuscar(List<Object[]> lEntidadesBuscar) {
		this.lEntidadesBuscar = lEntidadesBuscar;
	}

	public String getBuscarEntidad() {
		return buscarEntidad;
	}

	public void setBuscarEntidad(String buscarEntidad) {
		this.buscarEntidad = buscarEntidad;
	}

	public List<AlumnoEntidad> getlAlumnoEntidades() {
		return lAlumnoEntidades;
	}

	public void setlAlumnoEntidades(List<AlumnoEntidad> lAlumnoEntidades) {
		this.lAlumnoEntidades = lAlumnoEntidades;
	}

	public boolean isOpCrearAlumnoEntidad() {
		return opCrearAlumnoEntidad;
	}

	public void setOpCrearAlumnoEntidad(boolean opCrearAlumnoEntidad) {
		this.opCrearAlumnoEntidad = opCrearAlumnoEntidad;
	}

	public boolean isOpBorrarAlumnoEntidad() {
		return opBorrarAlumnoEntidad;
	}

	public void setOpBorrarAlumnoEntidad(boolean opBorrarAlumnoEntidad) {
		this.opBorrarAlumnoEntidad = opBorrarAlumnoEntidad;
	}

}
