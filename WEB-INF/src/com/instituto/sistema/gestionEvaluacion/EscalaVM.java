package com.instituto.sistema.gestionEvaluacion;

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
import com.instituto.modelo.Escala;
import com.instituto.modelo.EscalaDetalle;
import com.instituto.util.ParamsLocal;

public class EscalaVM extends TemplateViewModel {

	private List<Object[]> lEscalas;
	private List<Object[]> lEscalasOri;
	private Escala escalaSelected;

	private boolean opCrearEscala;
	private boolean opEditarEscala;
	private boolean opBorrarEscala;

	@Init(superclass = true)
	public void initEscalaVM() {

		cargarEscalas();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeEscalaVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearEscala = this.operacionHabilitada(ParamsLocal.OP_CREAR_ESCALA);
		this.opEditarEscala = this.operacionHabilitada(ParamsLocal.OP_EDITAR_ESCALA);
		this.opBorrarEscala = this.operacionHabilitada(ParamsLocal.OP_BORRAR_ESCALA);

	}

	private void cargarEscalas() {
		
		String sql = this.um.getSql("escala/listaEscala.sql");

		this.lEscalas = this.reg.sqlNativo(sql);
		this.lEscalasOri = this.lEscalas;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[5]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}
	
	@Command
	@NotifyChange("lEscala")
	public void filtrarEscala() {

		this.lEscalas = this.filtrarListaObject(this.filtroColumns, this.lEscalasOri);

	}

	// fin seccion
	
	private Window modal;
	private boolean editar = false;
	
	@Command
	public void modalEscalaAgregar() {

		if (!this.opCrearEscala)
			return;

		this.editar = false;
		modalEscala(-1);

	}


	@Command
	public void modalEscala(@BindingParam("escalaid") long escalaid) {

		if (escalaid != -1) {

			if(!this.opEditarEscala)
				return;
			
			this.escalaSelected = this.reg.getObjectById(Escala.class.getName(), escalaid);
			this.editar = true;

		} else {
			
			escalaSelected = new Escala();
			
		}

		modal = (Window) Executions.createComponents("/instituto/zul/gestionEvaluacion/escalaModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	
	
	@Command
	@NotifyChange("escalaSelected")
	public void agregarDetalle() {
		
		EscalaDetalle escalaDetalle = new EscalaDetalle();
		escalaDetalle.setEscala(this.escalaSelected);
		
		this.escalaSelected.getDetalles().add(escalaDetalle);
		
		
	}
	
	@Command
	@NotifyChange("lEscalas")
	public void guardar() {

		this.save(escalaSelected);

		this.cargarEscalas();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("La Escala fue Actualizada.");
			this.editar = false;
		}else {
			
			Notification.show("La Escala fue agregada.");
		}
		
		this.escalaSelected = null;
		
		

	}

	
	//fin modal
	
	@Command
	public void borrarEscalaConfirmacion(@BindingParam("escalaid") final long escalaid) {

		if (!this.opBorrarEscala)
			return;

		Escala s = this.reg.getObjectById(Escala.class.getName(), escalaid);

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarEscala(s);

				}

			}

		};

		this.mensajeEliminar("La Escala sera eliminada. \n Continuar?", event);
	}

	private void borrarEscala(Escala escala) {

		this.reg.deleteObject(escala);

		this.cargarEscalas();

		BindUtils.postNotifyChange(null, null, this, "lEscalas");

	}
	
	
	@Command
	@NotifyChange("escalaSelected")
	public void borrarDetalle (@BindingParam("detalle") EscalaDetalle detalle) {
		
		this.escalaSelected.getDetalles().remove(detalle);
		
	}
	
	
	

	public List<Object[]> getlEscalas() {
		return lEscalas;
	}

	public void setlEscalas(List<Object[]> lEscalas) {
		this.lEscalas = lEscalas;
	}

	public Escala getEscalaSelected() {
		return escalaSelected;
	}

	public void setEscalaSelected(Escala escalaSelected) {
		this.escalaSelected = escalaSelected;
	}

	public boolean isOpCrearEscala() {
		return opCrearEscala;
	}

	public void setOpCrearEscala(boolean opCrearEscala) {
		this.opCrearEscala = opCrearEscala;
	}

	public boolean isOpEditarEscala() {
		return opEditarEscala;
	}

	public void setOpEditarEscala(boolean opEditarEscala) {
		this.opEditarEscala = opEditarEscala;
	}

	public boolean isOpBorrarEscala() {
		return opBorrarEscala;
	}

	public void setOpBorrarEscala(boolean opBorrarEscala) {
		this.opBorrarEscala = opBorrarEscala;
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

}
