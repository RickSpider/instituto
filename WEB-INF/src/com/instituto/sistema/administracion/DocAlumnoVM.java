package com.instituto.sistema.administracion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.annotation.Command;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Window;

import com.doxacore.modelo.Tipo;
import com.instituto.modelo.CVADocumento;
import com.instituto.modelo.CVADocumentoDetalle;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.serchModel.AlumnoSearchModel;
import com.instituto.serchModel.CursoVigenteSearchModel;
import com.instituto.serchModel.TipoSearchModel;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class DocAlumnoVM extends TemplateViewModelLocal {
	
	private List<CVADocumento> lCVADocumentos;
	
	private AlumnoSearchModel alumnoSearchModelSelected;
	private ListModelArray<AlumnoSearchModel> lAlumnoSearchModel;
	
	private CursoVigenteSearchModel cursoVigenteSearchModelSelected;
	private ListModelArray<CursoVigenteSearchModel> lCursoVigenteSearchModel;
	
	private ListModelArray<TipoSearchModel> lDocumentoEstadoSearchModel;
	private TipoSearchModel documentoEstadoSearchModelSelected;
	
	
	@Init(superclass = true)
	public void initDocumentacionVM() {
		
		this.generarSearchModel();
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeDocumentacionVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub
		
	}

	
	private void generarSearchModel() {
		
		this.lAlumnoSearchModel = crearSearchModel(
		        this.um.getSql("buscarAlumno.sql").replace("?1", this.getCurrentSede().getSedeid()+""),
		        o -> new AlumnoSearchModel(
		        		Long.parseLong(o[0].toString()),
		        		o[2].toString()+", "+o[1].toString(),
		        		o[3].toString()		        		
		        )
		    );
		
		
	
		
	}
	
	@Command
	@NotifyChange("*")
	public void generarSearchModelCVA() {
		
		if (this.alumnoSearchModelSelected == null) {
			
			this.cursoVigenteSearchModelSelected = null;
			this.lCVADocumentos = null;
			this.lCursoVigenteSearchModel.clearSelection();
			this.lCursoVigenteSearchModel = null;
			return;
		}
		
		this.lCursoVigenteSearchModel = crearSearchModel(
		        this.um.getSql("buscarCursoVigentePorAlumno.sql").replace("?1", this.alumnoSearchModelSelected.getId()+"").replace("?2", "true").replace("--1", ""),
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
	
	@Command
	@NotifyChange("*")
	public void cargarDatos() {
		
		this.lCVADocumentos = this.reg.getAllObjectsByCondicionOrder(CVADocumento.class.getName(), "alumnoid = "+this.alumnoSearchModelSelected.getId()+" and cursovigenteid= "+this.cursoVigenteSearchModelSelected.getId(), "cvadocumentoid asc");
		
	}
	
	private Window modal;
	private CVADocumentoDetalle cvadocumentodetalleSelected;
	
	public void modalDocumentoDetalle(@BindingParam("documento") CVADocumento doc) {
		
		this.cvadocumentodetalleSelected = null;
		this.documentoEstadoSearchModelSelected = null;
		
		this.lDocumentoEstadoSearchModel =  crearSearchModel(
		this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_DOCUMENTO_ESTADO),
        ocv -> new TipoSearchModel(
        		Long.parseLong(ocv[0].toString()),
        		ocv[1].toString()
			)
        );
		
		this.cvadocumentodetalleSelected = new CVADocumentoDetalle();
		this.cvadocumentodetalleSelected.setCvadocumento(doc);
		this.cvadocumentodetalleSelected.setDocumentoEstadoTipo(doc.getDocumentoEstadoTipo());
		
		modal = (Window) Executions.createComponents("/instituto/zul/administracion/documentoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}
	
	@Command
	@NotifyChange("*")
	public void guardarDetalle() {
		
		if (this.documentoEstadoSearchModelSelected == null) {
			
			this.mensajeError("Debes Seleccionar un estado");
			return;
		}
		
		if (this.cvadocumentodetalleSelected.getObservacion() == null || this.cvadocumentodetalleSelected.getObservacion().length() <= 0) {
			
			this.mensajeError("Debes Cargar una observacion");
			
			return;
			
		}
		
		this.cvadocumentodetalleSelected.getCvadocumento().setDocumentoEstadoTipo(null);
		
		this.cvadocumentodetalleSelected.setDocumentoEstadoTipo(this.reg.getObjectById(Tipo.class.getName(), this.documentoEstadoSearchModelSelected.getId()));
		
		this.cvadocumentodetalleSelected.getCvadocumento().setDocumentoEstadoTipo(this.cvadocumentodetalleSelected.getDocumentoEstadoTipo());
		
		CVADocumento cvadoc = this.cvadocumentodetalleSelected.getCvadocumento();
		
		cvadoc.setDocumentoEstadoTipo(this.cvadocumentodetalleSelected.getDocumentoEstadoTipo());
		
		if (this.cvadocumentodetalleSelected.getDocumentoEstadoTipo().getSigla().compareTo(ParamsLocal.SIGLA_DOCUMENTO_ESTADO_ENTREGADO) == 0) {
			
			List<EstadoCuenta> lEstadoCuenta = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(), "alumnoid = "+cvadoc.getAlumnoid().getAlumnoid()+" and cursovigenteid = "+cvadoc.getCursoVigente().getCursovigenteid(),null);
			
			for (EstadoCuenta x : lEstadoCuenta) {
				
				if (x.getSaldo() > 0) {
					
					this.mensajeError("El alumno tiene pagos pendientes.");
					
					return;
					
				}
				
			}
			
			
		}
		
		this.save(this.cvadocumentodetalleSelected);

		this.save(cvadoc);
		
		this.cvadocumentodetalleSelected = null;
		this.documentoEstadoSearchModelSelected = null;
		
		this.modal.detach();
		
		this.cargarDatos();
		
		
	}
	

	public AlumnoSearchModel getAlumnoSearchModelSelected() {
		return alumnoSearchModelSelected;
	}

	public void setAlumnoSearchModelSelected(AlumnoSearchModel alumnoSearchModelSelected) {
		this.alumnoSearchModelSelected = alumnoSearchModelSelected;
	}

	public ListModelArray<AlumnoSearchModel> getlAlumnoSearchModel() {
		return lAlumnoSearchModel;
	}

	public void setlAlumnoSearchModel(ListModelArray<AlumnoSearchModel> lAlumnoSearchModel) {
		this.lAlumnoSearchModel = lAlumnoSearchModel;
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
	
	
	

}
