package com.instituto.sistema.administracion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.annotation.Command;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.modelo.Tipo;
import com.instituto.modelo.CVADocumento;
import com.instituto.modelo.CVADocumentoDetalle;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.serchModel.CursoVigenteSearchModel;
import com.instituto.serchModel.TipoSearchModel;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class DocumentacionVM extends TemplateViewModelLocal {
	
	private List<CVADocumento> lCVADocumentos;
	
//	private AlumnoSearchModel alumnoSearchModelSelected;
//	private ListModelArray<AlumnoSearchModel> lAlumnoSearchModel;
	
	private CursoVigenteSearchModel cursoVigenteSearchModelSelected;
	private ListModelArray<CursoVigenteSearchModel> lCursoVigenteSearchModel;
	
	private ListModelArray<TipoSearchModel> lDocumentoEstadoSearchModel;
	private TipoSearchModel documentoEstadoSearchModelSelected;
	
	private List<Object[]> lAlumnos;
	private List<Object[]> lAlumnosOri;
	
	private boolean opEditarDocumentoDetalle;
	private boolean opBorrarDocumentoDetalle;
	
	private List<Object[]> conteoDocs;
	
	
	@Init(superclass = true)
	public void initDocumentacionVM() {
		
		this.generarSearchModel();
		
		inicializarFiltros();
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeDocumentacionVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opEditarDocumentoDetalle = this.operacionHabilitada(ParamsLocal.OP_EDITAR_DOCUMENTACIONDETALLE);
		this.opBorrarDocumentoDetalle = this.operacionHabilitada(ParamsLocal.OP_BORRAR_DOCUMENTACIONDETALLE);
		
	}

	
	private void generarSearchModel() {		
		
		this.lCursoVigenteSearchModel = crearSearchModel(
		        this.um.getSql("buscarCursoVigente.sql").replace("?1", this.getCurrentUsuarioSede().getSede().getSedeid()+"").replace("--1","").replace("?2", "true"),
		        ocv -> new CursoVigenteSearchModel(
		        		Long.parseLong(ocv[0].toString()),
		        		ocv[1].toString(), 
		        		ocv[2].toString(),
						ocv[3].toString(),
						""
					)
		    );
		
		
	}
	
	
	
	private <T> ListModelArray<T> crearSearchModel(String sql, java.util.function.Function<Object[], T> mapper) {
	    List<Object[]> resultados = this.reg.sqlNativo(sql);
	    List<T> lista = new ArrayList<>(resultados.size());

	    for (Object[] fila : resultados) {
	        lista.add(mapper.apply(fila));
	    }

	    ListModelArray<T> modelo = new ListModelArray<>(lista);
	    return modelo;
	}
	
	private Object[] alumnoObjectSelected;
	
	@NotifyChange("*")
	public void cargarAlumnos() {
		
		if (this.cursoVigenteSearchModelSelected == null) {
			this.lAlumnos = null;
			this.alumnoObjectSelected = null;
			this.lCVADocumentos = null;
			return;
		}
		
		this.lAlumnos =  this.reg.sqlNativo(this.um.getSql("cursoVigenteListaAlumnos.sql").replace("?1", this.cursoVigenteSearchModelSelected.getId()+""));
		this.lAlumnosOri = this.lAlumnos;
		
		this.calcularActivos();
	}
	

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[6]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}
	
	@Command
	@NotifyChange("lAlumnos")
	public void filtrarAlumno() {

		this.lAlumnos = this.filtrarListaObject(this.filtroColumns, this.lAlumnosOri);

	}
	
	@Command
	@NotifyChange("*")
	public void cargarDatos() {
		
		if (this.alumnoObjectSelected == null) {
			
			return;
			
		}
		
		this.lCVADocumentos = this.reg.getAllObjectsByCondicionOrder(CVADocumento.class.getName(), "alumnoid = "+alumnoObjectSelected[0].toString()+" and cursovigenteid= "+this.cursoVigenteSearchModelSelected.getId(), "cvadocumentoid asc");
		
		this.calcularActivos();
	}
	
	private Window modal;
	private CVADocumentoDetalle cvadocumentodetalleSelected;
	
	public void modalDocumentoDetalle(@BindingParam("documento") CVADocumento doc) {
		
		this.cvadocumentodetalleSelected = null;
		this.documentoEstadoSearchModelSelected = null;
		
		cargarTipoSM();
		
		this.cvadocumentodetalleSelected = new CVADocumentoDetalle();
		this.cvadocumentodetalleSelected.setCvadocumento(doc);
		this.cvadocumentodetalleSelected.setDocumentoEstadoTipo(doc.getDocumentoEstadoTipo());
		this.cvadocumentodetalleSelected.setFecha(new Date());
		
		modal = (Window) Executions.createComponents("/instituto/zul/administracion/documentoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}
	
	public void borrarPersonaConfirmacion(@BindingParam("detalle") CVADocumentoDetalle detalle) {

		if (!this.opBorrarDocumentoDetalle)
			return;

		
		
		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarDocumentacionDetalleDetalle(detalle);

				}

			}

		};

		this.mensajeEliminar("La persona sera eliminada. \n Continuar?", event);
	}
	
	public void borrarDocumentacionDetalleDetalle(CVADocumentoDetalle detalle) {
		
		this.reg.deleteObject(detalle);
		
		this.cargarDatos();
		
		BindUtils.postNotifyChange(null, null, this,"*");
		
	}
	
	
	public void editarDetalleModal(@BindingParam("detalle") CVADocumentoDetalle detalle) {
		
		this.cvadocumentodetalleSelected = detalle;
		this.documentoEstadoSearchModelSelected = new TipoSearchModel(detalle.getDocumentoEstadoTipo());
		
		cargarTipoSM(); 
		
		modal = (Window) Executions.createComponents("/instituto/zul/administracion/documentoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
		
	}
	
	private void cargarTipoSM() {
		
		this.lDocumentoEstadoSearchModel =  crearSearchModel(
				this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_DOCUMENTO_ESTADO),
		        ocv -> new TipoSearchModel(
		        		Long.parseLong(ocv[0].toString()),
		        		ocv[1].toString()
					)
		        );
		
	}
	
	@Command
	@NotifyChange("*")
	public void guardarDetalle() {
		
		if (this.documentoEstadoSearchModelSelected == null) {
			
			this.mensajeError("Debes Seleccionar un estado");
			return;
		}
		
		/*if (this.cvadocumentodetalleSelected.getObservacion() == null || this.cvadocumentodetalleSelected.getObservacion().length() <= 0) {
			
			this.mensajeError("Debes Cargar una observacion");
			
			return;
			
		}*/
		
		this.cvadocumentodetalleSelected.getCvadocumento().setDocumentoEstadoTipo(null);
		
		this.cvadocumentodetalleSelected.setDocumentoEstadoTipo(this.reg.getObjectById(Tipo.class.getName(), this.documentoEstadoSearchModelSelected.getId()));
		
		this.cvadocumentodetalleSelected.getCvadocumento().setDocumentoEstadoTipo(this.cvadocumentodetalleSelected.getDocumentoEstadoTipo());
		
		CVADocumento cvadoc = this.cvadocumentodetalleSelected.getCvadocumento();
		
		cvadoc.setDocumentoEstadoTipo(this.cvadocumentodetalleSelected.getDocumentoEstadoTipo());
		
		if (this.cvadocumentodetalleSelected.getDocumentoEstadoTipo().getSigla().compareTo(ParamsLocal.SIGLA_DOCUMENTO_ESTADO_ENTREGADO) == 0) {
			
			List<EstadoCuenta> lEstadoCuenta = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(), 
					"alumnoid = "+cvadoc.getAlumnoid().getAlumnoid()+" and cursovigenteid = "+cvadoc.getCursoVigente().getCursovigenteid()
					,null);
			
			for (EstadoCuenta x : lEstadoCuenta) {
				
				if (x.getSaldo() > 0 && !x.isInactivo()) {
					
					this.mensajeError("El alumno tiene pagos pendientes.");
					
					return;
					
				}
				
			}
			
			
		}
		
		this.save(this.cvadocumentodetalleSelected);

		this.save(cvadoc);
		
		this.cargarDatos();
		
		this.cvadocumentodetalleSelected = null;
		this.documentoEstadoSearchModelSelected = null;
		
		this.modal.detach();
		
	}
	
	public void calcularActivos() {
		
		String sql = this.um.getSql("cvaDocumento/conteoDocumentos.sql").replace("?1", this.cursoVigenteSearchModelSelected.getId()+"");
		
		this.conteoDocs = this.reg.sqlNativo(sql);
			
	}
	
	
	
	public CursoVigenteSearchModel getCursoVigenteSearchModelSelected() {
		return cursoVigenteSearchModelSelected;
	}

	public void setCursoVigenteSearchModelSelected(CursoVigenteSearchModel cursoVigenteSearchModelSelected) {
		this.cursoVigenteSearchModelSelected = cursoVigenteSearchModelSelected;
	}

	public ListModelArray<CursoVigenteSearchModel> getlCursoVigenteSearchModel() {
		return lCursoVigenteSearchModel;
	}

	public void setlCursoVigenteSearchModel(ListModelArray<CursoVigenteSearchModel> lCursoVigenteSearchModel) {
		this.lCursoVigenteSearchModel = lCursoVigenteSearchModel;
	}

	public List<CVADocumento> getlCVADocumentos() {
		return lCVADocumentos;
	}

	public void setlCVADocumentos(List<CVADocumento> lCVADocumentos) {
		this.lCVADocumentos = lCVADocumentos;
	}

	public ListModelArray<TipoSearchModel> getlDocumentoEstadoSearchModel() {
		return lDocumentoEstadoSearchModel;
	}

	public void setlDocumentoEstadoSearchModel(ListModelArray<TipoSearchModel> lDocumentoEstadoSearchModel) {
		this.lDocumentoEstadoSearchModel = lDocumentoEstadoSearchModel;
	}

	public TipoSearchModel getDocumentoEstadoSearchModelSelected() {
		return documentoEstadoSearchModelSelected;
	}

	public void setDocumentoEstadoSearchModelSelected(TipoSearchModel documentoEstadoSearchModelSelected) {
		this.documentoEstadoSearchModelSelected = documentoEstadoSearchModelSelected;
	}

	public CVADocumentoDetalle getCvadocumentodetalleSelected() {
		return cvadocumentodetalleSelected;
	}

	public void setCvadocumentodetalleSelected(CVADocumentoDetalle cvadocumentodetalleSelected) {
		this.cvadocumentodetalleSelected = cvadocumentodetalleSelected;
	}

	public List<Object[]> getlAlumnos() {
		return lAlumnos;
	}

	public void setlAlumnos(List<Object[]> lAlumnos) {
		this.lAlumnos = lAlumnos;
	}

	public Object[] getAlumnoObjectSelected() {
		return alumnoObjectSelected;
	}

	public void setAlumnoObjectSelected(Object[] alumnoObjectSelected) {
		this.alumnoObjectSelected = alumnoObjectSelected;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public boolean isOpEditarDocumentoDetalle() {
		return opEditarDocumentoDetalle;
	}

	public void setOpEditarDocumentoDetalle(boolean opEditarDocumentoDetalle) {
		this.opEditarDocumentoDetalle = opEditarDocumentoDetalle;
	}

	public boolean isOpBorrarDocumentoDetalle() {
		return opBorrarDocumentoDetalle;
	}

	public void setOpBorrarDocumentoDetalle(boolean opBorrarDocumentoDetalle) {
		this.opBorrarDocumentoDetalle = opBorrarDocumentoDetalle;
	}

	public List<Object[]> getConteoDocs() {
		return conteoDocs;
	}

	public void setConteoDocs(List<Object[]> conteoDocs) {
		this.conteoDocs = conteoDocs;
	}

	

	
	
	

}
