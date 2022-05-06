package com.instituto.sistema.gestionSede;

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
import com.instituto.modelo.CursoMateria;
import com.instituto.modelo.Materia;
import com.instituto.util.ParamsLocal;

public class MateriaVM extends TemplateViewModel {

	private List<Materia> lMaterias;
	private List<Materia> lMateriasOri;
	private Materia materiaSelected;

	private boolean opCrearMateria;
	private boolean opEditarMateria;
	private boolean opBorrarMateria;

	@Init(superclass = true)
	public void initMateriaVM() {

		cargarMaterias();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeMateriaVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearMateria = this.operacionHabilitada(ParamsLocal.OP_CREAR_MATERIA);
		this.opEditarMateria = this.operacionHabilitada(ParamsLocal.OP_EDITAR_MATERIA);
		this.opBorrarMateria = this.operacionHabilitada(ParamsLocal.OP_BORRAR_MATERIA);

	}

	private void cargarMaterias() {

		this.lMaterias = this.reg.getAllObjectsByCondicionOrder(Materia.class.getName(), null, "Materiaid asc");
		this.lMateriasOri = this.lMaterias;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[2]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lMaterias")
	public void filtrarMateria() {

		this.lMaterias = this.filtrarLT(this.filtroColumns, this.lMateriasOri);

	}

	// fin seccion
	
	//seccion modal
	
	private Window modal;
	private boolean editar = false;
	private boolean editarMateria = false;

	@Command
	public void modalMateriaAgregar() {

		if(!this.opCrearMateria)
			return;

		this.editar = false;
		this.editarMateria = true;
		modalMateria(-1);

	}

	@Command
	public void modalMateria(@BindingParam("materiaid") long materiaid) {

		if (materiaid != -1) {

			if(!this.opEditarMateria)
				return;
			
			this.materiaSelected = this.reg.getObjectById(Materia.class.getName(), materiaid);			
			this.editar = true;
			
			this.editarMateria = false;
			List<CursoMateria> lCursoMateria = this.reg.getAllObjectsByCondicionOrder(CursoMateria.class.getName(), "materiaid = "+this.materiaSelected.getMateriaid(), null);
			
			if (lCursoMateria.size() <= 0) {
				
				this.editarMateria = true;
				
			}
			
		} else {
			
			materiaSelected = new Materia();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/gestionSede/materiaModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private boolean verificarCampos() {
		
		if (this.materiaSelected.getMateria() == null || this.materiaSelected.getMateria().length() <= 0) {
			
			return false;
			
		}
		
		if (this.materiaSelected.getDescripcion() == null || this.materiaSelected.getDescripcion().length() <= 0) {
			
			return false;
			
		}
		
		return true;
	}	

	@Command
	@NotifyChange("lMaterias")
	public void guardar() {
		
		if (!verificarCampos()) {
			return;
		}

		this.save(materiaSelected);

		this.materiaSelected = null;

		this.cargarMaterias();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("La Materia fue Actualizada.");
			this.editar = false;
		}else {
			
			Notification.show("La Materia fue agregada.");
		}
		
		

	}

	
	//fin modal
	
	@Command
	public void borrarMateriaConfirmacion(@BindingParam("materia") final Materia materia) {
		
		if (!this.opBorrarMateria)
			return;
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					borrarMateria(materia);
					
				}
				
			}

		};
		
		this.mensajeEliminar("La Materia sera eliminada. \n Continuar?", event);
	}
	
	private void borrarMateria (Materia materia) {
		
		this.reg.deleteObject(materia);
		
		this.cargarMaterias();
		
		BindUtils.postNotifyChange(null,null,this,"lMaterias");
		
	}
	
	

	public List<Materia> getlMaterias() {
		return lMaterias;
	}

	public void setlMaterias(List<Materia> lMaterias) {
		this.lMaterias = lMaterias;
	}

	public Materia getMateriaSelected() {
		return materiaSelected;
	}

	public void setMateriaSelected(Materia materiaSelected) {
		this.materiaSelected = materiaSelected;
	}

	public boolean isOpCrearMateria() {
		return opCrearMateria;
	}

	public void setOpCrearMateria(boolean opCrearMateria) {
		this.opCrearMateria = opCrearMateria;
	}

	public boolean isOpEditarMateria() {
		return opEditarMateria;
	}

	public void setOpEditarMateria(boolean opEditarMateria) {
		this.opEditarMateria = opEditarMateria;
	}

	public boolean isOpBorrarMateria() {
		return opBorrarMateria;
	}

	public void setOpBorrarMateria(boolean opBorrarMateria) {
		this.opBorrarMateria = opBorrarMateria;
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

	public boolean isEditarMateria() {
		return editarMateria;
	}

	public void setEditarMateria(boolean editarMateria) {
		this.editarMateria = editarMateria;
	}

}
