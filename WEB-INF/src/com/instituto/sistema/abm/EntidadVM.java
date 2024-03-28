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
import com.instituto.modelo.Entidad;
import com.instituto.util.ParamsLocal;

public class EntidadVM extends TemplateViewModel {

	private List<Entidad> lEntidades;
	private List<Entidad> lEntidadesOri;
	private Entidad entidadSelected;

	private boolean opCrearEntidad;
	private boolean opEditarEntidad;
	private boolean opBorrarEntidad;

	@Init(superclass = true)
	public void initEntidadVM() {

		cargarEntidades();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeEntidadVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearEntidad = this.operacionHabilitada(ParamsLocal.OP_CREAR_ENTIDAD);
		this.opEditarEntidad = this.operacionHabilitada(ParamsLocal.OP_EDITAR_ENTIDAD);
		this.opBorrarEntidad = this.operacionHabilitada(ParamsLocal.OP_BORRAR_ENTIDAD);

	}

	private void cargarEntidades() {

		this.lEntidades = this.reg.getAllObjectsByCondicionOrder(Entidad.class.getName(), null, "Entidadid asc");
		this.lEntidadesOri = this.lEntidades;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[1]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lEntidades")
	public void filtrarEntidad() {

		this.lEntidades = this.filtrarLT(this.filtroColumns, this.lEntidadesOri);

	}

	// fin seccion
	
	//seccion modal
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalEntidadAgregar() {

		if(!this.opCrearEntidad)
			return;

		this.editar = false;
		modalEntidad(-1);

	}

	@Command
	public void modalEntidad(@BindingParam("entidadid") long entidadid) {

		if (entidadid != -1) {

			if(!this.opEditarEntidad)
				return;
			
			this.entidadSelected = this.reg.getObjectById(Entidad.class.getName(), entidadid);
			this.editar = true;

		} else {
			
			entidadSelected = new Entidad();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/abm/entidadModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private boolean verificarCampos() {
		
		return true;
	}	

	@Command
	@NotifyChange("lEntidades")
	public void guardar() {
		
		if (!verificarCampos()) {
			return;
		}

		this.save(entidadSelected);

		this.entidadSelected = null;

		this.cargarEntidades();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("La Entidad fue Actualizada.");
			this.editar = false;
		}else {
			
			Notification.show("La Entidad fue agregada.");
		}
		
		

	}

	//fin modal
	
	@Command
	public void borrarEntidadConfirmacion(@BindingParam("entidad") final Entidad entidad) {
		
		if (!this.opBorrarEntidad)
			return;
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					borrarEntidad(entidad);
					
				}
				
			}

		};
		
		this.mensajeEliminar("La Entidad sera eliminada. \n Continuar?", event);
	}
	
	private void borrarEntidad (Entidad entidad) {
		
		this.reg.deleteObject(entidad);
		
		this.cargarEntidades();
		
		BindUtils.postNotifyChange(null,null,this,"lEntidades");
		
	}
	

	public List<Entidad> getlEntidades() {
		return lEntidades;
	}

	public void setlEntidades(List<Entidad> lEntidades) {
		this.lEntidades = lEntidades;
	}

	public Entidad getEntidadSelected() {
		return entidadSelected;
	}

	public void setEntidadSelected(Entidad entidadSelected) {
		this.entidadSelected = entidadSelected;
	}

	public boolean isOpCrearEntidad() {
		return opCrearEntidad;
	}

	public void setOpCrearEntidad(boolean opCrearEntidad) {
		this.opCrearEntidad = opCrearEntidad;
	}

	public boolean isOpEditarEntidad() {
		return opEditarEntidad;
	}

	public void setOpEditarEntidad(boolean opEditarEntidad) {
		this.opEditarEntidad = opEditarEntidad;
	}

	public boolean isOpBorrarEntidad() {
		return opBorrarEntidad;
	}

	public void setOpBorrarEntidad(boolean opBorrarEntidad) {
		this.opBorrarEntidad = opBorrarEntidad;
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
