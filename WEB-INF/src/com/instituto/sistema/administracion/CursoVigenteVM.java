package com.instituto.sistema.administracion;

import java.util.Collections;
import java.util.Comparator;
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

import com.instituto.modelo.Alumno;
import com.instituto.modelo.Concepto;
import com.instituto.modelo.Convenio;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.CursoVigenteConcepto;
import com.instituto.modelo.CursoVigenteConvenio;
import com.instituto.modelo.CursoVigenteMateria;
import com.instituto.modelo.Materia;
import com.instituto.modelo.Curso;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CursoVigenteVM extends TemplateViewModelLocal {

	private List<CursoVigente> lCursosVigentes;
	private List<CursoVigente> lCursosVigentesOri;
	private CursoVigente cursoVigenteSelected;
	private CursoVigente cursoVigenteSelectedAlumnoConceptoMateriaConvenio;
	private List<CursoVigenteAlumno> lAlumnosCursosVigentes;
	private List<CursoVigenteAlumno> lAlumnosCursosVigentesOri;
	private List<CursoVigenteConcepto> lConceptosCursosVigentes;
	private List<CursoVigenteConcepto> lConceptosCursosVigentesOri;
	private List<CursoVigenteMateria> lMateriasCursosVigentes;
	private List<CursoVigenteMateria> lMateriasCursosVigentesOri;
	private List<CursoVigenteConvenio> lConveniosCursosVigentesOri;
	private List<CursoVigenteConvenio> lConveniosCursosVigentes;
	private CursoVigenteConcepto cursoVigenteConceptoSelected;
	private CursoVigenteMateria cursoVigenteMateriaSelected;
	private CursoVigenteConvenio cursoVigenteConvenioSelected;

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

	private boolean opAgregarCursoVigenteConvenio;
	private boolean opQuitarCursoVigenteConvenio;
	private boolean opEditarCursoVigenteConvenio;

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

		this.opAgregarCursoVigenteConvenio = this.operacionHabilitada(ParamsLocal.OP_AGREGAR_CURSOVIGENTE_CONVENIO);
		this.opQuitarCursoVigenteConvenio = this.operacionHabilitada(ParamsLocal.OP_QUITAR_CURSOVIGENTE_CONVENIO);
		this.opEditarCursoVigenteConvenio = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CURSOVIGENTE_CONVENIO);

	}

	private void cargarCursosVigentes() {

		this.lCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigente.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid(), "CursoVigenteid asc");
		this.lCursosVigentesOri = this.lCursosVigentes;
	}

	// seccion filtro

	private String filtroColumns[];
	private String filtroColumnsAlumnos[];
	private String filtroColumnsConceptos[];
	private String filtroColumnsMaterias[];
	private String filtroColumnsConvenios[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		this.filtroColumnsAlumnos = new String[2];
		this.filtroColumnsConceptos = new String[2];
		this.filtroColumnsMaterias = new String[2];
		this.filtroColumnsConvenios = new String[2];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

		for (int i = 0; i < this.filtroColumnsAlumnos.length; i++) {

			this.filtroColumnsAlumnos[i] = "";

		}

		for (int i = 0; i < this.filtroColumnsConceptos.length; i++) {

			this.filtroColumnsConceptos[i] = "";

		}

		for (int i = 0; i < this.filtroColumnsMaterias.length; i++) {

			this.filtroColumnsMaterias[i] = "";

		}
		
		for (int i = 0; i < this.filtroColumnsConvenios.length; i++) {

			this.filtroColumnsConvenios[i] = "";

		}

	}

	@Command
	@NotifyChange("lCursosVigentes")
	public void filtrarCursoVigente() {

		this.lCursosVigentes = this.filtrarLT(this.filtroColumns, this.lCursosVigentesOri);

	}

	@Command
	@NotifyChange("lAlumnosCursosVigentes")
	public void filtrarCursoVigenteAlumno() {

		this.lAlumnosCursosVigentes = this.filtrarLT(this.filtroColumnsAlumnos, this.lAlumnosCursosVigentesOri);

	}

	@Command
	@NotifyChange("lConceptosCursosVigentes")
	public void filtrarCursoVigenteConcepto() {

		this.lConceptosCursosVigentes = this.filtrarLT(this.filtroColumnsConceptos, this.lConceptosCursosVigentesOri);

	}

	@Command
	@NotifyChange("lMateriasCursosVigentes")
	public void filtrarCursoVigenteMateria() {

		this.lMateriasCursosVigentes = this.filtrarLT(this.filtroColumnsMaterias, this.lMateriasCursosVigentesOri);

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
	@NotifyChange({ "lAlumnosCursosVigentes", "buscarAlumno", "lConceptosCursosVigentes", "buscarConcepto",
			"lMateriasCursosVigentes", "buscarMateria","lConveniosCursosVigentes", "buscarConvenio" })
	public void refrescarAll(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.refrescarAlumnos(cursoVigente);
		this.refrescarConceptos(cursoVigente);
		this.refrescarMaterias(cursoVigente);
		this.refrescarConvenios(cursoVigente);

	}

	// Seccion alumnos cursoVigente

	@Command
	@NotifyChange({ "lAlumnosCursosVigentes", "buscarAlumno" })
	public void refrescarAlumnos(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio = cursoVigente;

		String[] join = { "en.alumno as a", "a.persona as p" };
		String[] condicion = { "cursoVigenteid = " + cursoVigente.getCursovigenteid() };
		String[] orden = {};

		this.lAlumnosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteAlumno.class.getName(),
				"cursoVigenteid = " + cursoVigente.getCursovigenteid(), null);

		Collections.sort(this.lAlumnosCursosVigentes, new Comparator<CursoVigenteAlumno>() {

			@Override
			public int compare(CursoVigenteAlumno o1, CursoVigenteAlumno o2) {
				return o1.getAlumno().getFullNombre().compareToIgnoreCase(o2.getAlumno().getFullNombre());
			}

		});

		this.lAlumnosCursosVigentesOri = this.lAlumnosCursosVigentes;

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

		this.refrescarAlumnos(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

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

		if (this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {

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

		for (CursoVigenteAlumno x : this.lAlumnosCursosVigentesOri) {

			if (this.buscarSelectedAlumno.getAlumnoid().longValue() == x.getAlumno().getAlumnoid().longValue()) {

				this.mensajeError("El CursoVigente ya tiene el alumno " + x.getAlumno().getFullNombre());

				this.buscarAlumno = "";
				this.buscarSelectedAlumno = null;

				return;

			}

		}

		CursoVigenteAlumno cu = new CursoVigenteAlumno();
		cu.setAlumno(this.buscarSelectedAlumno);
		cu.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);
		this.save(cu);

		// this.lastPageListBox((Listbox) this.mainComponent.query("#lbAlumnos"));

		this.refrescarAlumnos(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

	}

	// fins buscador

	// Seccion Conceptos cursoVigente

	@Command
	public void modalCursoVigenteConceptoAgregar() {

		if (!this.opAgregarCursoVigenteConcepto)
			return;

		if (this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {

			this.mensajeInfo("Seleccione un Curso Vigente.");
			return;
		}
		
		if (this.lAlumnosCursosVigentesOri.size() > 0) {
			
			this.mensajeInfo("No se puede agregar Conceptos, ya existen alumnos.");			
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
			this.cursoVigenteConceptoSelected.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);
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

		this.refrescarConceptos(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

	}

	@Command
	@NotifyChange({ "lConceptosCursosVigentes", "buscarConcepto" })
	public void refrescarConceptos(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio = cursoVigente;
		this.lConceptosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteConcepto.class.getName(),
				"cursoVigenteid = " + cursoVigente.getCursovigenteid(), "conceptoid asc");

		this.lConceptosCursosVigentesOri = this.lConceptosCursosVigentes;

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

		this.refrescarConceptos(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

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

		if (this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {

			this.mensajeInfo("Selecciona un CursoVigente.");

			return;
		}

		String sqlBuscarConcepto = this.um.getSql("buscarCursoVigenteConcepto.sql").replace("?1",
				this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio.getCursovigenteid() + "");

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

		if (this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {

			this.mensajeInfo("Seleccione un Curso Vigente.");
			return;
		}

		this.editar = false;
		modalCursoVigenteMateria(null);

	}

	@Command
	public void modalCursoVigenteMateria(@BindingParam("cursovigentemateria") CursoVigenteMateria cursoVigenteMateria) {

		if (cursoVigenteMateria != null) {

			if (!this.opEditarCursoVigenteMateria)
				return;

			this.cursoVigenteMateriaSelected = cursoVigenteMateria;
			this.buscarMateria = this.cursoVigenteMateriaSelected.getMateria().getMateria();
			this.editar = true;

		} else {

			this.cursoVigenteMateriaSelected = new CursoVigenteMateria();
			this.cursoVigenteMateriaSelected.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);
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

		this.refrescarMaterias(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

	}

	@Command
	@NotifyChange({ "lMateriasCursosVigentes", "buscarMateria" })
	public void refrescarMaterias(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio = cursoVigente;
		this.lMateriasCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteMateria.class.getName(),
				"cursoVigenteid = " + cursoVigente.getCursovigenteid(), "orden asc");

		this.lMateriasCursosVigentesOri = this.lMateriasCursosVigentes;

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

		this.refrescarMaterias(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

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

		if (this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {

			this.mensajeInfo("Selecciona un CursoVigente.");

			return;
		}

		String sqlBuscarMateria = this.um.getSql("buscarMateriaNotCursoVigente.sql")
				.replace("?1", this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio.getCurso().getCursoid() + "")
				.replace("?2", this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio.getCursovigenteid() + "");

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

	// Seccion Convenios cursoVigente

	@Command
	public void modalCursoVigenteConvenioAgregar() {

		if (!this.opAgregarCursoVigenteConvenio)
			return;

		if (this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {

			this.mensajeInfo("Seleccione un Curso Vigente.");
			return;
		}
		
		if (this.lAlumnosCursosVigentesOri.size() > 0) {
			
			this.mensajeInfo("No se puede agregar Convenios, ya existen alumnos.");			
			return;
		}

		this.editar = false;
		modalCursoVigenteConvenio(null);

	}

	@Command
	public void modalCursoVigenteConvenio(
			@BindingParam("cursovigenteconvenio") CursoVigenteConvenio cursoVigenteConvenio) {

		if (cursoVigenteConvenio != null) {

			if (!this.opEditarCursoVigenteConvenio)
				return;

			this.cursoVigenteConvenioSelected = cursoVigenteConvenio;
			this.buscarConvenio = this.cursoVigenteConvenioSelected.getConvenio().getDescripcion();
			this.editar = true;

		} else {

			this.cursoVigenteConvenioSelected = new CursoVigenteConvenio();
			this.cursoVigenteConvenioSelected.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);
			this.buscarConvenio = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cursoVigenteConvenioModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	public boolean verificarCamposConvenio() {

		return true;
	}

	@Command
	@NotifyChange({ "lConveniosCursosVigentes" })
	public void guardarConvenio() {

		if (!verificarCamposConvenio()) {
			return;
		}

		this.save(cursoVigenteConvenioSelected);

		this.cursoVigenteConvenioSelected = null;

		this.modal.detach();

		if (editar) {

			Notification.show("El Convenio fue Actualizado.");
			this.editar = false;
		} else {

			Notification.show("El Convenio fue agregado.");
		}

		this.refrescarConvenios(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

	}

	@Command
	@NotifyChange({ "lConveniosCursosVigentes", "buscarConvenio" })
	public void refrescarConvenios(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio = cursoVigente;
		this.lConveniosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteConvenio.class.getName(),
				"cursoVigenteid = " + cursoVigente.getCursovigenteid(), "orden asc");

		this.lConveniosCursosVigentesOri = this.lConveniosCursosVigentes;

		/*
		 * .buscarSelectedConvenio = null; this.buscarConvenio = "";
		 */

	}

	@Command
	public void borrarConvenioConfirmacion(@BindingParam("cursoVigenteConvenio") final CursoVigenteConvenio ca) {

		if (!this.opQuitarCursoVigenteConvenio) {

			this.mensajeError("No tienes permisos para Borrar Convenios a un CursoVigente.");

			return;

		}

		this.mensajeEliminar("El Convenio " + ca.getConvenio().getDescripcion() + " sera removido del Curso Vigente"
				+ ca.getCursoVigente().getCurso().getCurso() + " \n Continuar?", new EventListener() {

					@Override
					public void onEvent(Event evt) throws Exception {

						if (evt.getName().equals(Messagebox.ON_YES)) {

							borrarCursoVigenteConvenio(ca);

						}

					}

				});

	}

	private void borrarCursoVigenteConvenio(CursoVigenteConvenio ca) {

		this.reg.deleteObject(ca);

		this.refrescarConvenios(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

		BindUtils.postNotifyChange(null, null, this, "lConveniosCursosVigentes");

	}

	private List<Object[]> lConveniosbuscarOri;
	private List<Object[]> lConveniosBuscar;
	private Convenio buscarSelectedConvenio;
	private String buscarConvenio = "";

	@Command
	@NotifyChange("lConveniosBuscar")
	public void filtrarConvenioBuscar() {

		this.lConveniosBuscar = this.filtrarListaObject(buscarConvenio, this.lConveniosbuscarOri);

	}

	@Command
	@NotifyChange("lConveniosBuscar")
	public void generarListaBuscarConvenio() {

		if (this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {

			this.mensajeInfo("Selecciona un CursoVigente.");

			return;
		}

		String sqlBuscarConvenio = this.um.getSql("buscarConvenioNotCursoVigente.sql")
				.replace("?1", this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio.getCursovigenteid() + "");

		this.lConveniosBuscar = this.reg.sqlNativo(sqlBuscarConvenio);
		this.lConveniosbuscarOri = this.lConveniosBuscar;
	}

	@Command
	@NotifyChange("buscarConvenio")
	public void onSelectConvenio(@BindingParam("id") long id) {

		this.buscarSelectedConvenio = this.reg.getObjectById(Convenio.class.getName(), id);
		this.buscarConvenio = buscarSelectedConvenio.getDescripcion();
		this.cursoVigenteConvenioSelected.setConvenio(buscarSelectedConvenio);

	}

	// fin curso vigente convenio

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

	public String[] getFiltroColumnsAlumnos() {
		return filtroColumnsAlumnos;
	}

	public void setFiltroColumnsAlumnos(String[] filtroColumnsAlumnos) {
		this.filtroColumnsAlumnos = filtroColumnsAlumnos;
	}

	public String[] getFiltroColumnsConceptos() {
		return filtroColumnsConceptos;
	}

	public void setFiltroColumnsConceptos(String[] filtroColumnsConceptos) {
		this.filtroColumnsConceptos = filtroColumnsConceptos;
	}

	public String[] getFiltroColumnsMaterias() {
		return filtroColumnsMaterias;
	}

	public void setFiltroColumnsMaterias(String[] filtroColumnsMaterias) {
		this.filtroColumnsMaterias = filtroColumnsMaterias;
	}

	public List<CursoVigenteConvenio> getlConveniosCursosVigentes() {
		return lConveniosCursosVigentes;
	}

	public void setlConveniosCursosVigentes(List<CursoVigenteConvenio> lConveniosCursosVigentes) {
		this.lConveniosCursosVigentes = lConveniosCursosVigentes;
	}

	public boolean isOpAgregarCursoVigenteConvenio() {
		return opAgregarCursoVigenteConvenio;
	}

	public void setOpAgregarCursoVigenteConvenio(boolean opAgregarCursoVigenteConvenio) {
		this.opAgregarCursoVigenteConvenio = opAgregarCursoVigenteConvenio;
	}

	public boolean isOpQuitarCursoVigenteConvenio() {
		return opQuitarCursoVigenteConvenio;
	}

	public void setOpQuitarCursoVigenteConvenio(boolean opQuitarCursoVigenteConvenio) {
		this.opQuitarCursoVigenteConvenio = opQuitarCursoVigenteConvenio;
	}

	public boolean isOpEditarCursoVigenteConvenio() {
		return opEditarCursoVigenteConvenio;
	}

	public void setOpEditarCursoVigenteConvenio(boolean opEditarCursoVigenteConvenio) {
		this.opEditarCursoVigenteConvenio = opEditarCursoVigenteConvenio;
	}

	public String getBuscarConvenio() {
		return buscarConvenio;
	}

	public void setBuscarConvenio(String buscarConvenio) {
		this.buscarConvenio = buscarConvenio;
	}

	public String[] getFiltroColumnsConvenios() {
		return filtroColumnsConvenios;
	}

	public void setFiltroColumnsConvenios(String[] filtroColumnsConvenios) {
		this.filtroColumnsConvenios = filtroColumnsConvenios;
	}

	public List<Object[]> getlConveniosBuscar() {
		return lConveniosBuscar;
	}

	public void setlConveniosBuscar(List<Object[]> lConveniosBuscar) {
		this.lConveniosBuscar = lConveniosBuscar;
	}

}
