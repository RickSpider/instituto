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

import com.instituto.util.ParamsLocal;
import com.instituto.modelo.Concepto;
import com.instituto.modelo.Curso;
import com.instituto.modelo.CursoConcepto;
import com.instituto.modelo.CursoMateria;
import com.instituto.modelo.Materia;


public class CursoVM extends TemplateViewModel {

	private List<Curso> lCursos;
	private List<Curso> lCursosOri;
	private List<CursoMateria> lMateriasCursos;
	private List<CursoConcepto> lConceptosCurso;
	private Curso cursoSelected;
	private Curso cursoSelectedMateriaConcepto;
	private String filtroColumnsCurso[];
	
	private boolean opCrearCurso;
	private boolean opEditarCurso;
	private boolean opBorrarCurso;
	private boolean opAgregarMateria;
	private boolean opQuitarMateria;

	@Init(superclass = true)
	public void initCursoVM() {
		
		cargarCursos();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeCursoVM() {
		
	}
	
	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearCurso = this.operacionHabilitada(ParamsLocal.OP_CREAR_CURSO);
		this.opEditarCurso = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CURSO);
		this.opBorrarCurso = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CURSO);
		this.opAgregarMateria = this.operacionHabilitada(ParamsLocal.OP_AGREGAR_MATERIA);
		this.opQuitarMateria = this.operacionHabilitada(ParamsLocal.OP_QUITAR_MATERIA);
		
	}

	private void cargarCursos() {
 
		this.lCursos = this.reg.getAllObjectsByCondicionOrder(Curso.class.getName(), null, "cursoid asc");
		this.lCursosOri = this.lCursos;

	}
	
	private void inicializarFiltros(){
		
		this.filtroColumnsCurso = new String[3]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el modelo
		
		for (int i = 0 ; i<this.filtroColumnsCurso.length; i++) {
			
			this.filtroColumnsCurso[i] = "";
			
		}
		
	}

	@Command
	@NotifyChange("lCursos")
	public void filtrarCurso() {

		lCursos = this.filtrarLT(this.filtroColumnsCurso, this.lCursosOri);

	}

	// Seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalCursoAgregar() {
		
		if(!this.opCrearCurso)
			return;

		this.editar = false;
		modalCurso(-1);

	}
	
	@Command
	public void modalCurso(@BindingParam("cursoid") long cursoid) {

		if (cursoid != -1) {
			
			if(!this.opEditarCurso)
				return;

			this.cursoSelected = this.reg.getObjectById(Curso.class.getName(), cursoid);
			this.editar = true;

		} else {

			this.cursoSelected = new Curso();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/gestionSede/cursoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private boolean verificarCampos() {
	
		if (this.cursoSelected.getCurso() == null || this.cursoSelected.getCurso().length() <= 0) {
			
			return false;
			
		}
		
		if (this.cursoSelected.getDescripcion() == null || this.cursoSelected.getDescripcion().length() <= 0) {
			
			return false;
			
		}
		
		if (this.cursoSelected.getDuracion() <= 0) {
			
			return false;
			
		}
		
		return true;
	}	


	@Command
	@NotifyChange("lCursos")
	public void guardar() {
		
		if (!verificarCampos()) {
			
			return;
			
		}
		
		this.save(cursoSelected);

		this.cursoSelected = null;

		this.cargarCursos();

		this.modal.detach();

		if (editar) {

			Notification.show("El Curso fue Actualizado.");
			this.editar = false;

		} else {

			Notification.show("El Curso fue agregado.");
		}

	}

	// Fin Seccion Modal

	@Command
	public void borrarCursoConfirmacion(@BindingParam("curso") final Curso curso) {
		
		if(!this.opBorrarCurso)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarCurso(curso);

				}

			}

		};

		this.mensajeEliminar("El curso sera eliminado. \n Continuar?", event);
	}

	private void borrarCurso(Curso curso) {

		this.reg.deleteObject(curso);

		this.cargarCursos();

		BindUtils.postNotifyChange(null, null, this, "lCursos");

	}
	
	@Command
	@NotifyChange({ "lMateriasCursos", "buscarMateria", "lConceptosCurso", "buscarConcepto" })
	public void refrescarAll(@BindingParam("curso") Curso curso) {
		
		this.refrescarMaterias(curso);
		//this.refrescarConceptos(curso);
		
	}

	// Seccion materias curso

	@Command
	@NotifyChange({"lMateriasCursos","buscarMateria"})
	public void refrescarMaterias(@BindingParam("curso") Curso curso) {

		this.cursoSelectedMateriaConcepto = curso;
		this.lMateriasCursos = this.reg.getAllObjectsByCondicionOrder(CursoMateria.class.getName(),
				"cursoid = " + curso.getCursoid(), "materiaid asc");
		
		this.buscarSelectedMateria = null;
		this.buscarMateria="";

	}

	@Command
	public void borrarMateriaConfirmacion(@BindingParam("cursomateria") final CursoMateria ru) {
		
		if (!this.opQuitarMateria) {
			
			this.mensajeError("No tienes permisos para Borrar Materias a un Curso.");
			
			return;
			
		}
		
		this.mensajeEliminar("La Materia "+ru.getMateria().getMateria()+" sera removida del Curso "+ru.getCurso().getCurso()+" \n Continuar?",  
				new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarCursoMateria(ru);

				}

			}

		});

	}
	
	private void borrarCursoMateria(CursoMateria ru) {
		
		this.reg.deleteObject(ru);

		this.refrescarMaterias(this.cursoSelectedMateriaConcepto);

		BindUtils.postNotifyChange(null, null, this, "lMateriasCursos");
		
	}

	// fin materias curso

	// seccion buscador

	private List<Object[]> lMateriasbuscarOri;
	private List<Object[]> lMateriasBuscar;
	private Materia buscarSelectedMateria;
	private String buscarMateria = "";

	@Command
	@NotifyChange("lMateriasBuscar")
	public void filtrarMateriaBuscar() {

		this.lMateriasBuscar = this.filtrarListaObject(buscarMateria, this.lMateriasbuscarOri);

	}

	@Command
	@NotifyChange("lMateriasBuscar")
	public void generarListaBuscarMateria() {

		if (this.cursoSelectedMateriaConcepto == null) {

			this.mensajeInfo("Selecciona un Curso.");

			return;
		}
		
		String sqlBuscarMateria = this.um.getSql("buscarMateriaNotCurso.sql").replace("?1", cursoSelectedMateriaConcepto.getCursoid()+"");

		this.lMateriasBuscar = this.reg.sqlNativo(sqlBuscarMateria);
		this.lMateriasbuscarOri = this.lMateriasBuscar;
	}
	
	@Command
	@NotifyChange("buscarMateria")
	public void onSelectMateria(@BindingParam("id") long id) {
		
		this.buscarSelectedMateria = this.reg.getObjectById(Materia.class.getName(), id);
		this.buscarMateria = this.buscarSelectedMateria.getMateria();
		
	}

	@Command
	@NotifyChange({"lMateriasCursos","buscarMateria"})
	public void agregarMateria() {
		
		if (!this.opAgregarMateria) {
			
			this.mensajeError("No tienes permiso para agregar un Materia al Curso. ");
			return;
		}
		
		if (this.buscarSelectedMateria == null) {
			
			this.mensajeInfo("Selecciona un Materia para agregar.");
			
			return;
			
		}
		
		for(CursoMateria x : this.lMateriasCursos) {
			
			if (this.buscarSelectedMateria.getMateriaid() == x.getMateria().getMateriaid()) {
				
				this.mensajeError("El Curso ya tiene el materia "+x.getMateria().getMateria());
				
				return;
				
			}
			
		}
		
		CursoMateria ur = new CursoMateria();
		ur.setMateria(this.buscarSelectedMateria);
		ur.setCurso(this.cursoSelectedMateriaConcepto);
		this.save(ur);

		this.refrescarMaterias(this.cursoSelectedMateriaConcepto);

	}

	// fins buscador

	
	public List<Curso> getlCursos() {
		return lCursos;
	}

	public void setlCursos(List<Curso> lCursos) {
		this.lCursos = lCursos;
	}

	public Curso getCursoSelected() {
		return cursoSelected;
	}

	public void setCursoSelected(Curso cursoSelected) {
		this.cursoSelected = cursoSelected;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public List<CursoMateria> getlMateriasCursos() {
		return lMateriasCursos;
	}

	public void setlMateriasCursos(List<CursoMateria> lMateriasCursos) {
		this.lMateriasCursos = lMateriasCursos;
	}

	public List<Object[]> getlMateriasBuscar() {
		return lMateriasBuscar;
	}

	public void setlMateriasBuscar(List<Object[]> lMateriasBuscar) {
		this.lMateriasBuscar = lMateriasBuscar;
	}

	public String getBuscarMateria() {
		return buscarMateria;
	}

	public void setBuscarMateria(String buscarMateria) {
		this.buscarMateria = buscarMateria;
	}

	public String[] getFiltroColumnsCurso() {
		return filtroColumnsCurso;
	}

	public void setFiltroColumnsCurso(String[] filtroColumnsCurso) {
		this.filtroColumnsCurso = filtroColumnsCurso;
	}

	public boolean isOpCrearCurso() {
		return opCrearCurso;
	}

	public void setOpCrearCurso(boolean opCrearCurso) {
		this.opCrearCurso = opCrearCurso;
	}

	public boolean isOpEditarCurso() {
		return opEditarCurso;
	}

	public void setOpEditarCurso(boolean opEditarCurso) {
		this.opEditarCurso = opEditarCurso;
	}

	public boolean isOpBorrarCurso() {
		return opBorrarCurso;
	}

	public void setOpBorrarCurso(boolean opBorrarCurso) {
		this.opBorrarCurso = opBorrarCurso;
	}

	public boolean isOpAgregarMateria() {
		return opAgregarMateria;
	}

	public void setOpAgregarMateria(boolean opAgregarMateria) {
		this.opAgregarMateria = opAgregarMateria;
	}

	public boolean isOpQuitarMateria() {
		return opQuitarMateria;
	}

	public void setOpQuitarMateria(boolean opQuitarMateria) {
		this.opQuitarMateria = opQuitarMateria;
	}

	public List<CursoConcepto> getlConceptosCurso() {
		return lConceptosCurso;
	}

	public void setlConceptosCurso(List<CursoConcepto> lConceptosCurso) {
		this.lConceptosCurso = lConceptosCurso;
	}

}
