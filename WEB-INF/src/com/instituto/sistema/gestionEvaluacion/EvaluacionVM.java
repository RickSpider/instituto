package com.instituto.sistema.gestionEvaluacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Messagebox;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Rol;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.UsuarioRol;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.CursoVigenteMateria;
import com.instituto.modelo.Escala;
import com.instituto.modelo.EscalaDetalle;
import com.instituto.modelo.Evaluacion;
import com.instituto.modelo.EvaluacionDetalle;
import com.instituto.modelo.Proveedor;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;
import com.instituto.util.component.FinderModeloModel;

public class EvaluacionVM extends TemplateViewModelLocal {

	private List<Object[]> lEvaluaciones;
	private List<Object[]> lEvaluacionesOri;
	
	private Evaluacion evaluacionSelected;
	private Escala escalaSelected;
	private Proveedor docenteSelected;
	
	private boolean rolDocente = false;

	private boolean opCrearEvaluacion;
	private boolean opEditarEvaluacion;
	private boolean opBorrarEvaluacion;
	
	private boolean agregarEditar =false;
	
	private boolean visibleDocente;

	@Init(superclass = true)
	public void initEvaluacionVM() {

		cargarEvaluaciones();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeEvaluacionVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearEvaluacion = this.operacionHabilitada(ParamsLocal.OP_CREAR_EVALUACION);
		this.opEditarEvaluacion = this.operacionHabilitada(ParamsLocal.OP_EDITAR_EVALUACION);
		this.opBorrarEvaluacion = this.operacionHabilitada(ParamsLocal.OP_BORRAR_EVALUACION);

	}

	private void cargarEvaluaciones() {
		
		String sql = this.um.getSql("evaluacion/listaEvaluacion.sql");
		
		this.rolDocente = this.isUserRolDocente();
		
		if (this.rolDocente) {
			
			this.docenteSelected = this.reg.getObjectByCondicion(Proveedor.class.getName(), "usuarioid = "+this.getCurrentUser().getUsuarioid());
			
			sql = sql.replace("--1", "").replace("?1", docenteSelected.getProveedorid()+"\n");
			
		}


		this.lEvaluaciones = this.reg.sqlNativo(sql);
		this.lEvaluacionesOri = this.lEvaluaciones;
	}
	
