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

import com.doxacore.TemplateViewModel;
import com.doxacore.modelo.Ciudad;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.Persona;
import com.instituto.modelo.Sede;
import com.instituto.util.ParamsLocal;

public class AlumnoVM extends TemplateViewModel {

	private List<Alumno> lAlumnos;
	private List<Alumno> lAlumnosOri;
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

		this.lAlumnos = this.reg.getAllObjectsByCondicionOrder(Alumno.class.getName(), null, "Alumnoid asc");
		this.lAlumnosOri = this.lAlumnos;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
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
			this.buscarSede = this.alumnoSelected.getSede().getSede();
			this.buscarPersona = this.alumnoSelected.getPersona().getNombre();
			this.editar = true;

		} else {

			alumnoSelected = new Alumno();
			this.buscarSede = "";
			this.buscarPersona = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/abm/alumnoModal.zul", this.mainComponent, null);
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
		
		if (this.alumnoSelected.getMatricula() == null || this.alumnoSelected.getMatricula( ).length() <=0) {
			
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
		this.buscarPersona = this.buscarSelectedPersona.getNombre();
		this.alumnoSelected.setAlumnoid(buscarSelectedPersona.getPersonaid());
		this.alumnoSelected.setPersona(buscarSelectedPersona);

	}

	// fin buscador de Persona

	// seccion buscarSede

	private List<Object[]> lSedesBuscarOri = null;
	private List<Object[]> lSedesBuscar = null;
	private Sede buscarSelectedSede = null;
	private String buscarSede = "";

	@Command
	@NotifyChange("lSedesBuscar")
	public void filtrarSedeBuscar() {

		this.lSedesBuscar = this.filtrarListaObject(buscarSede, this.lSedesBuscarOri);

	}

	@Command
	@NotifyChange("lSedesBuscar")
	public void generarListaBuscarSede() {

		String sqlSede = this.um.getSql("buscarSede.sql");

		this.lSedesBuscar = this.reg.sqlNativo(sqlSede);

		this.lSedesBuscarOri = this.lSedesBuscar;
	}

	@Command
	@NotifyChange("buscarSede")
	public void onSelectSede(@BindingParam("id") long id) {

		this.buscarSelectedSede = this.reg.getObjectById(Sede.class.getName(), id);
		this.buscarSede = this.buscarSelectedSede.getSede();
		this.alumnoSelected.setSede(buscarSelectedSede);

	}

	// fin buscador de Sede

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

	public List<Object[]> getlSedesBuscar() {
		return lSedesBuscar;
	}

	public void setlSedesBuscar(List<Object[]> lSedesBuscar) {
		this.lSedesBuscar = lSedesBuscar;
	}

	public String getBuscarSede() {
		return buscarSede;
	}

	public void setBuscarSede(String buscarSede) {
		this.buscarSede = buscarSede;
	}

}
