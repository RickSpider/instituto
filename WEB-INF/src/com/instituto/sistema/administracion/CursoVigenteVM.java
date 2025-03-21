package com.instituto.sistema.administracion;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Tipotipo;
import com.doxacore.report.ReportExcel;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.Concepto;
import com.instituto.modelo.Convenio;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.CursoVigenteConcepto;
import com.instituto.modelo.CursoVigenteConvenio;
import com.instituto.modelo.CursoVigenteMateria;
import com.instituto.modelo.Empresa;
import com.instituto.modelo.Materia;
import com.instituto.modelo.Proveedor;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.modelo.Evaluacion;
import com.instituto.modelo.EvaluacionDetalle;
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
	private CursoVigenteAlumno cursoVigenteAlumnoSelected;
	private CursoVigenteConcepto cursoVigenteConceptoSelected;
	private CursoVigenteMateria cursoVigenteMateriaSelected;
	private CursoVigenteConvenio cursoVigenteConvenioSelected;
	
	private Tipo evaluacionTipoSelected;

	private boolean[] bDias = new boolean[7];

	private boolean opCrearCursoVigente;
	private boolean opEditarCursoVigente;
	private boolean opBorrarCursoVigente;

	private boolean opAgregarCursoVigenteAlumno;
	private boolean opQuitarCursoVigenteAlumno;
	private boolean opEditarCursoVigenteAlumno;
	private boolean opAnularCursoVigenteAlumno;

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
		this.opEditarCursoVigenteAlumno = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CURSOVIGENTE_ALUMNO);
		this.opAnularCursoVigenteAlumno = this.operacionHabilitada(ParamsLocal.OP_ANULAR_CURSOVIGENTE_ALUMNO);
		
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
				"sedeid = " + this.getCurrentSede().getSedeid(), "CursoVigenteid desc");
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
		this.filtroColumnsMaterias = new String[5];
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
			"lMateriasCursosVigentes", "buscarMateria", "lConveniosCursosVigentes", "buscarConvenio" })
	public void refrescarAll(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.refrescarAlumnos(cursoVigente);
		this.refrescarConceptos(cursoVigente);
		this.refrescarMaterias(cursoVigente);
		this.refrescarConvenios(cursoVigente);

	}

	// Seccion alumnos cursoVigente

	@Command
	public void modalCursoVigenteAlumnoAgregar() {

		if (!this.opAgregarCursoVigenteAlumno)
			return;

		if (this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {

			this.mensajeInfo("Seleccione un Curso Vigente.");
			return;
		}

		this.editar = false;
		modalCursoVigenteAlumno(null);

	}

	@Command
	public void modalCursoVigenteAlumno(@BindingParam("cursovigentealumno") CursoVigenteAlumno cursoVigenteAlumno) {

		if (cursoVigenteAlumno != null) {

			if (!this.opEditarCursoVigenteAlumno)
				return;

			this.cursoVigenteAlumnoSelected = cursoVigenteAlumno;
			this.buscarAlumno = this.cursoVigenteAlumnoSelected.getAlumno().getFullNombre();
			this.editar = true;

		} else {

			this.cursoVigenteAlumnoSelected = new CursoVigenteAlumno();
			this.cursoVigenteAlumnoSelected.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);
			this.buscarAlumno = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cursoVigenteAlumnoModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	public boolean verificarCamposAlumno() {

		return true;
	}

	@Command
	@NotifyChange({ "lAlumnosCursosVigentes" })
	public void guardarAlumno() {

		if (!verificarCamposAlumno()) {
			return;
		}

		this.save(cursoVigenteAlumnoSelected);

		this.modal.detach();

		if (editar) {

			Notification.show("El Alumno fue Actualizado.");
			this.editar = false;
		} else {

			GenerarEstadoCuenta gec = new GenerarEstadoCuenta();
			List<EstadoCuenta> lec = gec.generarMovimientoAlumno(this.cursoVigenteAlumnoSelected, this.lConceptosCursosVigentesOri);
			
			for (EstadoCuenta x : lec) {
				
				this.save(x);
				
			}
			
			Notification.show("El Alumno fue agregado.");
		}

		this.cursoVigenteAlumnoSelected = null;

		this.refrescarAlumnos(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

	}
	
	@Command
	public void generarEstadoCuentaCursoVigente() {
		
		if(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio == null) {
			
			this.mensajeError("Seleccione un cursoVigente");
			
			return;
			
		}
		
		
		for (CursoVigenteAlumno cva : this.lAlumnosCursosVigentesOri) {
			
			System.out.println("Generando alumno "+cva.getAlumno().getFullNombre());
			
			GenerarEstadoCuenta gec = new GenerarEstadoCuenta();
			List<EstadoCuenta> lec = gec.generarMovimientoAlumno(cva, this.lConceptosCursosVigentesOri);
			
			for (EstadoCuenta x : lec) {
				
				this.save(x);
				
			}
			
		}
		
		
		
	}

	@Command
	@NotifyChange({ "lAlumnosCursosVigentes", "buscarAlumno" })
	public void refrescarAlumnos(@BindingParam("cursoVigente") CursoVigente cursoVigente) {

		this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio = cursoVigente;
		this.lAlumnosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteAlumno.class.getName(),
				"cursoVigenteid = " + cursoVigente.getCursovigenteid()+" AND inscripcionAnulada = false", "alumnoid asc");

		this.lAlumnosCursosVigentesOri = this.lAlumnosCursosVigentes;

		/*
		 * .buscarSelectedAlumno = null; this.buscarAlumno = "";
		 */

	}

	@Command
	public void borrarAlumnoConfirmacion(@BindingParam("cursoVigenteAlumno") final CursoVigenteAlumno ca) {

		if (!this.opQuitarCursoVigenteAlumno) {

			this.mensajeError("No tienes permisos para Borrar Alumnos a un CursoVigente.");

			return;

		}

		this.mensajeEliminar("El Alumno " + ca.getAlumno().getFullNombre() + " sera removido del Curso Vigente"
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
		
		
		List<EstadoCuenta> lEstadoCuenta = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(),
				"cursovigenteid = "+ca.getCursoVigente().getCursovigenteid()+" AND alumnoid = "+ca.getAlumno().getAlumnoid() , "estadocuentaid asc");
		
		boolean hayMovimiento = false;
		
		for (EstadoCuenta x : lEstadoCuenta) {
			
			if (x.getPago()> 0) {
				
				hayMovimiento = true;
				break;
				
			}
			
			if (x.isInactivo()) {
				
				hayMovimiento = true;
				break;
				
			}
			
			List<CobranzaDetalle> lCobranzasDetalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(),
					"estadocuentaid = " + x.getEstadocuentaid(), null);
			
			if (lCobranzasDetalles.size() > 0) {
				
				hayMovimiento = true;
				break;
				
			}
						
		}
		
		if (hayMovimiento) {
			
			this.mensajeError("El Alumno Posee Movimientos, intente desinscribir al alumno.");			
			return;
			
		}
		
		for (EstadoCuenta x : lEstadoCuenta) {
			
			this.reg.deleteObject(x);
			
		}
		

		this.reg.deleteObject(ca);

		this.refrescarAlumnos(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

		BindUtils.postNotifyChange(null, null, this, "lAlumnosCursosVigentes");

	}

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

		String sqlBuscarAlumno = this.um.getSql("buscarAlumnoNotCursoVigente.sql").replace("?1", this.getCurrentSede().getSedeid()+"")
				.replace("?2", this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio.getCursovigenteid() + "");

		this.lAlumnosBuscar = this.reg.sqlNativo(sqlBuscarAlumno);
		this.lAlumnosbuscarOri = this.lAlumnosBuscar;
	}

	@Command
	@NotifyChange("buscarAlumno")
	public void onSelectAlumno(@BindingParam("id") long id) {

		this.buscarSelectedAlumno = this.reg.getObjectById(Alumno.class.getName(), id);
		this.buscarAlumno = buscarSelectedAlumno.getFullNombre();
		this.cursoVigenteAlumnoSelected.setAlumno(buscarSelectedAlumno);

	}

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
	
	private Date incrementoFecha = new Date();
	private double incremento = 0;
	
	@Command 
	public void cursoVigenteConceptoIncremento(@BindingParam("cursovigenteconcepto") CursoVigenteConcepto cursoVigenteConcepto) {
		
		if (!this.opEditarCursoVigenteConcepto)
			return;
		
		this.cursoVigenteConceptoSelected = cursoVigenteConcepto;

		
		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cursoVigenteConceptoIncremento.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
		
	}
	
	@Command
	public void confirmacionIncremento() {
		
		this.mensajeEliminar("Se incrementaran los estasdos de cuentas de los alumnos que no han pagado apartir de fecha establecida"+" \n Continuar?", new EventListener() {

					@Override
					public void onEvent(Event evt) throws Exception {

						if (evt.getName().equals(Messagebox.ON_YES)) {

							actualizarIncremento(incrementoFecha, cursoVigenteConceptoSelected);

						}

					}

				});

	}
	
	
	public void actualizarIncremento(Date fecha, CursoVigenteConcepto cvc) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String sql="update estadoscuentas\r\n" +
				"set monto = monto+"+incremento+"\n"+
				"where cursovigenteid = "+cvc.getCursoVigente().getCursovigenteid()+" \r\n" + 
				"and monto > (montodescuento + pago)\r\n" + 
				"and conceptoid = "+cvc.getConcepto().getConceptoid()+"\r\n" + 
				"and vencimiento >= '"+sdf.format(fecha)+"';";
		
		System.out.println(sql);
		
		this.reg.sqlNativoVoid(sql);
		
		
		
		this.incrementoFecha= new Date();
		this.incremento=0;
		
		Notification.show("Se actualizaron los estados de cuenta de los alumnos, favor verificar.");
		
		this.modal.detach();
	}
	
	
	@Command
	public void anularInscripcionAlumnoConfirmacion(@BindingParam("cursoVigenteAlumno") final CursoVigenteAlumno ca) {

		if (!this.opAnularCursoVigenteAlumno) {

			this.mensajeError("No tienes permisos para Borrar Alumnos a un CursoVigente.");

			return;

		}

		this.mensajeEliminar("Se anulara la inscripcion del Alumno " + ca.getAlumno().getFullNombre() + " al Curso Vigente"
				+ ca.getCursoVigente().getCurso().getCurso() + " \n Continuar?", new EventListener() {

					@Override
					public void onEvent(Event evt) throws Exception {

						if (evt.getName().equals(Messagebox.ON_YES)) {

							anularInscriptionAlumnoCursoVigente(ca);

						}

					}

				});

	}
	
	private void anularInscriptionAlumnoCursoVigente(CursoVigenteAlumno cva) {
		

		List<EstadoCuenta> lEstadoCuenta = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(),
				"cursovigenteid = "+cva.getCursoVigente().getCursovigenteid()+" AND alumnoid = "+cva.getAlumno().getAlumnoid() , "estadocuentaid asc");
		
		for (EstadoCuenta x : lEstadoCuenta) {
			
			if (x.getPago() == 0) {
				
				x.setInactivo(true);
				x.setMotivoInactivacion("Anulacion de Inscripcion al Curso");
				this.save(x);
				
			}
			
		}
		
		cva.setInscripcionAnulada(true);
		
		this.save(cva);
		
		this.refrescarAlumnos(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);

		BindUtils.postNotifyChange(null, null, this, "lAlumnosCursosVigentes");
		
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
		
		this.inicializarFinders();
		
		if (cursoVigenteMateria != null) {

			if (!this.opEditarCursoVigenteMateria)
				return;

			this.cursoVigenteMateriaSelected = cursoVigenteMateria;
			this.buscarMateria = this.cursoVigenteMateriaSelected.getMateria().getMateria();
			this.editar = true;
			this.buscarEstado = this.cursoVigenteMateriaSelected.getEstado().getTipo();

		} else {

			this.cursoVigenteMateriaSelected = new CursoVigenteMateria();
			this.cursoVigenteMateriaSelected.setCursoVigente(this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio);
			this.buscarMateria = "";
			this.buscarEstado = "";
			
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

			Notification.show("La Materia fue Actualizado.");
			this.editar = false;
		} else {

			Notification.show("La Materia fue agregado.");
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

		/*if (this.lAlumnosCursosVigentesOri.size() > 0) {

			this.mensajeInfo("No se puede agregar Convenios, ya existen alumnos.");
			return;
		}*/

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

		String sqlBuscarConvenio = this.um.getSql("buscarConvenioNotCursoVigente.sql").replace("?1",
				this.cursoVigenteSelectedAlumnoConceptoMateriaConvenio.getCursovigenteid() + "");

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

	// buscarEstado

	private List<Object[]> lEstadosBuscarOri = null;
	private List<Object[]> lEstadosBuscar = null;
	private Tipo buscarSelectedEstado = null;
	private String buscarEstado = "";

	@Command
	@NotifyChange("lEstadosBuscar")
	public void filtrarEstadoBuscar() {

		this.lEstadosBuscar = this.filtrarListaObject(buscarEstado, this.lEstadosBuscarOri);

	}

	@Command
	@NotifyChange("lEstadosBuscar")
	public void generarListaBuscarEstado() {

		Tipotipo tipotipo = this.reg.getObjectByColumnString(Tipotipo.class.getName(), "sigla",
				ParamsLocal.SIGLA_ESTADO_CV_MATERIA);
		String buscarEstadoSQL = this.um.getSql("buscarTipos.sql").replace("?1", "" + tipotipo.getTipotipoid());

		System.out.println(buscarEstadoSQL);

		this.lEstadosBuscar = this.reg.sqlNativo(buscarEstadoSQL);

		this.lEstadosBuscarOri = this.lEstadosBuscar;
	}

	@Command
	@NotifyChange("buscarEstado")
	public void onSelectEstado(@BindingParam("id") long id) {

		this.buscarSelectedEstado = this.reg.getObjectById(Tipo.class.getName(), id);
		this.buscarEstado = this.buscarSelectedEstado.getTipo();
		this.cursoVigenteMateriaSelected.setEstado(buscarSelectedEstado);

	}

	// fin buscarEstado
	
	
	// Seccion Finder

	private FinderModel proveedorFinder;
	private FinderModel evaluacionTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		Tipo docente = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_PROVEEDOR_DOCENTE);

		String sqlDocente = this.um.getSql("proveedor/buscarProveedorSedeTipo.sql").replace("?1", this.getCurrentSede().getSedeid()+"").replace("?2", docente.getTipoid()+"");

		proveedorFinder = new FinderModel("Docente", sqlDocente);
		
		String sqlEvaluacionTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_EVALUACION);
		
		this.evaluacionTipoFinder = new FinderModel("evaluacionTipo", sqlEvaluacionTipo);

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.proveedorFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.proveedorFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.evaluacionTipoFinder.getNameFinder()) == 0) {

			this.evaluacionTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.evaluacionTipoFinder, "listFinder");

			return;

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.proveedorFinder.setListFinder(this.filtrarListaObject(filter, this.proveedorFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.proveedorFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.evaluacionTipoFinder.getNameFinder()) == 0) {

			this.evaluacionTipoFinder.setListFinder(this.filtrarListaObject(filter, this.evaluacionTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.evaluacionTipoFinder, "listFinder");

			return;

		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.cursoVigenteMateriaSelected.setProveedor(this.reg.getObjectById(Proveedor.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}
		
		if (finder.compareTo(this.evaluacionTipoFinder.getNameFinder()) == 0) {

			this.evaluacionTipoSelected = this.reg.getObjectById(Tipo.class.getName(), id);
			

			return;
		}


	}
	
	
	@Command
	public void listaAlumnosExport(@BindingParam("cursoVigenteid") Long cursoVigenteid) {
		
		ReportExcel re = new ReportExcel("ListaAlumnos");
		CursoVigente cv = this.reg.getObjectById(CursoVigente.class.getName(), cursoVigenteid);
		
		List<String[]> titulos = new ArrayList<String[]>();
		
		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);
		String tituloLogo = empresa.getNombreFantasia()+"\n"
				+ empresa.getExtra2();
		
		//String[] t1 = {"INSTITUTO SANTO TOMAS"};
		//String[] t2 = {"LISTA DE ALUMNOS"};
		String[] espacioBlanco = {""};
		String[] t3 = {"Sede:",this.getCurrentSede().getSede()};
		String[] t4 = {"Curso:", cv.getCurso().getCurso()};
		
		
		
		//titulos.add(t1);
		//titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		
		
		List<String[]> headersDatos = new ArrayList<String[]>();
		String [] hd1 =  {"Nro", "Apellidos", "Nombres", "C.I.", "Nro Telefono", "Nombre Completo"};
		headersDatos.add(hd1);
		
		String sql = this.um.getSql("cursoVigenteListaAlumnos.sql").replace("?1", cursoVigenteid.toString());
		List<Object[]> datos = this.reg.sqlNativo(sql);
		
		for (int i = 0 ; i<datos.size() ; i++) {
			
			datos.get(i)[0] = i+1;
			
		}
		
		Empresa emp = this.reg.getObjectById(Empresa.class.getName(), 1);
		
		if (emp != null && emp.getPathLogo() != null) {
			 
			re.descargar(titulos, headersDatos, datos, tituloLogo, emp.getLogo() );
			
		}else {
			
			re.descargar(titulos, headersDatos, datos);
			
		}
		
		//re.descargar(titulos, headersDatos, datos);
		
		
	}
	
	@Command
	public void generarPlanillaMateriaModal(@BindingParam("cursovigentemateria") CursoVigenteMateria cursovigentemateria ) {
		
		this.inicializarFinders();
		
		this.evaluacionTipoSelected = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_EVALUACION_ORDINARIO);
		this.cursoVigenteMateriaSelected = cursovigentemateria;
		
		modal = (Window) Executions.createComponents("/instituto/zul/administracion/generarPlanillaMateriaModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
		
	}
	
	
	
	@Command
	public void planillaCalificacionesMateria() {
		
		
		CursoVigente cv = this.cursoVigenteMateriaSelected.getCursoVigente();
		Evaluacion ev = this.reg.getObjectByCondicion(Evaluacion.class.getName(), 
				"cursovigenteid = "+cv.getCursovigenteid()+" "+
				"and materiaid = "+cursoVigenteMateriaSelected.getMateria().getMateriaid()+" "+
				"and evaluacionTipoId = "+this.evaluacionTipoSelected.getTipoid());
		
		if (ev == null) {
			
			this.mensajeError("No hay evaluacion cargada.");
			
			return;
			
		}
		
		
		ReportExcel re = new ReportExcel("PlanillaCalificaciones_"+this.cursoVigenteMateriaSelected.getMateria().getMateria());
		
		List<String[]> titulos = new ArrayList<String[]>();
		
		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);
		String tituloLogo = empresa.getNombreFantasia()+"\n"
				+ empresa.getExtra2();
		
		//String[] t1 = {"INSTITUTO SANTO TOMAS"};
		//String[] t2 = {"Resolucion M.E.C. Nº 841/98"};
		String[] espacioBlanco = {""};
		String[] t3 = {"Sede:",this.getCurrentSede().getSede()};
		String[] t4 = {"Curso:", cv.getCurso().getCurso()};
		String[] t5 = {"ASIGNATURA: ", this.cursoVigenteMateriaSelected.getMateria().getMateria()};
		String[] t6 = {"EVALUCION: ", this.evaluacionTipoSelected.getTipo()};
		String[] t7 = {"FECHA:",};
		String[] t8 = {"PROFESOR/A:", this.cursoVigenteMateriaSelected.getProveedor().getPersona().getNombreCompleto()};
		
		//titulos.add(t1);
		//titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(espacioBlanco);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		titulos.add(t5);
		titulos.add(t6);
		titulos.add(t7);
		titulos.add(t8);
		
		List<String[]> headersDatos = new ArrayList<String[]>();
		String [] hd1 =  {"Nro","Apellidos", "Nombres", "C.I.", "EVALUACIONES", "","","","","", "TOTAL","Calificacion"};
		String [] hd2=  {"","", "", "", "Evaluacion 1","Evaluacion2","Evaluacion 3","Evaluacion 4","Evaluacion 5" ,"Evaluacion Final","Calificacion","Letra"};
		headersDatos.add(hd1);
		headersDatos.add(hd2);

		Empresa emp = this.reg.getObjectById(Empresa.class.getName(), 1);
		
		List<Object[]> datos2 = new ArrayList<>();
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("es", "ES"));
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0.##",dfs);
		
		int c = 1;		
		for (EvaluacionDetalle edx : ev.getDetalles()) {
			
			
			
			if (edx.getCalificacion()!= 0) {
				
				Object[] o = {c,edx.getAlumno().getPersona().getApellido(),
						edx.getAlumno().getPersona().getNombre(),
						edx.getAlumno().getPersona().getDocumentoNum(),
						df.format(edx.getProceso1()),
						df.format(edx.getProceso2()),
						df.format(edx.getProceso3()),
						df.format(edx.getProceso4()),
						df.format(edx.getProceso5()),
						df.format(edx.getPfinal()),
						df.format(edx.getCalificacion()),
						this.convertir(edx.getCalificacion())};
				
				datos2.add(o);
				c+=1;
			}
			
			
		}
		
		if (emp != null && emp.getPathLogo() != null) {
			 
			re.descargar(titulos, headersDatos, datos2, tituloLogo, emp.getLogo() );
			
		}else {
			
			re.descargar(titulos, headersDatos, datos2);
			
		}
		
		this.modal.detach();
		
	}
	
	 private String convertir(double numero) {
		 
		 int num = (int) numero;
		 
	        switch (num) {
	            case 1: return "uno";
	            case 2: return "dos";
	            case 3: return "tres";
	            case 4: return "cuatro";
	            case 5: return "cinco";
	            default: return "Número fuera de rango";
	        }
	    }
	
	@Command
	public void planillaCalificaciones(@BindingParam("cursoVigenteid") Long cursoVigenteid) {
		
		ReportExcel re = new ReportExcel("PlanillaCalificacionesCurso"+"cursoVigenteid");
		CursoVigente cv = this.reg.getObjectById(CursoVigente.class.getName(), cursoVigenteid);
		
		List<String[]> titulos = new ArrayList<String[]>();
		
		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);
		String tituloLogo = empresa.getNombreFantasia()+"\n"
				+ empresa.getExtra2();
		
		//String[] t1 = {"INSTITUTO SANTO TOMAS"};
		//String[] t2 = {"Resolucion M.E.C. Nº 841/98"};
		String[] espacioBlanco = {""};
		String[] t3 = {"Sede:",this.getCurrentSede().getSede()};
		String[] t4 = {"Curso:", cv.getCurso().getCurso()};
		String[] t5 = {"ASIGNATURA:"};
		String[] t6 = {"FECHA:"};
		String[] t7 = {"PROFESOR/A:"};
		
		//titulos.add(t1);
		//titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		titulos.add(t5);
		titulos.add(t6);
		titulos.add(t7);
		
		List<String[]> headersDatos = new ArrayList<String[]>();
		String [] hd1 =  {"Nro", "Apellidos", "Nombres", "C.I.", "EVALUACIONES", "","","", "TOTAL","Calificacion"};
		String [] hd2=  {"", "", "", "", "Evaluacion 1","Evaluacion2","Evaluacion 3","Evaluacion 4","Evaluacion 5" ,"Evaluacion Final","","Nº","Letra"};
		headersDatos.add(hd1);
		headersDatos.add(hd2);
		
		String sql = this.um.getSql("cursoVigenteListaAlumnosPlanillaCalif.sql").replace("?1", cursoVigenteid.toString());
		List<Object[]> datos = this.reg.sqlNativo(sql);
		
		for (int i = 0 ; i<datos.size() ; i++) {
			
			datos.get(i)[0] = i+1;
			
		}
		List<Object[]> datos2 = new ArrayList<>();
		
		for (Object[] x:datos) {
			
			Object [] o = new Object[11];
			
			for(int i = 0 ; i<o.length ; i++) {
				
				if (i < x.length) {
					
					o[i] = x[i];
					
				}else {
					
					o[i]="";
					
				}
				
				
				
			}
			
			datos2.add(o);
			
		}
		
		Empresa emp = this.reg.getObjectById(Empresa.class.getName(), 1);
		
		if (emp != null && emp.getPathLogo() != null) {
			 
			re.descargar(titulos, headersDatos, datos2, tituloLogo, emp.getLogo() );
			
		}else {
			
			re.descargar(titulos, headersDatos, datos2);
			
		}
		
		
		
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

	public List<Object[]> getlEstadosBuscar() {
		return lEstadosBuscar;
	}

	public void setlEstadosBuscar(List<Object[]> lEstadosBuscar) {
		this.lEstadosBuscar = lEstadosBuscar;
	}

	public String getBuscarEstado() {
		return buscarEstado;
	}

	public void setBuscarEstado(String buscarEstado) {
		this.buscarEstado = buscarEstado;
	}

	public CursoVigenteAlumno getCursoVigenteAlumnoSelected() {
		return cursoVigenteAlumnoSelected;
	}

	public void setCursoVigenteAlumnoSelected(CursoVigenteAlumno cursoVigenteAlumnoSelected) {
		this.cursoVigenteAlumnoSelected = cursoVigenteAlumnoSelected;
	}

	public boolean isOpAnularCursoVigenteAlumno() {
		return opAnularCursoVigenteAlumno;
	}

	public void setOpAnularCursoVigenteAlumno(boolean opAnularCursoVigenteAlumno) {
		this.opAnularCursoVigenteAlumno = opAnularCursoVigenteAlumno;
	}

	public boolean isOpEditarCursoVigenteAlumno() {
		return opEditarCursoVigenteAlumno;
	}

	public void setOpEditarCursoVigenteAlumno(boolean opEditarCursoVigenteAlumno) {
		this.opEditarCursoVigenteAlumno = opEditarCursoVigenteAlumno;
	}

	public FinderModel getProveedorFinder() {
		return proveedorFinder;
	}

	public void setProveedorFinder(FinderModel proveedorFinder) {
		this.proveedorFinder = proveedorFinder;
	}

	public Date getIncrementoFecha() {
		return incrementoFecha;
	}

	public void setIncrementoFecha(Date incrementoFecha) {
		this.incrementoFecha = incrementoFecha;
	}

	public double getIncremento() {
		return incremento;
	}

	public void setIncremento(double incremento) {
		this.incremento = incremento;
	}

	public FinderModel getEvaluacionTipoFinder() {
		return evaluacionTipoFinder;
	}

	public void setEvaluacionTipoFinder(FinderModel evaluacionTipoFinder) {
		this.evaluacionTipoFinder = evaluacionTipoFinder;
	}

	public Tipo getEvaluacionTipoSelected() {
		return evaluacionTipoSelected;
	}

	public void setEvaluacionTipoSelected(Tipo evaluacionTipoSelected) {
		this.evaluacionTipoSelected = evaluacionTipoSelected;
	}

	
	

}
