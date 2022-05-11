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
import com.instituto.modelo.Concepto;
import com.instituto.util.ParamsLocal;

public class ConceptoVM extends TemplateViewModel {

	private List<Concepto> lConceptos;
	private List<Concepto> lConceptosOri;
	private Concepto conceptoSelected;

	private boolean opCrearConcepto;
	private boolean opEditarConcepto;
	private boolean opBorrarConcepto;

	@Init(superclass = true)
	public void initConceptoVM() {

		cargarConceptos();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeConceptoVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearConcepto = this.operacionHabilitada(ParamsLocal.OP_CREAR_CONCEPTO);
		this.opEditarConcepto = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CONCEPTO);
		this.opBorrarConcepto = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CONCEPTO);

	}

	private void cargarConceptos() {

		this.lConceptos = this.reg.getAllObjectsByCondicionOrder(Concepto.class.getName(), null, "Conceptoid asc");
		this.lConceptosOri = this.lConceptos;
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
	@NotifyChange("lConceptos")
	public void filtrarConcepto() {

		this.lConceptos = this.filtrarLT(this.filtroColumns, this.lConceptosOri);

	}

	// fin seccion
	
	//seccion modal
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalConceptoAgregar() {

		if(!this.opCrearConcepto)
			return;

		this.editar = false;
		modalConcepto(-1);

	}

	@Command
	public void modalConcepto(@BindingParam("conceptoid") long conceptoid) {

		if (conceptoid != -1) {

			if(!this.opEditarConcepto)
				return;
			
			this.conceptoSelected = this.reg.getObjectById(Concepto.class.getName(), conceptoid);
			this.editar = true;

		} else {
			
			conceptoSelected = new Concepto();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/abm/conceptoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private boolean verificarCampos() {
		
		if (this.conceptoSelected.getConcepto() == null || this.conceptoSelected.getConcepto().length() <= 0) {
			
			return false;
			
		}
		
		if (this.conceptoSelected.getDescripcion() == null || this.conceptoSelected.getDescripcion().length() <= 0) {
			
			return false;
			
		}
		
		if (this.conceptoSelected.getImporte() == null ) {
			
			return false;
			
		}
		
		
		
		return true;
	}	

	@Command
	@NotifyChange("lConceptos")
	public void guardar() {
		
		if (!verificarCampos()) {
			return;
		}

		this.save(conceptoSelected);

		this.conceptoSelected = null;

		this.cargarConceptos();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("El Concepto fue Actualizado.");
			this.editar = false;
		}else {
			
			Notification.show("El Concepto fue agregado.");
		}
		
		

	}

	
	//fin modal
	
	@Command
	public void borrarConceptoConfirmacion(@BindingParam("concepto") final Concepto concepto) {
		
		if (!this.opBorrarConcepto)
			return;
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					borrarConcepto(concepto);
					
				}
				
			}

		};
		
		this.mensajeEliminar("El Concepto sera eliminado. \n Continuar?", event);
	}
	
	private void borrarConcepto (Concepto concepto) {
		
		this.reg.deleteObject(concepto);
		
		this.cargarConceptos();
		
		BindUtils.postNotifyChange(null,null,this,"lConceptos");
		
	}
	
	public List<Concepto> getlConceptos() {
		return lConceptos;
	}

	public void setlConceptos(List<Concepto> lConceptos) {
		this.lConceptos = lConceptos;
	}

	public Concepto getConceptoSelected() {
		return conceptoSelected;
	}

	public void setConceptoSelected(Concepto conceptoSelected) {
		this.conceptoSelected = conceptoSelected;
	}

	public boolean isOpCrearConcepto() {
		return opCrearConcepto;
	}

	public void setOpCrearConcepto(boolean opCrearConcepto) {
		this.opCrearConcepto = opCrearConcepto;
	}

	public boolean isOpEditarConcepto() {
		return opEditarConcepto;
	}

	public void setOpEditarConcepto(boolean opEditarConcepto) {
		this.opEditarConcepto = opEditarConcepto;
	}

	public boolean isOpBorrarConcepto() {
		return opBorrarConcepto;
	}

	public void setOpBorrarConcepto(boolean opBorrarConcepto) {
		this.opBorrarConcepto = opBorrarConcepto;
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
