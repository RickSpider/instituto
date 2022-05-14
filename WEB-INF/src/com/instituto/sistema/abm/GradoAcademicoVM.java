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
import com.instituto.modelo.GradoAcademico;
import com.instituto.util.ParamsLocal;
import com.instituto.modelo.GradoAcademico;

public class GradoAcademicoVM extends TemplateViewModel {

	private List<GradoAcademico> lGradosAcademicos;
	private List<GradoAcademico> lGradosAcademicosOri;
	private GradoAcademico gradoAcademicoSelected;

	private boolean opCrearGradoAcademico;
	private boolean opEditarGradoAcademico;
	private boolean opBorrarGradoAcademico;

	@Init(superclass = true)
	public void initGradoAcademicoVM() {

		cargarGradosAcademicos();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeGradoAcademicoVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearGradoAcademico = this.operacionHabilitada(ParamsLocal.OP_CREAR_GRADOACADEMICO);
		this.opEditarGradoAcademico = this.operacionHabilitada(ParamsLocal.OP_EDITAR_GRADOACADEMICO);
		this.opBorrarGradoAcademico = this.operacionHabilitada(ParamsLocal.OP_BORRAR_GRADOACADEMICO);

	}

	private void cargarGradosAcademicos() {

		this.lGradosAcademicos = this.reg.getAllObjectsByCondicionOrder(GradoAcademico.class.getName(), null, "GradoAcademicoid asc");
		this.lGradosAcademicosOri = this.lGradosAcademicos;
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
	@NotifyChange("lGradosAcademicos")
	public void filtrarGradoAcademico() {

		this.lGradosAcademicos = this.filtrarLT(this.filtroColumns, this.lGradosAcademicosOri);

	}

	// fin seccion
	
	//seccion modal
	
		private Window modal;
		private boolean editar = false;

		@Command
		public void modalGradoAcademicoAgregar() {

			if(!this.opCrearGradoAcademico)
				return;

			this.editar = false;
			modalGradoAcademico(-1);

		}

		@Command
		public void modalGradoAcademico(@BindingParam("gradoacademicoid") long gradoacademicoid) {

			if (gradoacademicoid != -1) {

				if(!this.opEditarGradoAcademico)
					return;
				
				this.gradoAcademicoSelected = this.reg.getObjectById(GradoAcademico.class.getName(), gradoacademicoid);
				this.editar = true;

			} else {
				
				gradoAcademicoSelected = new GradoAcademico();


			}

			modal = (Window) Executions.createComponents("/instituto/zul/abm/gradoAcademicoModal.zul", this.mainComponent,
					null);
			Selectors.wireComponents(modal, this, false);
			modal.doModal();

		}
		
		private boolean verificarCampos() {
			
			if (this.gradoAcademicoSelected.getGradoacademico() == null || this.gradoAcademicoSelected.getGradoacademico().length() <= 0) {
				
				return false;
				
			} 
			
			if (this.gradoAcademicoSelected.getDescripcion() == null || this.gradoAcademicoSelected.getDescripcion().length() <= 0) {
				
				return false;
				
			} 
			
			return true;
		}	

		@Command
		@NotifyChange("lGradosAcademicos")
		public void guardar() {
			
			if (!verificarCampos()) {
				return;
			}

			this.save(gradoAcademicoSelected);

			this.gradoAcademicoSelected = null;

			this.cargarGradosAcademicos();

			this.modal.detach();
			
			if (editar) {
				
				Notification.show("La GradoAcademico fue Actualizada.");
				this.editar = false;
			}else {
				
				Notification.show("La GradoAcademico fue agregada.");
			}
			
			

		}

		
		//fin modal
		
		@Command
		public void borrarGradoAcademicoConfirmacion(@BindingParam("gradoAcademico") final GradoAcademico gradoAcademico) {
			
			if (!this.opBorrarGradoAcademico)
				return;
			
			EventListener event = new EventListener () {

				@Override
				public void onEvent(Event evt) throws Exception {
					
					if (evt.getName().equals(Messagebox.ON_YES)) {
						
						borrarGradoAcademico(gradoAcademico);
						
					}
					
				}

			};
			
			this.mensajeEliminar("La GradoAcademico sera eliminada. \n Continuar?", event);
		}
		
		private void borrarGradoAcademico (GradoAcademico gradoAcademico) {
			
			this.reg.deleteObject(gradoAcademico);
			
			this.cargarGradosAcademicos();
			
			BindUtils.postNotifyChange(null,null,this,"lGradosAcademicos");
			
		}

		public List<GradoAcademico> getlGradosAcademicos() {
			return lGradosAcademicos;
		}

		public void setlGradosAcademicos(List<GradoAcademico> lGradosAcademicos) {
			this.lGradosAcademicos = lGradosAcademicos;
		}

		public boolean isOpCrearGradoAcademico() {
			return opCrearGradoAcademico;
		}

		public void setOpCrearGradoAcademico(boolean opCrearGradoAcademico) {
			this.opCrearGradoAcademico = opCrearGradoAcademico;
		}

		public boolean isOpEditarGradoAcademico() {
			return opEditarGradoAcademico;
		}

		public void setOpEditarGradoAcademico(boolean opEditarGradoAcademico) {
			this.opEditarGradoAcademico = opEditarGradoAcademico;
		}

		public boolean isOpBorrarGradoAcademico() {
			return opBorrarGradoAcademico;
		}

		public void setOpBorrarGradoAcademico(boolean opBorrarGradoAcademico) {
			this.opBorrarGradoAcademico = opBorrarGradoAcademico;
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

		public GradoAcademico getGradoAcademicoSelected() {
			return gradoAcademicoSelected;
		}

		public void setGradoAcademicoSelected(GradoAcademico gradoAcademicoSelected) {
			this.gradoAcademicoSelected = gradoAcademicoSelected;
		}

}
