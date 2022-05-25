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
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.Curso;
import com.instituto.modelo.CursoVigente;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CursoVigenteVM extends TemplateViewModelLocal {

	private List<CursoVigente> lCursosVigentes;
	private List<CursoVigente> lCursosVigentesOri;
	private CursoVigente cursoVigenteSelected;
	private CursoVigente cursoVigenteSelectedAlumno;
	private List<CursoVigenteAlumno> lAlumnosCursosVigentes;

	private boolean[] bDias = new boolean[7];

	private boolean opCrearCursoVigente;
	private boolean opEditarCursoVigente;
	private boolean opBorrarCursoVigente;
	private boolean opAgregarCursoVigenteAlumno;
	private boolean opQuitarCursoVigenteAlumno;

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
		
		this.opAgregarCursoVigenteAlumno = this.operacionHabilitada(ParamsLocal.OP_AGREGAR_CURSOVIGENTE_ALUMNO);
		this.opQuitarCursoVigenteAlumno = this.operacionHabilitada(ParamsLocal.OP_QUITAR_CURSOVIGENTE_ALUMNO);

	}

	private void cargarCursosVigentes() {

		this.lCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigente.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid(), "CursoVigenteid asc");
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

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalCursoVigenteAgregar() {

		if (!this.opCrearCursoVigente)
			return;

		this.editar = false;
		modalCursoVigente(-1);

	}

	@Command
	public void modalCursoVigente(@BindingParam("cursovigenteid") long cursovigenteid) {

		if (cursovigenteid != -1) {

			if (!this.opEditarCursoVigente)
				return;

			this.cursoVigenteSelected = this.reg.getObjectById(CursoVigente.class.getName(), cursovigenteid);
			this.buscarCurso = this.cursoVigenteSelected.getCurso().getCurso();
			this.editar = true;

		} else {

			cursoVigenteSelected = new CursoVigente();
			cursoVigenteSelected.setSede(this.getCurrentSede());
			this.buscarCurso = "";

		}

		String[] sDias = this.cursoVigenteSelected.getDias().split(";");

		for (int i = 0; i < sDias.length; i++) {

			bDias[i] = new Boolean(sDias[i]);

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cursoVigenteModal.zul",
				this.mainComponent, null);
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

		String dias = "";

		for (int i = 0; i < bDias.length; i++) {

			dias += String.valueOf(bDias[i]);

			if ((i + 1) != bDias.length) {

				dias += ";";

			}

		}

		this.cursoVigenteSelected.setDias(dias);

		this.save(cursoVigenteSelected);

		this.cursoVigenteSelected = null;

		this.cargarCursosVigentes();

		this.modal.detach();

		if (editar) {

			Notification.show("El Curso Vigente fue Actualizado.");
			this.editar = false;
		} else {

			Notification.show("El Curso Vigente fue agregado.");
		}

	}

	// fin modal

	@Command
	public void borrarCursoVigenteConfirmacion(@BindingParam("cursoVigente") final CursoVigente cursoVigente) {

		if (!this.opBorrarCursoVigente)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarCursoVigente(cursoVigente);

				}

			}

		};

		this.mensajeEliminar("El Curso Vigente sera eliminado. \n Continuar?", event);
	}

	private void borrarCursoVigente(CursoVigente cursoVigente) {

		this.reg.deleteObject(cursoVigente);

		this.cargarCursosVigentes();

		BindUtils.postNotifyChange(null, null, this, "lCursosVigentes");

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
	
	// Seccion alumnos cursoVigente

		@Command
		@NotifyChange({ "lAlumnosCursosVigentes", "buscarAlumno" })
		public void refrescarAlumnos(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

			this.cursoVigenteSelectedAlumno = cursoVigente;
			this.lAlumnosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteAlumno.class.getName(),
					"cursoVigenteid = " + cursoVigente.getCursovigenteid(), "alumnoid asc");

			this.buscarSelectedAlumno = null;
			this.buscarAlumno = "";

		}

		@Command
		public void borrarAlumnoConfirmacion(@BindingParam("cursoVigentealumno") final CursoVigenteAlumno ca) {

			if (!this.opQuitarCursoVigenteAlumno) {

				this.mensajeError("No tienes permisos para Borrar Alumnos a un CursoVigente.");

				return;

			}

			this.mensajeEliminar("El Alumno " + ca.getAlumno().getFullNombre() + " sera removido del Curso "
					+ ca.getCursoVigente().getCurso().getCurso() + " \n Continuar?", new EventListener() {

						@Override
						public void onEvent(Event evt) throws Exception {

							if (evt.getName().equals(Messagebox.ON_YES)) {

								borrarCursoVigenteAlumno(ca);

							}

						}

					});

		}

		private void borrarCursoVigenteAlumno(CursoVigenteAlumno ca) {

			this.reg.deleteObject(ca);

			this.refrescarAlumnos(this.cursoVigenteSelectedAlumno);

			BindUtils.postNotifyChange(null, null, this, "lAlumnosCursosVigentes");

		}

		// fin alumnos cursoVigente

		// seccion buscador

		private List<Object[]> lAlumnosbuscarOri;
		private List<Object[]> lAlumnosBuscar;
		private Alumno buscarSelectedAlumno;
		private String buscarAlumno = "";

		@Command
		@NotifyChange("lAlumnosBuscar")
		public void filtrarAlumnoBuscar() {

			this.lAlumnosBuscar = this.filtrarListaObject(buscarAlumno, this.lAlumnosbuscarOri);

		}

		@Command
		@NotifyChange("lAlumnosBuscar")
		public void generarListaBuscarAlumno() {

			if (this.cursoVigenteSelectedAlumno == null) {

				this.mensajeInfo("Selecciona un CursoVigente.");

				return;
			}

			String sqlBuscarAlumno = this.um.getSql("buscarAlumno.sql");

			this.lAlumnosBuscar = this.reg.sqlNativo(sqlBuscarAlumno);
			this.lAlumnosbuscarOri = this.lAlumnosBuscar;
		}

		@Command
		@NotifyChange("buscarAlumno")
		public void onSelectAlumno(@BindingParam("id") long id) {

			this.buscarSelectedAlumno = this.reg.getObjectById(Alumno.class.getName(), id);
			this.buscarAlumno = buscarSelectedAlumno.getFullNombre();

		}

		@Command
		@NotifyChange({ "lAlumnosCursosVigentes", "buscarAlumno" })
		public void agregarAlumno() {

			if (!this.opAgregarCursoVigenteAlumno) {

				this.mensajeError("No tienes permiso para agregar un Alumno al CursoVigente. ");
				return;
			}

			if (this.buscarSelectedAlumno == null) {

				this.mensajeInfo("Selecciona un Alumno para agregar.");

				return;

			}

			for (CursoVigenteAlumno x : this.lAlumnosCursosVigentes) {

				if (this.buscarSelectedAlumno.getAlumnoid() == x.getAlumno().getAlumnoid()) {

					this.mensajeError("El CursoVigente ya tiene el alumno " + x.getAlumno().getFullNombre());

					return;

				}

			}

			CursoVigenteAlumno cu = new CursoVigenteAlumno();
			cu.setAlumno(this.buscarSelectedAlumno);
			cu.setCursoVigente(this.cursoVigenteSelectedAlumno);
			this.save(cu);

			this.refrescarAlumnos(this.cursoVigenteSelectedAlumno);

		}

		// fins buscador

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

	public boolean[] getbDias() {
		return bDias;
	}

	public void setbDias(boolean[] bDias) {
		this.bDias = bDias;
	}

	public List<CursoVigenteAlumno> getlAlumnosCursosVigentes() {
		return lAlumnosCursosVigentes;
	}

	public void setlAlumnosCursosVigentes(List<CursoVigenteAlumno> lAlumnosCursosVigentes) {
		this.lAlumnosCursosVigentes = lAlumnosCursosVigentes;
	}

	public boolean isOpAgregarCursoVigenteAlumno() {
		return opAgregarCursoVigenteAlumno;
	}

	public void setOpAgregarCursoVigenteAlumno(boolean opAgregarCursoVigenteAlumno) {
		this.opAgregarCursoVigenteAlumno = opAgregarCursoVigenteAlumno;
	}

	public boolean isOpQuitarCursoVigenteAlumno() {
		return opQuitarCursoVigenteAlumno;
	}

	public void setOpQuitarCursoVigenteAlumno(boolean opQuitarCursoVigenteAlumno) {
		this.opQuitarCursoVigenteAlumno = opQuitarCursoVigenteAlumno;
	}

	public List<Object[]> getlAlumnosBuscar() {
		return lAlumnosBuscar;
	}

	public void setlAlumnosBuscar(List<Object[]> lAlumnosBuscar) {
		this.lAlumnosBuscar = lAlumnosBuscar;
	}

	public String getBuscarAlumno() {
		return buscarAlumno;
	}

	public void setBuscarAlumno(String buscarAlumno) {
		this.buscarAlumno = buscarAlumno;
	}

}
