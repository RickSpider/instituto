package com.instituto.sistema.administracion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.doxacore.util.Register;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.CursoVigenteConcepto;
import com.instituto.modelo.CursoVigenteConvenio;
import com.instituto.modelo.Feriado;
import com.instituto.modelo.MovimientoCuenta;

public class GenerarMovimiento {
	
	private Register reg = new Register();

	public void generarMovimientoAlumno(CursoVigenteAlumno cva, List<CursoVigenteConcepto> lCVConceptos,
			List<CursoVigenteConvenio> lCVConvenios) {
		
		List<Feriado> lFeriados = this.reg.getAllObjects(Feriado.class.getName());
		
		String sql = "select \n" + 
				"cvm.orden,\n" + 
				"count(cvm.orden) as cantidadMaterias\n" + 
				"from cursosvigentesmaterias cvm\n" + 
				"left join materias m on m.materiaid = cvm.materiaid\n" + 
				"left join tipos t on t.tipoid = m.materiatipoid\n" + 
				"where \n" + 
				"cursovigenteid = "+cva.getCursoVigente().getCursovigenteid()+" and \n" + 
				"t.sigla = 'MATERIA_AULA'\n" + 
				"group by cvm.orden";
		
		List<Object []> lOrdenMaterias = this.reg.sqlNativo(sql);

		ArrayList<MovimientoCuenta> out = new ArrayList<MovimientoCuenta>();

		for (CursoVigenteConcepto x : lCVConceptos) {

			boolean esIgual = false;

			if (x.getConcepto().getConcepto().compareTo("MATRICULA") == 0) {

				esIgual = true;
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(cva.getCursoVigente().getFechaFin());
				int diaVencimiento = calendar.get(Calendar.DAY_OF_MONTH);

				int periodoEducativo = cva.getCursoVigente().getCurso().getPeriodoEducativo();
				
				int cantMeses = lOrdenMaterias.size() / periodoEducativo;

				for (int i = 0; i < periodoEducativo; i++) {

					MovimientoCuenta mc = new MovimientoCuenta();
					mc.setAlumno(cva.getAlumno());
					mc.setConcepto(x.getConcepto());
					mc.setPeriodo(i + 1);
					mc.setMonto(x.getImporte());
					
					calendar.set(Calendar.DAY_OF_MONTH, diaVencimiento);
					calendar = calculoVencimiento(calendar, lFeriados);
					
					mc.setVencimiento(calendar.getTime());
					
					out.add(mc);
					
					calendar.add(Calendar.MONTH, cantMeses);
					
					out.add(mc);

				}

			}

			if (x.getConcepto().getConcepto().compareTo("MODULO") == 0) {

				esIgual = true;
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(cva.getCursoVigente().getFechaFin());
				int diaVencimiento = calendar.get(Calendar.DAY_OF_MONTH);
			
				for (int i = 0; i < lOrdenMaterias.size(); i++) {
					
					int cantidadMaterias = Integer.parseInt(lOrdenMaterias.get(i)[1].toString()); 

					MovimientoCuenta mc = new MovimientoCuenta();
					mc.setAlumno(cva.getAlumno());
					mc.setConcepto(x.getConcepto());
					mc.setPeriodo(i + 1);
					mc.setMonto(x.getImporte()*cantidadMaterias); 
					
					calendar.set(Calendar.DAY_OF_MONTH, diaVencimiento);
					calendar = calculoVencimiento(calendar, lFeriados);
					
					mc.setVencimiento(calendar.getTime());
					
					out.add(mc);
					
					calendar.add(Calendar.MONTH, 1);

				}


			}

			if (!esIgual) {
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(cva.getCursoVigente().getFechaFin());	
				int diaVencimiento = calendar.get(Calendar.DAY_OF_MONTH);
				
				
				MovimientoCuenta mc = new MovimientoCuenta();
				mc.setAlumno(cva.getAlumno());
				mc.setConcepto(x.getConcepto());
				mc.setPeriodo(1);
				mc.setMonto(x.getImporte()); 
				
				calendar.set(Calendar.DAY_OF_MONTH, diaVencimiento);
				calendar = calculoVencimiento(calendar, lFeriados);
				
				mc.setVencimiento(calendar.getTime());
				
				out.add(mc);
				
				calendar.add(Calendar.MONTH, 1);
				
			}

		}
		

	}
	
	private Calendar calculoVencimiento(Calendar calendar, List<Feriado> lFeriados) {
		
		
		if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
			
			calendar.add(Calendar.MONTH, 1);
			
		}
		
		
		for (int i = 0 ; i< lFeriados.size(); i++) {
			
			if(calendar.get(Calendar.MONTH) == lFeriados.get(i).getMes() &&
					calendar.get(Calendar.DAY_OF_MONTH) == lFeriados.get(i).getDia()){
				
				calendar.add(Calendar.DAY_OF_WEEK, 1);
				
				i=0;
				
			} 
			
		} 
		
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			
			calendar.add(Calendar.DAY_OF_WEEK, 1);
			
		}

		
		return calendar;
	}

}
