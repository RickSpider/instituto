package com.instituto.sistema.gestionEvaluacion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import com.doxacore.report.TemplateReportViewModel;
import com.instituto.modelo.Empresa;
import com.instituto.modelo.Evaluacion;



public class EvaluacionReporteVM extends TemplateReportViewModel {
	
	/*
	 * 
	 * 
	 * si acatualizas esta clase anada actualiza reporte/ReporteKudePDF.java
	 * 
	 * 
	 */
	
	@Init(superclass = true)
	public void initFacturaVM() {

		this.source += "evaluacionReport.jasper";

	}

	@AfterCompose(superclass = true)
	public void afterComposeFacturaVM() {

	}
	
	@Override
	protected String[] cargarColumas() {
		String[] columns = {"nro","Apellido", "Nombre", "CI", "P1", "P2", "P3", "P4", "P5", "PF","calificacion", "Letra"};
		
		 return columns;
	}

	@Override
	protected Map<String, Object> cargarParametros() {
		
		Evaluacion evaluacion = this.reg.getObjectById(Evaluacion.class.getName(), this.id);

		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);

		Map<String, Object> parameters = new HashMap<>();
		
		try {
			parameters.put("Logo", ImageIO.read(new ByteArrayInputStream(empresa.getLogo())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		parameters.put("Empresa", empresa.getNombreFantasia());
		parameters.put("Extra2", empresa.getExtra2());
		
		parameters.put("Sede", evaluacion.getCursoVigenteMateria().getCursoVigente().getSede().getSede());
		parameters.put("Fecha", new SimpleDateFormat("dd/MM/yyyy").format(evaluacion.getFecha()));
		parameters.put("Docente", evaluacion.getDocente().getPersona().getNombreCompleto());
		parameters.put("Curso", evaluacion.getCursoVigenteMateria().getCursoVigente().getCurso().getCurso());
		parameters.put("Materia", evaluacion.getCursoVigenteMateria().getMateria().getMateria());
		parameters.put("EvaluacionTipo", evaluacion.getEvaluacionTipo().getTipo());
		
		parameters.put("tproceso1", evaluacion.getTotalProceso1());
		parameters.put("tproceso2", evaluacion.getTotalProceso2());
		parameters.put("tproceso3", evaluacion.getTotalProceso3());
		parameters.put("tproceso4", evaluacion.getTotalProceso4());
		parameters.put("tproceso5", evaluacion.getTotalProceso5());
		parameters.put("tfinal", evaluacion.getTotalfinal());
		
		return parameters;
	}

	@Override
	protected List<Object[]> cargarDatos() {
		
		String evaluacionDetalle = this.um.getSql("evaluacion/evaluacionDetalle.sql").replace("?1", this.id+"");
		List<Object[]>datos = this.reg.sqlNativo(evaluacionDetalle);
		
		return datos;
	}

}
