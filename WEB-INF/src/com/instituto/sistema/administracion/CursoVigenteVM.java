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
import com.instituto.modelo.Alumno;
import com.instituto.modelo.Curso;
import com.instituto.modelo.CursoVigente;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CursoVigenteVM extends TemplateViewModelLocal {

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

		this.lCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigente.class.getName(), "sedeid = "+this.getCurrentSede().getSedeid(), "CursoVigenteid asc");
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
			this.buscarCurso = this.cursoVigenteSelected.getCurso().getCurso();
			this.editar = true;

		} else {
			
			cursoVigenteSelected = new CursoVigente();
			cursoVigenteSelected.setSede(this.getCurrentSede());
			this.buscarCurso = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cursoVigenteModal.zul", this.mainComponent,
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
			
			Notification.show("El Curso Vigente fue Actualizado.");
			this.editar = false;
		}else {
			
			Notification.show("El Curso Vigente fue agregado.");
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
		
		this.mensajeEliminar("El Curso Vigente sera eliminado. \n Continuar?", event);
	}
	
	private void borrarCursoVigente (CursoVigente cursoVigente) {
		
		this.reg.deleteObject(cursoVigente);
		
		this.cargarCursosVigentes();
		
		BindUtils.postNotifyChange(null,null,this,"lCursosVigentes");
		
	}
	
	// buscador de Curso

				private List<Object[]> lCursosBuscarOri = null;
				private List<Object[]> lCursosBuscar = null;
				private Curso buscarSelectedCurso = null;
				private String buscarCurso = "";

				@Command
				@NotifyChange("lCursosBuscar")
				public void filtrarCursoBuscar() {

					this.lCursosBuscar = this.filtrarListaObject(buscarCurso, this.lCursosBuscarOri);

				}
				
				@Command
				@NotifyChange("lCursosBuscar")
				public void generarListaBuscarCurso() {

					String sql = this.um.getSql("buscarCurso.sql");
					
					this.lCursosBuscar = this.reg.sqlNativo(sql);
					
					this.lCursosBuscarOri = this.lCursosBuscar;
				}
				
				@Command
				@NotifyChange("buscarCurso")
				public void onSelectCurso(@BindingParam("id") long id) {
					
					this.buscarSelectedCurso = this.reg.getObjectById(Curso.class.getName(), id);	
					this.buscarCurso = this.buscarSelectedCurso.getCurso();
					this.cursoVigenteSelected.setCurso(buscarSelectedCurso);

				}
				
				// fin buscar ciudad
				
	

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

	public List<Object[]> getlCursosBuscar() {
		return lCursosBuscar;
	}

	public void setlCursosBuscar(List<Object[]> lCursosBuscar) {
		this.lCursosBuscar = lCursosBuscar;
	}

	public String getBuscarCurso() {
		return buscarCurso;
	}

	public void setBuscarCurso(String buscarCurso) {
		this.buscarCurso = buscarCurso;
	}


}