	protected boolean isUserRolDocente() {
		
		Rol r = this.reg.getObjectByColumnString(Rol.class.getName(), "rol", "Docente");
		
		UsuarioRol ur = this.reg.getObjectByCondicion(UsuarioRol.class.getName(), "usuarioid = "+this.getCurrentUser().getUsuarioid()+" AND rolid = "+r.getRolid());
		
		if (ur == null) {
			
			return false;
		}
		
		return true;
		
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
	@NotifyChange("lEvaluacion")
	public void filtrarEvaluacion() {

		this.lEvaluaciones = this.filtrarListaObject(this.filtroColumns, this.lEvaluacionesOri);

	}

	// fin seccion
	
	//private Window modal;
	private boolean editar = false;
	
	@Command
	@NotifyChange("*")
	public void agregarEvaluacionAgregar() {

		if (!this.opCrearEvaluacion)
			return;
		
		if (!this.rolDocente) {
			
			this.docenteSelected = null;
			
		}
		
		this.evaluacionSelected = new Evaluacion();
		this.escalaSelected = null;

		this.editar = false;
		editarEvaluacion(-1);

	}


	@Command
	@NotifyChange("*")
	public void editarEvaluacion(@BindingParam("evaluacionid") long evaluacionid) {

		if (evaluacionid != -1) {

			if(!this.opEditarEvaluacion)
				return;
			
			this.evaluacionSelected = this.reg.getObjectById(Evaluacion.class.getName(), evaluacionid);
			this.editar = true;

		} else {
			
			
			
			if (Objects.isNull(this.docenteSelected)) {
				
				this.visibleDocente = true;
				
			}else {
				
				this.visibleDocente = false;
				
			}
			
			evaluacionSelected = new Evaluacion();

			
			//Proveedor docente = this.reg.getObjectById(Proveedor.class.getName(), 15);
			this.evaluacionSelected.setDocente(this.docenteSelected);
		}
		
		this.agregarEditar = true;
		
		this.inicializarFinders();

		/*modal = (Window) Executions.createComponents("/instituto/zul/gestionEvaluacion/evaluacionModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();*/

	}
	
	
	
	@Command
	@NotifyChange("evaluacionSelected")
	public void agregarDetalle() {
		
		EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
		evaluacionDetalle.setEvaluacion(this.evaluacionSelected);
		
		this.evaluacionSelected.getDetalles().add(evaluacionDetalle);
		
		
	}
	
	@Command
	@NotifyChange("lEvaluaciones")
	public void guardar() {
		

		this.save(evaluacionSelected);

		this.cargarEvaluaciones();

		//this.modal.detach();
		
		if (editar) {
			
			Notification.show("La Evaluacion fue Actualizada.");
			this.editar = false;
		}else {
			
			Notification.show("La Evaluacion fue agregada.");
		}
		
		this.evaluacionSelected = null;
		
		

	}

	
	//fin modal
	
	@Command
	public void borrarEvaluacionConfirmacion(@BindingParam("evaluacionid") final long evaluacionid) {

		if (!this.opBorrarEvaluacion)
			return;

		Evaluacion s = this.reg.getObjectById(Evaluacion.class.getName(), evaluacionid);

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarEvaluacion(s);

				}

			}

		};

		this.mensajeEliminar("La Evaluacion sera eliminada. \n Continuar?", event);
	}

	private void borrarEvaluacion(Evaluacion evaluacion) {

		this.reg.deleteObject(evaluacion);

		this.cargarEvaluaciones();

		BindUtils.postNotifyChange(null, null, this, "lEvaluaciones");

	}
	
	
	@Command
	@NotifyChange("evaluacionSelected")
	public void borrarDetalle (@BindingParam("detalle") EvaluacionDetalle detalle) {
		
		this.evaluacionSelected.getDetalles().remove(detalle);
		
	}
	
	//seccion finder
	
	
	private FinderModel evaluacionTipoFinder;
	
	private FinderModel docenteFinder;
	
	private FinderModeloModel<CursoVigenteMateria> cursoVigenteMateriaFinder;
	
	@NotifyChange("*")
	public void inicializarFinders() {
		
		String sqlEvaluacionTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_EVALUACION);
		
		//System.out.println(sqlEvaluacionTipo);
		
		this.evaluacionTipoFinder = new FinderModel("evaluacionTipo", sqlEvaluacionTipo);

		Tipo docenteTipo = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_PROVEEDOR_DOCENTE);
		String sqlDocente = this.um.getSql("proveedor/buscarProveedorSedeTipo.sql").replace("?1", this.getCurrentSede().getSedeid()+"" ).replace("?2",docenteTipo.getTipoid()+"" );
		this.docenteFinder = new FinderModel("docente", sqlDocente);
		
		
		
		if (!Objects.isNull(this.docenteSelected)) {
			
			//Tipo estado = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_ESTAOD_CV_MATERIA_PROCESO);
			
			this.cursoVigenteMateriaFinder = new FinderModeloModel<CursoVigenteMateria>("CursoVigenteMateria", this.reg,
					CursoVigenteMateria.class.getName(), "proveedorid = "+this.evaluacionSelected.getDocente().getProveedorid());
		}
		
		
		
	}
	
	public void generarFinders(@BindingParam("finder") String finder) {

		
		

		if (finder.compareTo(this.docenteFinder.getNameFinder()) == 0) {

			this.docenteFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.docenteFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.cursoVigenteMateriaFinder.getNameFinder()) == 0) {

			this.cursoVigenteMateriaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cursoVigenteMateriaFinder, "listFinderModelo");

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
		
		if (finder.compareTo(this.docenteFinder.getNameFinder()) == 0) {

			this.docenteFinder.setListFinder(this.filtrarListaObject(filter, this.docenteFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.docenteFinder, "listFinder");

			return;

		}

		if (finder.compareTo(this.evaluacionTipoFinder.getNameFinder()) == 0) {

			this.evaluacionTipoFinder.setListFinder(this.filtrarListaObject(filter, this.evaluacionTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.evaluacionTipoFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.cursoVigenteMateriaFinder.getNameFinder()) == 0) {

			this.cursoVigenteMateriaFinder.setListFinderModelo(this.filtrarLT(filter, this.cursoVigenteMateriaFinder.getListFinderModeloOri()));
			BindUtils.postNotifyChange(null, null, this.cursoVigenteMateriaFinder, "listFinderModelo");

			return;

		}
		
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.evaluacionTipoFinder.getNameFinder()) == 0) {
			
			Tipo tipo = this.reg.getObjectById(Tipo.class.getName(), id);
			
			this.evaluacionSelected.setEvaluacionTipo(tipo);
			
			
			if (!this.verificarDatos()) {
				
				return;
			}
			
			
			if (this.evaluacionSelected.getCursoVigenteMateria() != null) {
				
				this.cargarAlumnos(tipo, this.evaluacionSelected.getCursoVigenteMateria());
				
			}

			return;
		}
		
		if (finder.compareTo(this.docenteFinder.getNameFinder()) == 0) {
			
			this.docenteSelected = this.reg.getObjectById(Proveedor.class.getName(), id);
			this.evaluacionSelected.setDocente(docenteSelected);
			
			this.cursoVigenteMateriaFinder = new FinderModeloModel<CursoVigenteMateria>("CursoVigenteMateria", this.reg,
					CursoVigenteMateria.class.getName(), "proveedorid = "+this.evaluacionSelected.getDocente().getProveedorid());
			
			this.evaluacionSelected.setCursoVigenteMateria(null);
			this.evaluacionSelected.setEvaluacionTipo(null);
			
			return;
		}
		
	}
	
	
	public void cargarAlumnos(Tipo tipo, CursoVigenteMateria cursoVigenteMateria) {
		
		this.evaluacionSelected.getDetalles().clear();
		
		if (tipo.getSigla().compareTo(ParamsLocal.SIGLA_EVALUACION_ORDINARIO) == 0) {
			
			List<CursoVigenteAlumno> lcva = this.reg.getAllObjectsByCondicionOrder(CursoVigenteAlumno.class.getName(),
					"cursoVigenteId = "+cursoVigenteMateria.getCursoVigente().getCursovigenteid(), "cursovigentealumnopk.alumno.persona.apellido asc");
		
			for (CursoVigenteAlumno cva :  lcva) {
				
				EvaluacionDetalle ev = new EvaluacionDetalle ();
				
				ev.setEvaluacion(this.evaluacionSelected);
				ev.setAlumno(cva.getAlumno());
				
				
				this.evaluacionSelected.getDetalles().add(ev);				
				
			}
			
			
		}else if(tipo.getSigla().compareTo(ParamsLocal.SIGLA_EVALUACION_COMPLEMENTARIO) == 0) {
			
			this.evaluacionSelected.setDetalles(generarEvalucionDetalles(ParamsLocal.SIGLA_EVALUACION_ORDINARIO));
			
			
		}else if (tipo.getSigla().compareTo(ParamsLocal.SIGLA_EVALUACION_REGULARIZACION) ==0 ) {
			
			this.evaluacionSelected.setDetalles(generarEvalucionDetalles(ParamsLocal.SIGLA_EVALUACION_COMPLEMENTARIO));
			
		}
		
		this.evaluacionSelected = this.save(this.evaluacionSelected);

	}
	
	private List<EvaluacionDetalle> generarEvalucionDetalles(String evaluacionTipo){
		
		Evaluacion evAnterior = buscarEvaluacion(evaluacionTipo);
		
		List<EvaluacionDetalle> evDetalle = new ArrayList<EvaluacionDetalle>();
		
		for (EvaluacionDetalle edx : evAnterior.getDetalles()) {
			
			if (edx.getCalificacion() <= 1) {
				
				EvaluacionDetalle ed = new EvaluacionDetalle();
				ed.setEvaluacion(this.evaluacionSelected);
				ed.setAlumno(edx.getAlumno());
				
				evDetalle.add(ed);
			}
			
		}
		
		return evDetalle;
		
	}
	
	
	private Evaluacion buscarEvaluacion (String evaluacionTipo) {
		
		 Tipo evTipo = this.reg.getObjectBySigla(Tipo.class.getName(), evaluacionTipo);
		
		Long cursoVigenteId = this.evaluacionSelected.getCursoVigenteMateria().getCursoVigente().getCursovigenteid();
		Long materiaId = this.evaluacionSelected.getCursoVigenteMateria().getMateria().getMateriaid();
		Long evaluacionTipoId = evTipo.getTipoid();

		// Verifica si ya existe una evaluación con los mismos parámetros
		Evaluacion ev = this.reg.getObjectByCondicion(Evaluacion.class.getName(), 
		"cursovigenteid = " + cursoVigenteId + 
		" and materiaid = " + materiaId + 
		" and evaluaciontipoid = " + evaluacionTipoId);
		
		return ev;
		
	}
	
	
	
	private boolean verificarDatos() {
	    if (Objects.isNull(this.evaluacionSelected.getEvaluacionTipo()) || 
	        Objects.isNull(this.evaluacionSelected.getCursoVigenteMateria())) {
	        return true;
	    }

	    Long cursoVigenteId = this.evaluacionSelected.getCursoVigenteMateria().getCursoVigente().getCursovigenteid();
	    Long materiaId = this.evaluacionSelected.getCursoVigenteMateria().getMateria().getMateriaid();
	    Long evaluacionTipoId = this.evaluacionSelected.getEvaluacionTipo().getTipoid();

	    // Verifica si ya existe una evaluación con los mismos parámetros
	    Evaluacion ev = this.reg.getObjectByCondicion(Evaluacion.class.getName(), 
	        "cursovigenteid = " + cursoVigenteId + 
	        " and materiaid = " + materiaId + 
	        " and evaluaciontipoid = " + evaluacionTipoId);

	    if (Objects.nonNull(ev)) {
	        this.mensajeError("Ya existe una evaluación para este curso y materia en este tipo de evaluación.");
	        limpiarSeleccion();
	        return false;
	    }

	    String siglaEvaluacion = this.evaluacionSelected.getEvaluacionTipo().getSigla();

	    if (siglaEvaluacion.equals(ParamsLocal.SIGLA_EVALUACION_COMPLEMENTARIO)) {
	        return verificarEvaluacionPrevia(ParamsLocal.SIGLA_EVALUACION_ORDINARIO, "La evaluación Ordinaria no está finalizada.");
	    }

	    if (siglaEvaluacion.equals(ParamsLocal.SIGLA_EVALUACION_REGULARIZACION)) {
	        return verificarEvaluacionPrevia(ParamsLocal.SIGLA_EVALUACION_COMPLEMENTARIO, "La evaluación Complementaria no está finalizada.");
	    }

	    return true;
	}

	private boolean verificarEvaluacionPrevia(String siglaTipoPrevio, String mensajeError) {
	    Long cursoVigenteId = this.evaluacionSelected.getCursoVigenteMateria().getCursoVigente().getCursovigenteid();
	    Long materiaId = this.evaluacionSelected.getCursoVigenteMateria().getMateria().getMateriaid();

	    Tipo evTipo = this.reg.getObjectBySigla(Tipo.class.getName(), siglaTipoPrevio);
	    Evaluacion evPrevio = this.reg.getObjectByCondicion(Evaluacion.class.getName(), 
	        "cursovigenteid = " + cursoVigenteId + 
	        " and materiaid = " + materiaId + 
	        " and evaluaciontipoid = " + evTipo.getTipoid());

	    if (Objects.isNull(evPrevio)) {
	        this.mensajeError("No existe " + siglaTipoPrevio + " para este curso.");
	        limpiarSeleccion();
	        return false;
	    }

	    if (!evPrevio.isFinalizado()) {
	        this.mensajeError(mensajeError);
	        limpiarSeleccion();
	        return false;
	    }

	    return true;
	}

	private void limpiarSeleccion() {
	    this.evaluacionSelected.setCursoVigenteMateria(null);
	    this.evaluacionSelected.setEvaluacionTipo(null);
	}
	

	
	// seccion finderModelo
	@Command
	@NotifyChange("*")
	public void onSelectetItemModeloFinder(@BindingParam("dato") Object dato, @BindingParam("finder") String finder) {

			if (finder.compareTo(this.cursoVigenteMateriaFinder.getNameFinder()) == 0) {

				this.evaluacionSelected.setCursoVigenteMateria((CursoVigenteMateria) dato);
				
				
				if (!this.verificarDatos()) {
					
					return;
				}
				
				
				if (this.evaluacionSelected.getEvaluacionTipo() != null) {
					
					this.cargarAlumnos(this.evaluacionSelected.getEvaluacionTipo(), this.evaluacionSelected.getCursoVigenteMateria());
					
				}

				return;

			}

		}
	
	
	@Command 
	public void verificarTotales() {
		
		if (Objects.isNull(this.evaluacionSelected.getCursoVigenteMateria().getCursoVigente().getCurso().getEscala())) {
			
			this.mensajeError("El curso no tiene escala cargada.");
			return;
		}
		
		double limiteSuperior = this.evaluacionSelected.getCursoVigenteMateria().getCursoVigente().getCurso().getEscala().getLimiteSuperior();
		
		double totalPuntaje = this.evaluacionSelected.getTotalProceso1()+
				this.evaluacionSelected.getTotalProceso2()+
				this.evaluacionSelected.getTotalProceso3()+
				this.evaluacionSelected.getTotalProceso4()+
				this.evaluacionSelected.getTotalProceso5()+
				this.evaluacionSelected.getTotalfinal();
				
		
		if (totalPuntaje > limiteSuperior) {
			
			this.mensajeInfo("La sumatoria de puntajes no debe superar 100 puntos");
			return;
		}
		
		this.save(this.evaluacionSelected);
		BindUtils.postNotifyChange(null, null, this.evaluacionSelected, "detalles");
		
	}
	
	
	@Command
	public void checkVal(@BindingParam("detalle" ) EvaluacionDetalle detalle, @BindingParam("cmp") Doublebox cmp, @BindingParam("dato") Double dato, @BindingParam("max") Double max) {
		
		if (dato > max) {
         	
			 Clients.showNotification("El valor minimo es 0 y el maximo "+max+".", "error", cmp, "end_center", 2000);
			 cmp.focus();			 
			 
			
        }else {
        	
        	double acumulado = detalle.getProceso1()+	
    				detalle.getProceso2()+
    				detalle.getProceso3()+
    				detalle.getProceso4()+
    				detalle.getProceso5()+
    				detalle.getPfinal();
        	
        	detalle.setCalificacion(calcularCalificacion(acumulado));
        	
        	this.save(detalle);
        	
        	
        	BindUtils.postNotifyChange(null, null,detalle, "calificacion");
        }
		
		

	}
	
	private Double calcularCalificacion(double acumulado) {
		
		if (Objects.isNull(this.escalaSelected)) {
			
			this.escalaSelected =  this.reg.getObjectById(Escala.class.getName(), this.evaluacionSelected.getCursoVigenteMateria().getCursoVigente().getCurso().getEscala().getEscalaid());
			
			if (Objects.isNull(this.escalaSelected)) {
				
				this.mensajeError("El curos no tiene escala cargada;");
				
				return null;
				
			}
			
		}

		double calificacion = 1;
		
		for (EscalaDetalle ed : this.escalaSelected.getDetalles()) {
			
			if (acumulado >= ed.getLimiteInferior() && acumulado <= ed.getLimiteSuperior()) {
				
				
				calificacion = ed.getNota();
				
				break;
				
			}
			
		}
		
		
		return calificacion;
		
	}

	@Command
	public void borrarPuntaje(@BindingParam("detalle" ) EvaluacionDetalle detalle) {
		
		detalle.setProceso1(0);
		detalle.setProceso2(0);
		detalle.setProceso3(0);
		detalle.setProceso4(0);
		detalle.setProceso5(0);
		detalle.setPfinal(0);
		detalle.setCalificacion(0);
		
		
		this.save(detalle);
		
		BindUtils.postNotifyChange(null, null,detalle, "*");
		
		
	}
	
	@NotifyChange("*")
	@Command
	public void volverLista() {
		
		if (!Objects.isNull(this.evaluacionSelected.getEvaluacionTipo()) && !Objects.isNull(this.evaluacionSelected.getCursoVigenteMateria())) {
			
			this.save(this.evaluacionSelected);

		}
		
		
		this.evaluacionSelected = null;
		
		this.agregarEditar = false;
		
		this.cargarEvaluaciones();
		
	}
	
	@Command
	public void finalizarEvaluacionConfirmacion() {

		/*if (!this.opBorrarEvaluacion)
			return;
*/
		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					finalizarEvaluacion();

				}

			}

		};

		this.mensajeEliminar("La Evaluacion finalizara, luego de esto ya no se podra modificar. \n Continuar?", event);
	}
	
	
	
	public void finalizarEvaluacion() {
		
		if (!Objects.isNull(this.evaluacionSelected.getEvaluacionTipo()) && !Objects.isNull(this.evaluacionSelected.getCursoVigenteMateria())) {
			
			this.evaluacionSelected.setFinalizadoDocente(true);
			this.evaluacionSelected.setFinalizado(true);
			
			this.save(this.evaluacionSelected);

		}
		
		
		this.evaluacionSelected = null;
		
		this.agregarEditar = false;
		
		this.cargarEvaluaciones();
		
		BindUtils.postNotifyChange(null, null,this, "*");
		
		
	}
	
	
	@Command
	public void finalizarEvaluacionConfirmacionDocente() {

		/*if (!this.opBorrarEvaluacion)
			return;
*/
		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					finalizarEvaluacionDocente();

				}

			}

		};

		this.mensajeEliminar("La Evaluacion finalizara, luego de esto ya no se podra modificar. \n Continuar?", event);
	}
	
	public void finalizarEvaluacionDocente() {
		
		if (!Objects.isNull(this.evaluacionSelected.getEvaluacionTipo()) && !Objects.isNull(this.evaluacionSelected.getCursoVigenteMateria())) {
			
			this.evaluacionSelected.setFinalizadoDocente(true);
			
			this.save(this.evaluacionSelected);

		}
		
		this.generarReporte(this.evaluacionSelected.getEvaluacionid());
		
		this.evaluacionSelected = null;
		
		this.agregarEditar = false;
		
		this.cargarEvaluaciones();
		
		BindUtils.postNotifyChange(null, null,this, "*");
		
		
	}
	
	public Double getNota() {
		
		return 0.0;
				
		
	}
	
	
	
	public boolean isDisabledCab() {
		
		if (!Objects.isNull(this.evaluacionSelected)) {
			
			if (this.evaluacionSelected.isFinalizado()) {
				
				return true;
				
			}if (this.evaluacionSelected.isFinalizadoDocente() && this.rolDocente) {
				
				return true;
				
			}
			
		}
		
		
		
		if (this.editar) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	public boolean isDisabledDet() {
		
		if (!Objects.isNull(this.evaluacionSelected)) {
		
			if (this.evaluacionSelected.isFinalizado()) {
				
				return true;
				
			}else if (this.evaluacionSelected.isFinalizadoDocente() && this.rolDocente) {
				
				return true;
				
			}
		}
		
		return false;
		
	}
	
	@Command
	public void generarReporte(@BindingParam("evaluacionid") Long evaluacionid) {
		
		Evaluacion ev = this.reg.getObjectById(Evaluacion.class.getCanonicalName(), evaluacionid);
		
		if (ev.isFinalizadoDocente()) {
			
			Executions.getCurrent().sendRedirect(
					"/instituto/zul/gestionEvaluacion/evaluacionReporte.zul?id=" + evaluacionid,
					"_blank");
			
		}else {
			
			this.mensajeInfo("Debe de estar finalizada la evaluacion para imprimir el reporte.");
			
		}

		
		
	}

	public List<Object[]> getlEvaluaciones() {
		return lEvaluaciones;
	}

	public void setlEvaluaciones(List<Object[]> lEvaluaciones) {
		this.lEvaluaciones = lEvaluaciones;
	}

	public Evaluacion getEvaluacionSelected() {
		return evaluacionSelected;
	}

	public void setEvaluacionSelected(Evaluacion evaluacionSelected) {
		this.evaluacionSelected = evaluacionSelected;
	}

	public boolean isOpCrearEvaluacion() {
		return opCrearEvaluacion;
	}

	public void setOpCrearEvaluacion(boolean opCrearEvaluacion) {
		this.opCrearEvaluacion = opCrearEvaluacion;
	}

	public boolean isOpEditarEvaluacion() {
		return opEditarEvaluacion;
	}

	public void setOpEditarEvaluacion(boolean opEditarEvaluacion) {
		this.opEditarEvaluacion = opEditarEvaluacion;
	}

	public boolean isOpBorrarEvaluacion() {
		return opBorrarEvaluacion;
	}

	public void setOpBorrarEvaluacion(boolean opBorrarEvaluacion) {
		this.opBorrarEvaluacion = opBorrarEvaluacion;
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

	public boolean isAgregarEditar() {
		return agregarEditar;
	}

	public void setAgregarEditar(boolean agregarEditar) {
		this.agregarEditar = agregarEditar;
	}

	public FinderModel getEvaluacionTipoFinder() {
		return evaluacionTipoFinder;
	}

	public void setEvaluacionTipoFinder(FinderModel evaluacionTipoFinder) {
		this.evaluacionTipoFinder = evaluacionTipoFinder;
	}

	public FinderModeloModel<CursoVigenteMateria> getCursoVigenteMateriaFinder() {
		return cursoVigenteMateriaFinder;
	}

	public void setCursoVigenteMateriaFinder(FinderModeloModel<CursoVigenteMateria> cursoVigenteMateriaFinder) {
		this.cursoVigenteMateriaFinder = cursoVigenteMateriaFinder;
	}

	public FinderModel getDocenteFinder() {
		return docenteFinder;
	}

	public void setDocenteFinder(FinderModel docenteFinder) {
		this.docenteFinder = docenteFinder;
	}

	public boolean isVisibleDocente() {
		return visibleDocente;
	}

	public void setVisibleDocente(boolean visibleDocente) {
		this.visibleDocente = visibleDocente;
	}

	public boolean isRolDocente() {
		return rolDocente;
	}

	public void setRolDocente(boolean rolDocente) {
		this.rolDocente = rolDocente;
	}


}
