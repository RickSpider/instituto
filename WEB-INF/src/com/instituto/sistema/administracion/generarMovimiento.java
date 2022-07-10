package com.instituto.sistema.administracion;

import java.util.List;

import com.instituto.modelo.Concepto;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.MovimientoCuenta;

public class generarMovimiento {
	
	public void generarMovimiento(List<CursoVigenteAlumno> lCursoVigenteAlumno, Concepto concepto) {
		
		
		if (concepto.getConcepto().compareTo("MATRICULA") == 0) {
			
			
		}
		
		if (concepto.getConcepto().compareTo("MODULO")==0) {
			
			movimientoCuota(lCursoVigenteAlumno, concepto);
			
		}
		
		
	}
	
	private void movimientoCuota(List<CursoVigenteAlumno> lCursoVigenteAlumno, Concepto concepto) {
		
		for (CursoVigenteAlumno cva : lCursoVigenteAlumno) {
			
			MovimientoCuenta mc = new MovimientoCuenta();
			
			mc.setAlumno(cva.getAlumno());
			mc.setConcepto(concepto);
			mc.setCursoVigente(cva.getCursoVigente());
			
			
			
		}
		
	}
	
	private void movimientoMatricula(List<CursoVigenteAlumno> lCursoVigenteAlumno, Concepto concepto) {
		
		
		
	}
	


}
