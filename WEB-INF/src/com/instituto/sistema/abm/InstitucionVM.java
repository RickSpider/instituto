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
import com.instituto.modelo.Institucion;
import com.instituto.util.ParamsLocal;

public class InstitucionVM extends TemplateViewModel {

	private List<Institucion> lInstituciones;
	private List<Institucion> lInstitucionesOri;
	private Institucion institucionSelected;

	private boolean opCrearInstitucion;
	private boolean opEditarInstitucion;
	private boolean opBorrarInstitucion;

	@Init(superclass = true)
	public void initInstitucionVM() {

		cargarInstituciones();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeInstitucionVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearInstitucion = this.operacionHabilitada(ParamsLocal.OP_CREAR_INSTITUCION);
		this.opEditarInstitucion = this.operacionHabilitada(ParamsLocal.OP_EDITAR_INSTITUCION);
		this.opBorrarInstitucion = this.operacionHabilitada(ParamsLocal.OP_BORRAR_INSTITUCION);

	}

	private void cargarInstituciones() {

		this.lInstituciones = this.reg.getAllObjectsByCondicionOrder(Institucion.class.getName(), null,
				"Institucionid asc");
		this.lInstitucionesOri = this.lInstituciones;
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
	@NotifyChange("lInstituciones")
	public void filtrarInstitucion() {

		this.lInstituciones = this.filtrarLT(this.filtroColumns, this.lInstitucionesOri);

	}

	// fin seccion

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalInstitucionAgregar() {

		if (!this.opCrearInstitucion)
			return;

		this.editar = false;
		modalInstitucion(-1);

	}

	@Command
	public void modalInstitucion(@BindingParam("institucionid") long institucionid) {

		if (institucionid != -1) {

			if (!this.opEditarInstitucion)
				return;

			this.institucionSelected = this.reg.getObjectById(Institucion.class.getName(), institucionid);
			buscarCiudad = this.institucionSelected.getCiudad().getCiudad();
			this.editar = true;

		} else {
			
			buscarCiudad = "";
			institucionSelected = new Institucion();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/abm/institucionModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		if (this.institucionSelected.getInstitucion() == null
				|| this.institucionSelected.getInstitucion().length() <= 0) {

			return false;

		}
		
		if (this.institucionSelected.getCiudad() == null) {
			
			return false;
			
		}
		
		if (this.institucionSelected.getDireccion() == null || this.institucionSelected.getDireccion().length() <= 0) {
			
			return false;
			
		}
		
		if (this.institucionSelected.getTelefono() == null || this.institucionSelected.getTelefono().length() <= 0) {
			
			return false;
			
		}
		
		if (this.institucionSelected.getEmail() == null || this.institucionSelected.getEmail().length() <= 0) {
			
			return false;
			
		}

		return true;
	}

	@Command
	@NotifyChange("lInstituciones")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}

		this.save(institucionSelected);

		this.institucionSelected = null;

		this.cargarInstituciones();

		this.modal.detach();

		if (editar) {

			Notification.show("La Institucion fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("La Institucion fue agregada.");
		}

	}

	// fin modal

	@Command
	public void borrarInstitucionConfirmacion(@BindingParam("institucion") final Institucion institucion) {

		if (!this.opBorrarInstitucion)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarInstitucion(institucion);

				}

			}

		};

		this.mensajeEliminar("La Institucion sera eliminada. \n Continuar?", event);
	}

	private void borrarInstitucion(Institucion institucion) {

		this.reg.deleteObject(institucion);

		this.cargarInstituciones();

		BindUtils.postNotifyChange(null, null, this, "lInstituciones");

	}

	// buscador de Ciudad

	private List<Object[]> lCiudadesBuscarOri = null;
	private List<Object[]> lCiudadesBuscar = null;
	private Ciudad buscarSelectedCiudad = null;
	private String buscarCiudad = "";

	@Command
	@NotifyChange("lCiudadesBuscar")
	public void filtrarCiudadBuscar() {

		this.lCiudadesBuscar = this.filtrarListaObject(buscarCiudad, this.lCiudadesBuscarOri);

	}

	@Command
	@NotifyChange("lCiudadesBuscar")
	public void generarListaBuscarCiudad() {

		this.lCiudadesBuscar = this.reg
				.sqlNativo("select c.ciudadid," + " c.ciudad," + " d.departamento," + " p.pais \n" + "from ciudades c\n"
						+ "left join departamentos d on d.departamentoid = c.departamentoid\n"
						+ "left join paises p on p.paisid = d.paisid\n" + "order by c.ciudadid asc;");

		this.lCiudadesBuscarOri = this.lCiudadesBuscar;
	}

	@Command
	@NotifyChange("buscarCiudad")
	public void onSelectCiudad(@BindingParam("id") long id) {

		this.buscarSelectedCiudad = this.reg.getObjectById(Ciudad.class.getName(), id);
		this.buscarCiudad = this.buscarSelectedCiudad.getCiudad();
		this.institucionSelected.setCiudad(buscarSelectedCiudad);

	}

	// fin buscar ciudad

	public List<Institucion> getlInstituciones() {
		return lInstituciones;
	}

	public void setlInstituciones(List<Institucion> lInstituciones) {
		this.lInstituciones = lInstituciones;
	}

	public boolean isOpCrearInstitucion() {
		return opCrearInstitucion;
	}

	public void setOpCrearInstitucion(boolean opCrearInstitucion) {
		this.opCrearInstitucion = opCrearInstitucion;
	}

	public boolean isOpEditarInstitucion() {
		return opEditarInstitucion;
	}

	public void setOpEditarInstitucion(boolean opEditarInstitucion) {
		this.opEditarInstitucion = opEditarInstitucion;
	}

	public boolean isOpBorrarInstitucion() {
		return opBorrarInstitucion;
	}

	public void setOpBorrarInstitucion(boolean opBorrarInstitucion) {
		this.opBorrarInstitucion = opBorrarInstitucion;
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

	public Institucion getInstitucionSelected() {
		return institucionSelected;
	}

	public void setInstitucionSelected(Institucion institucionSelected) {
		this.institucionSelected = institucionSelected;
	}

	public List<Object[]> getlCiudadesBuscar() {
		return lCiudadesBuscar;
	}

	public void setlCiudadesBuscar(List<Object[]> lCiudadesBuscar) {
		this.lCiudadesBuscar = lCiudadesBuscar;
	}

	public Ciudad getBuscarSelectedCiudad() {
		return buscarSelectedCiudad;
	}

	public void setBuscarSelectedCiudad(Ciudad buscarSelectedCiudad) {
		this.buscarSelectedCiudad = buscarSelectedCiudad;
	}

	public String getBuscarCiudad() {
		return buscarCiudad;
	}

	public void setBuscarCiudad(String buscarCiudad) {
		this.buscarCiudad = buscarCiudad;
	}

}
