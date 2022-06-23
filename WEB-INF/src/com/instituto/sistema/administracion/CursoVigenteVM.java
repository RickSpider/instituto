package com.instituto.sistema.administracion;

import java.text.SimpleDateFormat;
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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.TemplateViewModel;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.Concepto;
import com.instituto.modelo.Convenio;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.CursoVigenteConcepto;
import com.instituto.modelo.CursoVigenteMateria;
import com.instituto.modelo.Materia;
import com.instituto.modelo.Curso;
import com.instituto.modelo.CursoVigente;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CursoVigenteVM extends TemplateViewModelLocal {

	private List<CursoVigente> lCursosVigentes;
	private List<CursoVigente> lCursosVigentesOri;
	private CursoVigente cursoVigenteSelected;
	private CursoVigente cursoVigenteSelectedAlumnoConceptoMateria;
	private List<CursoVigenteAlumno> lAlumnosCursosVigentes;
	private List<CursoVigenteConcepto> lConceptosCursosVigentes;
	private List<CursoVigenteMateria> lMateriasCursosVigentes;
	private CursoVigenteConcepto cursoVigenteConceptoSelected;
	private CursoVigenteMateria cursoVigenteMateriaSelected;

	private boolean[] bDias = new boolean[7];

	private boolean opCrearCursoVigente;
	private boolean opEditarCursoVigente;
	private boolean opBorrarCursoVigente;
	private boolean opAgregarCursoVigenteAlumno;
	private boolean opQuitarCursoVigenteAlumno;

	private boolean opAgregarCursoVigenteConcepto;
	private boolean opQuitarCursoVigenteConcepto;
	private boolean opEditarCursoVigenteConcepto;
	
	private boolean opAgregarCursoVigenteMateria;
	private boolean opQuitarCursoVigenteMateria;
	private boolean opEditarCursoVigenteMateria;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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

		this.opAgregarCursoVigenteConcepto = this.operacionHabilitada(ParamsLocal.OP_AGREGAR_CURSOVIGENTE_CONCEPTO);
		this.opQuitarCursoVigenteConcepto = this.operacionHabilitada(ParamsLocal.OP_QUITAR_CURSOVIGENTE_CONCEPTO);
		this.opEditarCursoVigenteConcepto = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CURSOVIGENTE_CONCEPTO);
		
		this.opAgregarCursoVigenteMateria = this.operacionHabilitada(ParamsLocal.OP_AGREGAR_CURSOVIGENTE_MATERIA);
		this.opQuitarCursoVigenteMateria = this.operacionHabilitada(ParamsLocal.OP_QUITAR_CURSOVIGENTE_MATERIA);
		this.opEditarCursoVigenteMateria = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CURSOVIGENTE_MATERIA);

	}

	private void cargarCursosVigentes() {

		this.lCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigente.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid(), "CursoVigenteid asc");
		this.lCursosVigentesOri = this.lCursosVigentes;
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

		if (this.cursoVigenteSelected.getCurso() == null) {

			this.mensajeError("No hay curso agregado.");

			return false;

		}

		if (this.cursoVigenteSelected.getFechaInicio() == null) {

			this.mensajeError("No se definio fecha de inicio.");

			return false;

		}

		if (this.cursoVigenteSelected.getFechaFin() == null) {

			this.mensajeError("No se definio fecha de Fin.");

			return false;

		}

		if (this.cursoVigenteSelected.getFechaFin().equals(this.cursoVigenteSelected.getFechaInicio())) {

			this.mensajeError("Las fechas de inicio y fin no pueden ser iguales.");

			return false;

		}

		if (this.cursoVigenteSelected.getDias().compareTo("false;false;false;false;false;false;false") == 0) {

			this.mensajeError("Debes Seleccionar al menos un dia.");

			return false;

		}

		return true;
	}

	@Command
	@NotifyChange("lCursosVigentes")
	public void guardar() {

		String dias = "";

		for (int i = 0; i < bDias.length; i++) {

			dias += String.valueOf(bDias[i]);

			if ((i + 1) != bDias.length) {

				dias += ";";

			}

		}

		this.cursoVigenteSelected.setDias(dias);

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

	@Command
	@NotifyChange({ "lAlumnosCursosVigentes", "buscarAlumno", "lConceptosCursosVigentes", "buscarConcepto", "lMateriasCursosVigentes","buscarMateria" })
	public void refrescarAll(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.refrescarAlumnos(cursoVigente);
		this.refrescarConceptos(cursoVigente);
		this.refrescarMaterias(cursoVigente);

	}

	// Seccion alumnos cursoVigente

	@Command
	@NotifyChange({ "lAlumnosCursosVigentes", "buscarAlumno" })
	public void refrescarAlumnos(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.cursoVigenteSelectedAlumnoConceptoMateria = cursoVigente;
		this.lAlumnosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteAlumno.class.getName(),
				"cursoVigenteid = " + cursoVigente.getCursovigenteid(), "creado asc");

		this.buscarSelectedAlumno = null;
		this.buscarAlumno = "";

	}

	@Command
	public void borrarAlumnoConfirmacion(@BindingParam("cursoVigenteAlumno") final CursoVigenteAlumno ca) {

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

		this.refrescarAlumnos(this.cursoVigenteSelectedAlumnoConceptoMateria);

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

		if (this.cursoVigenteSelectedAlumnoConceptoMateria == null) {

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
		cu.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateria);
		this.save(cu);
		
		this.lastPageListBox((Listbox) this.mainComponent.query("#lbAlumnos"));
		
		this.refrescarAlumnos(this.cursoVigenteSelectedAlumnoConceptoMateria);

	}

	

	// fins buscador

	// Seccion Conceptos cursoVigente

	@Command
	public void modalCursoVigenteConceptoAgregar() {

		if (!this.opAgregarCursoVigenteConcepto)
			return;

		if (this.cursoVigenteSelectedAlumnoConceptoMateria == null) {

			this.mensajeInfo("Seleccione un Curso Vigente.");
			return;
		}

		this.editar = false;
		modalCursoVigenteConcepto(null);

	}

	@Command
	public void modalCursoVigenteConcepto(
			@BindingParam("cursovigenteconcepto") CursoVigenteConcepto cursoVigenteConcepto) {

		if (cursoVigenteConcepto != null) {

			if (!this.opEditarCursoVigenteConcepto)
				return;

			this.cursoVigenteConceptoSelected = cursoVigenteConcepto;
			this.buscarConcepto = this.cursoVigenteConceptoSelected.getConcepto().getConcepto();
			this.editar = true;

		} else {

			this.cursoVigenteConceptoSelected = new CursoVigenteConcepto();
			this.cursoVigenteConceptoSelected.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateria);
			this.buscarConcepto = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cursoVigenteConceptoModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	public boolean verificarCamposConcepto() {

		return true;
	}

	@Command
	@NotifyChange({ "lConceptosCursosVigentes" })
	public void guardarConcepto() {

		if (!verificarCamposConcepto()) {
			return;
		}

		this.save(cursoVigenteConceptoSelected);

		this.cursoVigenteConceptoSelected = null;

		this.modal.detach();

		if (editar) {

			Notification.show("El Concepto fue Actualizado.");
			this.editar = false;
		} else {

			Notification.show("El Concepto fue agregado.");
		}

		this.refrescarConceptos(this.cursoVigenteSelectedAlumnoConceptoMateria);

	}

	@Command
	@NotifyChange({ "lConceptosCursosVigentes", "buscarConcepto" })
	public void refrescarConceptos(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.cursoVigenteSelectedAlumnoConceptoMateria = cursoVigente;
		this.lConceptosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteConcepto.class.getName(),
				"cursoVigenteid = " + cursoVigente.getCursovigenteid(), "conceptoid asc");

		/*
		 * .buscarSelectedConcepto = null; this.buscarConcepto = "";
		 */

	}

	@Command
	public void borrarConceptoConfirmacion(@BindingParam("cursoVigenteConcepto") final CursoVigenteConcepto ca) {

		if (!this.opQuitarCursoVigenteConcepto) {

			this.mensajeError("No tienes permisos para Borrar Conceptos a un CursoVigente.");

			return;

		}

		this.mensajeEliminar("El Concepto " + ca.getConcepto().getConcepto() + " sera removido del Curso Vigente"
				+ ca.getCursoVigente().getCurso().getCurso() + " \n Continuar?", new EventListener() {

					@Override
					public void onEvent(Event evt) throws Exception {

						if (evt.getName().equals(Messagebox.ON_YES)) {

							borrarCursoVigenteConcepto(ca);

						}

					}

				});

	}

	private void borrarCursoVigenteConcepto(CursoVigenteConcepto ca) {

		this.reg.deleteObject(ca);

		this.refrescarConceptos(this.cursoVigenteSelectedAlumnoConceptoMateria);

		BindUtils.postNotifyChange(null, null, this, "lConceptosCursosVigentes");

	}

	private List<Object[]> lConceptosbuscarOri;
	private List<Object[]> lConceptosBuscar;
	private Concepto buscarSelectedConcepto;
	private String buscarConcepto = "";

	@Command
	@NotifyChange("lConceptosBuscar")
	public void filtrarConceptoBuscar() {

		this.lConceptosBuscar = this.filtrarListaObject(buscarConcepto, this.lConceptosbuscarOri);

	}

	@Command
	@NotifyChange("lConceptosBuscar")
	public void generarListaBuscarConcepto() {

		if (this.cursoVigenteSelectedAlumnoConceptoMateria == null) {

			this.mensajeInfo("Selecciona un CursoVigente.");

			return;
		}

		String sqlBuscarConcepto = this.um.getSql("buscarCursoVigenteConcepto.sql").replace("?1",
				this.cursoVigenteSelectedAlumnoConceptoMateria.getCursovigenteid() + "");

		this.lConceptosBuscar = this.reg.sqlNativo(sqlBuscarConcepto);
		this.lConceptosbuscarOri = this.lConceptosBuscar;
	}

	@Command
	@NotifyChange("buscarConcepto")
	public void onSelectConcepto(@BindingParam("id") long id) {

		this.buscarSelectedConcepto = this.reg.getObjectById(Concepto.class.getName(), id);
		this.buscarConcepto = buscarSelectedConcepto.getConcepto();
		this.cursoVigenteConceptoSelected.setConcepto(buscarSelectedConcepto);

	}

	// fins buscador
	
	// Seccion Materias cursoVigente

		@Command
		public void modalCursoVigenteMateriaAgregar() {

			if (!this.opAgregarCursoVigenteMateria)
				return;

			if (this.cursoVigenteSelectedAlumnoConceptoMateria == null) {

				this.mensajeInfo("Seleccione un Curso Vigente.");
				return;
			}

			this.editar = false;
			modalCursoVigenteMateria(null);

		}

		@Command
		public void modalCursoVigenteMateria(
				@BindingParam("cursovigentemateria") CursoVigenteMateria cursoVigenteMateria) {

			if (cursoVigenteMateria != null) {

				if (!this.opEditarCursoVigenteMateria)
					return;

				this.cursoVigenteMateriaSelected = cursoVigenteMateria;
				this.buscarMateria = this.cursoVigenteMateriaSelected.getMateria().getMateria();
				this.editar = true;

			} else {

				this.cursoVigenteMateriaSelected = new CursoVigenteMateria();
				this.cursoVigenteMateriaSelected.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateria);
				this.buscarMateria = "";

			}

			modal = (Window) Executions.createComponents("/instituto/zul/administracion/cursoVigenteMateriaModal.zul",
					this.mainComponent, null);
			Selectors.wireComponents(modal, this, false);
			modal.doModal();

		}

		public boolean verificarCamposMateria() {

			return true;
		}

		@Command
		@NotifyChange({ "lMateriasCursosVigentes" })
		public void guardarMateria() {

			if (!verificarCamposMateria()) {
				return;
			}

			this.save(cursoVigenteMateriaSelected);

			this.cursoVigenteMateriaSelected = null;

			this.modal.detach();

			if (editar) {

				Notification.show("El Materia fue Actualizado.");
				this.editar = false;
			} else {

				Notification.show("El Materia fue agregado.");
			}

			this.refrescarMaterias(this.cursoVigenteSelectedAlumnoConceptoMateria);

		}

		@Command
		@NotifyChange({ "lMateriasCursosVigentes", "buscarMateria" })
		public void refrescarMaterias(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

			this.cursoVigenteSelectedAlumnoConceptoMateria = cursoVigente;
			this.lMateriasCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteMateria.class.getName(),
					"cursoVigenteid = " + cursoVigente.getCursovigenteid(), "orden asc");

			/*
			 * .buscarSelectedMateria = null; this.buscarMateria = "";
			 */

		}

		@Command
		public void borrarMateriaConfirmacion(@BindingParam("cursoVigenteMateria") final CursoVigenteMateria ca) {

			if (!this.opQuitarCursoVigenteMateria) {

				this.mensajeError("No tienes permisos para Borrar Materias a un CursoVigente.");

				return;

			}

			this.mensajeEliminar("El Materia " + ca.getMateria().getMateria() + " sera removido del Curso Vigente"
					+ ca.getCursoVigente().getCurso().getCurso() + " \n Continuar?", new EventListener() {

						@Override
						public void onEvent(Event evt) throws Exception {

							if (evt.getName().equals(Messagebox.ON_YES)) {

								borrarCursoVigenteMateria(ca);

							}

						}

					});

		}

		private void borrarCursoVigenteMateria(CursoVigenteMateria ca) {

			this.reg.deleteObject(ca);

			this.refrescarMaterias(this.cursoVigenteSelectedAlumnoConceptoMateria);

			BindUtils.postNotifyChange(null, null, this, "lMateriasCursosVigentes");

		}

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

			if (this.cursoVigenteSelectedAlumnoConceptoMateria == null) {

				this.mensajeInfo("Selecciona un CursoVigente.");

				return;
			}

			String sqlBuscarMateria = this.um.getSql("buscarMateriaNotCursoVigente.sql")
					.replace("?1", this.cursoVigenteSelectedAlumnoConceptoMateria.getCurso().getCursoid() + "")
					.replace("?2", this.cursoVigenteSelectedAlumnoConceptoMateria.getCursovigenteid()+ "");

			this.lMateriasBuscar = this.reg.sqlNativo(sqlBuscarMateria);
			this.lMateriasbuscarOri = this.lMateriasBuscar;
		}

		@Command
		@NotifyChange("buscarMateria")
		public void onSelectMateria(@BindingParam("id") long id) {

			this.buscarSelectedMateria = this.reg.getObjectById(Materia.class.getName(), id);
			this.buscarMateria = buscarSelectedMateria.getMateria();
			this.cursoVigenteMateriaSelected.setMateria(buscarSelectedMateria);

		}

	// fin curso vigente materia

	// fin Conceptos cursoVigente

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

	public List<CursoVigenteConcepto> getlConceptosCursosVigentes() {
		return lConceptosCursosVigentes;
	}

	public void setlConceptosCursosVigentes(List<CursoVigenteConcepto> lConceptosCursosVigentes) {
		this.lConceptosCursosVigentes = lConceptosCursosVigentes;
	}

	public boolean isOpAgregarCursoVigenteConcepto() {
		return opAgregarCursoVigenteConcepto;
	}

	public void setOpAgregarCursoVigenteConcepto(boolean opAgregarCursoVigenteConcepto) {
		this.opAgregarCursoVigenteConcepto = opAgregarCursoVigenteConcepto;
	}

	public boolean isOpQuitarCursoVigenteConcepto() {
		return opQuitarCursoVigenteConcepto;
	}

	public void setOpQuitarCursoVigenteConcepto(boolean opQuitarCursoVigenteConcepto) {
		this.opQuitarCursoVigenteConcepto = opQuitarCursoVigenteConcepto;
	}

	public String getBuscarConcepto() {
		return buscarConcepto;
	}

	public void setBuscarConcepto(String buscarConcepto) {
		this.buscarConcepto = buscarConcepto;
	}

	public CursoVigenteConcepto getCursoVigenteConceptoSelected() {
		return cursoVigenteConceptoSelected;
	}

	public void setCursoVigenteConceptoSelected(CursoVigenteConcepto cursoVigenteConceptoSelected) {
		this.cursoVigenteConceptoSelected = cursoVigenteConceptoSelected;
	}

	public boolean isOpEditarCursoVigenteConcepto() {
		return opEditarCursoVigenteConcepto;
	}

	public void setOpEditarCursoVigenteConcepto(boolean opEditarCursoVigenteConcepto) {
		this.opEditarCursoVigenteConcepto = opEditarCursoVigenteConcepto;
	}

	public List<Object[]> getlConceptosBuscar() {
		return lConceptosBuscar;
	}

	public void setlConceptosBuscar(List<Object[]> lConceptosBuscar) {
		this.lConceptosBuscar = lConceptosBuscar;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public boolean isOpAgregarCursoVigenteMateria() {
		return opAgregarCursoVigenteMateria;
	}

	public void setOpAgregarCursoVigenteMateria(boolean opAgregarCursoVigenteMateria) {
		this.opAgregarCursoVigenteMateria = opAgregarCursoVigenteMateria;
	}

	public boolean isOpQuitarCursoVigenteMateria() {
		return opQuitarCursoVigenteMateria;
	}

	public void setOpQuitarCursoVigenteMateria(boolean opQuitarCursoVigenteMateria) {
		this.opQuitarCursoVigenteMateria = opQuitarCursoVigenteMateria;
	}

	public boolean isOpEditarCursoVigenteMateria() {
		return opEditarCursoVigenteMateria;
	}

	public void setOpEditarCursoVigenteMateria(boolean opEditarCursoVigenteMateria) {
		this.opEditarCursoVigenteMateria = opEditarCursoVigenteMateria;
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

	public List<CursoVigenteMateria> getlMateriasCursosVigentes() {
		return lMateriasCursosVigentes;
	}

	public void setlMateriasCursosVigentes(List<CursoVigenteMateria> lMateriasCursosVigentes) {
		this.lMateriasCursosVigentes = lMateriasCursosVigentes;
	}

	public CursoVigenteMateria getCursoVigenteMateriaSelected() {
		return cursoVigenteMateriaSelected;
	}

	public void setCursoVigenteMateriaSelected(CursoVigenteMateria cursoVigenteMateriaSelected) {
		this.cursoVigenteMateriaSelected = cursoVigenteMateriaSelected;
	}

}
