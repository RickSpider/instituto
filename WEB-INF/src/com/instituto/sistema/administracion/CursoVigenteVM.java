package com.instituto.sistema.administracion;

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
import com.instituto.modelo.CursoVigente;
import com.instituto.util.ParamsLocal;

public class CursoVigenteVM extends TemplateViewModel {

	private List<CursoVigente> lCursosVigentes;
	private List<CursoVigente> lCursosVigentesOri;
	private CursoVigente cursoVigenteSelected;

	private boolean opCrearCursoVigente;
	private boolean opEditarCursoVigente;
	private boolean opBorrarCursoVigente;

	@Init(superclass = true)
	public void initCursoVigenteVM() {

		cargarCursosVigentes();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeCursoVigenteVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearCursoVigente = this.operacionHabilitada(ParamsLocal.OP_CREAR_CURSOVIGENTE);
		this.opEditarCursoVigente = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CURSOVIGENTE);
		this.opBorrarCursoVigente = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CURSOVIGENTE);

	}

	private void cargarCursosVigentes() {

		this.lCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigente.class.getName(), null, "CursoVigenteid asc");
		this.lCursosVigentesOri = this.lCursosVigentes;
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
	@NotifyChange("lCursosVigentes")
	public void filtrarCursoVigente() {

		this.lCursosVigentes = this.filtrarLT(this.filtroColumns, this.lCursosVigentesOri);

	}

	// fin seccion
	
	//seccion modal
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalCursoVigenteAgregar() {

		if(!this.opCrearCursoVigente)
			return;

		this.editar = false;
		modalCursoVigente(-1);

	}

	@Command
	public void modalCursoVigente(@BindingParam("cursoVigenteid") long cursoVigenteid) {

		if (cursoVigenteid != -1) {

			if(!this.opEditarCursoVigente)
				return;
			
			this.cursoVigenteSelected = this.reg.getObjectById(CursoVigente.class.getName(), cursoVigenteid);
			this.editar = true;

		} else {
			
			cursoVigenteSelected = new CursoVigente();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/gestionCursoVigente/cursoVigenteModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private boolean verificarCampos() {
		
		
		return true;
	}	

	@Command
	@NotifyChange("lCursosVigentes")
	public void guardar() {
		
		if (!verificarCampos()) {
			return;
		}

		this.save(cursoVigenteSelected);

		this.cursoVigenteSelected = null;

		this.cargarCursosVigentes();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("La CursoVigente fue Actualizada.");
			this.editar = false;
		}else {
			
			Notification.show("La CursoVigente fue agregada.");
		}
		
		

	}

	
	//fin modal
	
	@Command
	public void borrarCursoVigenteConfirmacion(@BindingParam("cursoVigente") final CursoVigente cursoVigente) {
		
		if (!this.opBorrarCursoVigente)
			return;
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					borrarCursoVigente(cursoVigente);
					
				}
				
			}

		};
		
		this.mensajeEliminar("La CursoVigente sera eliminada. \n Continuar?", event);
	}
	
	private void borrarCursoVigente (CursoVigente cursoVigente) {
		
		this.reg.deleteObject(cursoVigente);
		
		this.cargarCursosVigentes();
		
		BindUtils.postNotifyChange(null,null,this,"lCursosVigentes");
		
	}
	

	public List<CursoVigente> getlCursosVigentes() {
		return lCursosVigentes;
	}

	public void setlCursosVigentes(List<CursoVigente> lCursosVigentes) {
		this.lCursosVigentes = lCursosVigentes;
	}

	public CursoVigente getCursoVigenteSelected() {
		return cursoVigenteSelected;
	}

	public void setCursoVigenteSelected(CursoVigente cursoVigenteSelected) {
		this.cursoVigenteSelected = cursoVigenteSelected;
	}

	public boolean isOpCrearCursoVigente() {
		return opCrearCursoVigente;
	}

	public void setOpCrearCursoVigente(boolean opCrearCursoVigente) {
		this.opCrearCursoVigente = opCrearCursoVigente;
	}

	public boolean isOpEditarCursoVigente() {
		return opEditarCursoVigente;
	}

	public void setOpEditarCursoVigente(boolean opEditarCursoVigente) {
		this.opEditarCursoVigente = opEditarCursoVigente;
	}

	public boolean isOpBorrarCursoVigente() {
		return opBorrarCursoVigente;
	}

	public void setOpBorrarCursoVigente(boolean opBorrarCursoVigente) {
		this.opBorrarCursoVigente = opBorrarCursoVigente;
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
