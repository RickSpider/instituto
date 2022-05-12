package com.instituto.sistema.admnistracion;

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
import com.doxacore.report.CustomDataSource;
import com.doxacore.report.ReportConfig;
import com.instituto.util.ParamsLocal;
import com.doxacore.util.UtilStaticMetodos;
import com.instituto.modelo.Convenio;
import com.instituto.modelo.ConvenioAlumno;
import com.instituto.modelo.Institucion;
import com.instituto.modelo.Alumno;

public class ConvenioVM extends TemplateViewModel {

	private List<Convenio> lConvenios;
	private List<Convenio> lConveniosOri;
	private List<ConvenioAlumno> lAlumnosConvenios;
	private Convenio convenioSelected;
	private Convenio convenioSelectedAlumno;
	private String filtroColumnsConvenio[];

	private boolean opCrearConvenio;
	private boolean opEditarConvenio;
	private boolean opBorrarConvenio;

	private boolean opAgregarAlumno;
	private boolean opQuitarAlumno;

	@Init(superclass = true)
	public void initConvenioVM() {

		cargarConvenios();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeConvenioVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearConvenio = this.operacionHabilitada(ParamsLocal.OP_CREAR_CONVENIO);
		this.opEditarConvenio = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CONVENIO);
		this.opBorrarConvenio = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CONVENIO);

		this.opAgregarAlumno = this.operacionHabilitada(ParamsLocal.OP_AGREGAR_ALUMNO);
		this.opQuitarAlumno = this.operacionHabilitada(ParamsLocal.OP_QUITAR_ALUMNO);

	}

	private void cargarConvenios() {

		this.lConvenios = this.reg.getAllObjectsByCondicionOrder(Convenio.class.getName(), null, "convenioid asc");
		this.lConveniosOri = this.lConvenios;

	}

	private void inicializarFiltros() {

		this.filtroColumnsConvenio = new String[3]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en
													// el modelo

		for (int i = 0; i < this.filtroColumnsConvenio.length; i++) {

			this.filtroColumnsConvenio[i] = "";

		}

	}

	@Command
	@NotifyChange("lConvenios")
	public void filtrarConvenio() {

		lConvenios = this.filtrarLT(this.filtroColumnsConvenio, this.lConveniosOri);

	}

	// Seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalConvenioAgregar() {

		if (!this.opCrearConvenio)
			return;

		this.editar = false;
		modalConvenio(-1);

	}

	@Command
	public void modalConvenio(@BindingParam("convenioid") long convenioid) {

		if (convenioid != -1) {

			if (!this.opEditarConvenio)
				return;

			this.convenioSelected = this.reg.getObjectById(Convenio.class.getName(), convenioid);
			this.buscarInstitucion = this.convenioSelected.getInstitucion().getInstitucion();
			this.editar = true;

		} else {

			this.convenioSelected = new Convenio();
			this.buscarInstitucion = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/convenioModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		if (this.convenioSelected.getInstitucion() == null) {

			return false;

		}

		return true;
	}

	@Command
	@NotifyChange("lConvenios")
	public void guardar() {

		if (!verificarCampos()) {

			return;

		}

		this.save(convenioSelected);

		this.convenioSelected = null;

		this.cargarConvenios();

		this.modal.detach();

		if (editar) {

			Notification.show("El Convenio fue Actualizado.");
			this.editar = false;

		} else {

			Notification.show("El Convenio fue agregado.");
		}

	}

	// Fin Seccion Modal

	@Command
	public void borrarConvenioConfirmacion(@BindingParam("convenio") final Convenio convenio) {

		if (!this.opBorrarConvenio)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarConvenio(convenio);

				}

			}

		};

		this.mensajeEliminar("El convenio sera eliminado. \n Continuar?", event);
	}

	private void borrarConvenio(Convenio convenio) {

		this.reg.deleteObject(convenio);

		this.cargarConvenios();

		BindUtils.postNotifyChange(null, null, this, "lConvenios");

	}

	// Seccion alumnos convenio

	@Command
	@NotifyChange({ "lAlumnosConvenios", "buscarAlumno" })
	public void refrescarAlumnos(@BindingParam("convenio") Convenio convenio) {

		this.convenioSelectedAlumno = convenio;
		this.lAlumnosConvenios = this.reg.getAllObjectsByCondicionOrder(ConvenioAlumno.class.getName(),
				"convenioid = " + convenio.getConvenioid(), "alumnoid asc");

		this.buscarSelectedAlumno = null;
		this.buscarAlumno = "";

	}

	@Command
	public void borrarAlumnoConfirmacion(@BindingParam("convenioalumno") final ConvenioAlumno ca) {

		if (!this.opQuitarAlumno) {

			this.mensajeError("No tienes permisos para Borrar Alumnos a un Convenio.");

			return;

		}

		this.mensajeEliminar("El Alumno " + ca.getAlumno().getFullNombre() + " sera removido del Convenio "
				+ ca.getConvenio().getInstitucion().getInstitucion() + " \n Continuar?", new EventListener() {

					@Override
					public void onEvent(Event evt) throws Exception {

						if (evt.getName().equals(Messagebox.ON_YES)) {

							borrarConvenioAlumno(ca);

						}

					}

				});

	}

	private void borrarConvenioAlumno(ConvenioAlumno ca) {

		this.reg.deleteObject(ca);

		this.refrescarAlumnos(this.convenioSelectedAlumno);

		BindUtils.postNotifyChange(null, null, this, "lAlumnosConvenios");

	}

	// fin alumnos convenio

	// seccion buscador

	private List<Object[]> lAlumnosbuscarOri;
	private List<Object[]> lAlumnosBuscar;
	private Alumno buscarSelectedAlumno;
	private String buscarAlumno = "";

	@Command
	@NotifyChange("lAlumnosBuscar")
	public void filtrarAlumnoBuscar() {

		this.lAlumnosBuscar = this.filtrarListaObject(buscarAlumno, this.lAlumnosbuscarOri);

	}

	@Command
	@NotifyChange("lAlumnosBuscar")
	public void generarListaBuscarAlumno() {

		if (this.convenioSelectedAlumno == null) {

			this.mensajeInfo("Selecciona un Convenio.");

			return;
		}

		String sqlBuscarAlumno = this.um.getSql("buscarAlumno.sql");

		this.lAlumnosBuscar = this.reg.sqlNativo(sqlBuscarAlumno);
		this.lAlumnosbuscarOri = this.lAlumnosBuscar;
	}

	@Command
	@NotifyChange("buscarAlumno")
	public void onSelectAlumno(@BindingParam("id") long id) {

		this.buscarSelectedAlumno = this.reg.getObjectById(Alumno.class.getName(), id);
		this.buscarAlumno = buscarSelectedAlumno.getFullNombre();

	}

	@Command
	@NotifyChange({ "lAlumnosConvenios", "buscarAlumno" })
	public void agregarAlumno() {

		if (!this.opAgregarAlumno) {

			this.mensajeError("No tienes permiso para agregar un Alumno al Convenio. ");
			return;
		}

		if (this.buscarSelectedAlumno == null) {

			this.mensajeInfo("Selecciona un Alumno para agregar.");

			return;

		}

		for (ConvenioAlumno x : this.lAlumnosConvenios) {

			if (this.buscarSelectedAlumno.getAlumnoid() == x.getAlumno().getAlumnoid()) {

				this.mensajeError("El Convenio ya tiene el alumno " + x.getAlumno().getFullNombre());

				return;

			}

		}

		ConvenioAlumno cu = new ConvenioAlumno();
		cu.setAlumno(this.buscarSelectedAlumno);
		cu.setConvenio(this.convenioSelectedAlumno);
		this.save(cu);

		this.refrescarAlumnos(this.convenioSelectedAlumno);

	}

	// fins buscador

	// buscador de Institucion

	private List<Object[]> lInstitucionesBuscarOri = null;
	private List<Object[]> lInstitucionesBuscar = null;
	private Institucion buscarSelectedInstitucion = null;
	private String buscarInstitucion = "";

	@Command
	@NotifyChange("lInstitucionesBuscar")
	public void filtrarInstitucionBuscar() {

		this.lInstitucionesBuscar = this.filtrarListaObject(buscarInstitucion, this.lInstitucionesBuscarOri);

	}

	@Command
	@NotifyChange("lInstitucionesBuscar")
	public void generarListaBuscarInstitucion() {

		String sqlInstitucion = this.um.getSql("buscarInstitucion.sql") ;
		
		this.lInstitucionesBuscar = this.reg.sqlNativo(sqlInstitucion);

		this.lInstitucionesBuscarOri = this.lInstitucionesBuscar;
	}

	@Command
	@NotifyChange("buscarInstitucion")
	public void onSelectInstitucion(@BindingParam("id") long id) {

		this.buscarSelectedInstitucion = this.reg.getObjectById(Institucion.class.getName(), id);
		this.buscarInstitucion = this.buscarSelectedInstitucion.getInstitucion();
		this.convenioSelected.setInstitucion(buscarSelectedInstitucion);

	}

	// fin buscar ciudad

	public List<Convenio> getlConvenios() {
		return lConvenios;
	}

	public void setlConvenios(List<Convenio> lConvenios) {
		this.lConvenios = lConvenios;
	}

	public Convenio getConvenioSelected() {
		return convenioSelected;
	}

	public void setConvenioSelected(Convenio convenioSelected) {
		this.convenioSelected = convenioSelected;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public List<ConvenioAlumno> getlAlumnosConvenios() {
		return lAlumnosConvenios;
	}

	public void setlAlumnosConvenios(List<ConvenioAlumno> lAlumnosConvenios) {
		this.lAlumnosConvenios = lAlumnosConvenios;
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

	public String[] getFiltroColumnsConvenio() {
		return filtroColumnsConvenio;
	}

	public void setFiltroColumnsConvenio(String[] filtroColumnsConvenio) {
		this.filtroColumnsConvenio = filtroColumnsConvenio;
	}

	public boolean isOpCrearConvenio() {
		return opCrearConvenio;
	}

	public void setOpCrearConvenio(boolean opCrearConvenio) {
		this.opCrearConvenio = opCrearConvenio;
	}

	public boolean isOpEditarConvenio() {
		return opEditarConvenio;
	}

	public void setOpEditarConvenio(boolean opEditarConvenio) {
		this.opEditarConvenio = opEditarConvenio;
	}

	public boolean isOpBorrarConvenio() {
		return opBorrarConvenio;
	}

	public void setOpBorrarConvenio(boolean opBorrarConvenio) {
		this.opBorrarConvenio = opBorrarConvenio;
	}

	public boolean isOpAgregarAlumno() {
		return opAgregarAlumno;
	}

	public void setOpAgregarAlumno(boolean opAgregarAlumno) {
		this.opAgregarAlumno = opAgregarAlumno;
	}

	public boolean isOpQuitarAlumno() {
		return opQuitarAlumno;
	}

	public void setOpQuitarAlumno(boolean opQuitarAlumno) {
		this.opQuitarAlumno = opQuitarAlumno;
	}

	public List<Object[]> getlInstitucionesBuscar() {
		return lInstitucionesBuscar;
	}

	public void setlInstitucionesBuscar(List<Object[]> lInstitucionesBuscar) {
		this.lInstitucionesBuscar = lInstitucionesBuscar;
	}

	public String getBuscarInstitucion() {
		return buscarInstitucion;
	}

	public void setBuscarInstitucion(String buscarInstitucion) {
		this.buscarInstitucion = buscarInstitucion;
	}

}
