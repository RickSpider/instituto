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
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;
import com.instituto.modelo.Concepto;
import com.instituto.modelo.Servicio;
import com.instituto.util.ParamsLocal;

public class ServicioVM extends TemplateViewModel {

	private List<Object[]> lServicios;
	private List<Object[]> lServiciosOri;
	private Servicio servicioSelected;

	private boolean opCrearServicio;
	private boolean opEditarServicio;
	private boolean opBorrarServicio;

	@Init(superclass = true)
	public void initServicioVM() {

		cargarServicios();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeServicioVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearServicio = this.operacionHabilitada(ParamsLocal.OP_CREAR_SERVICIO);
		this.opEditarServicio = this.operacionHabilitada(ParamsLocal.OP_EDITAR_SERVICIO);
		this.opBorrarServicio = this.operacionHabilitada(ParamsLocal.OP_BORRAR_SERVICIO);

	}

	private void cargarServicios() {

		String sqlServicio = this.um.getSql("servicio/listaServicio.sql");
		
		this.lServicios = this.reg.sqlNativo(sqlServicio);
		this.lServiciosOri = this.lServicios;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[4]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lServicios")
	public void filtrarServicio() {

		this.lServicios = this.filtrarListaObject(this.filtroColumns, this.lServiciosOri);

	}

	// fin seccion

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalServicioAgregar() {

		if (!this.opCrearServicio)
			return;

		this.editar = false;
		modalServicio(-1);

	}

	@Command
	public void modalServicio(@BindingParam("servicioid") long servicioid) {
		
		this.inicializarFinders();

		if (servicioid != -1) {

			if (!this.opEditarServicio)
				return;
			
			this.servicioSelected = this.reg.getObjectById(Servicio.class.getName(), servicioid);

		} else {

			servicioSelected = new Servicio();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/abm/servicioModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		if (this.servicioSelected.getServicio() == null || this.servicioSelected.getServicio().length() <= 0) {

			return false;

		}

		return true;
	}

	@Command
	@NotifyChange("lServicios")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}

		this.save(servicioSelected);

		this.servicioSelected = null;

		this.cargarServicios();

		this.modal.detach();

		if (editar) {

			Notification.show("La Servicio fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("La Servicio fue agregada.");
		}

	}

	// fin modal

	@Command
	public void borrarServicioConfirmacion(@BindingParam("servicioid") final long servicioid) {

		if (!this.opBorrarServicio)
			return;
		
		Servicio s = this.reg.getObjectById(Servicio.class.getName(), servicioid);

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarServicio(s);

				}

			}

		};

		this.mensajeEliminar("El Servicio sera eliminada. \n Continuar?", event);
	}

	private void borrarServicio(Servicio servicio) {

		this.reg.deleteObject(servicio);

		this.cargarServicios();

		BindUtils.postNotifyChange(null, null, this, "lServicios");

	}

	// Seccion Finder

	private FinderModel conceptoFinder;
	
	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlConcepto = "Select c.conceptoid as id, c.concepto as concepto, t.tipo as impuesto  from conceptos c \r\n" + 
				"	join tipos t on t.tipoid = c.impuestotipoid\r\n" + 
				"	order by c.conceptoid asc";

		conceptoFinder = new FinderModel("Concepto", sqlConcepto);
		
		

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.conceptoFinder.getNameFinder()) == 0) {

			this.conceptoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.conceptoFinder, "listFinder");

		}
		
		
	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {



		if (finder.compareTo(this.conceptoFinder.getNameFinder()) == 0) {

			this.conceptoFinder.setListFinder(this.filtrarListaObject(filter, this.conceptoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.conceptoFinder, "listFinder");

		}

		
		

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.conceptoFinder.getNameFinder()) == 0) {

			this.servicioSelected.setConcepto(this.reg.getObjectById(Concepto.class.getName(), id));
			//BindUtils.postNotifyChange(null, null, this, "contribuyenteFinder");
		}
		
	


	}

	

	public Servicio getServicioSelected() {
		return servicioSelected;
	}

	public void setServicioSelected(Servicio servicioSelected) {
		this.servicioSelected = servicioSelected;
	}

	public boolean isOpCrearServicio() {
		return opCrearServicio;
	}

	public void setOpCrearServicio(boolean opCrearServicio) {
		this.opCrearServicio = opCrearServicio;
	}

	public boolean isOpEditarServicio() {
		return opEditarServicio;
	}

	public void setOpEditarServicio(boolean opEditarServicio) {
		this.opEditarServicio = opEditarServicio;
	}

	public boolean isOpBorrarServicio() {
		return opBorrarServicio;
	}

	public void setOpBorrarServicio(boolean opBorrarServicio) {
		this.opBorrarServicio = opBorrarServicio;
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

	public FinderModel getConceptoFinder() {
		return conceptoFinder;
	}

	public void setConceptoFinder(FinderModel conceptoFinder) {
		this.conceptoFinder = conceptoFinder;
	}

	public List<Object[]> getlServicios() {
		return lServicios;
	}

	public void setlServicios(List<Object[]> lServicios) {
		this.lServicios = lServicios;
	}

}
