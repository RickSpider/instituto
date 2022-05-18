package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="cursosconceptos")
public class CursoConcepto extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4240125768886330055L;

	
	@EmbeddedId
	private CursoConceptoPK cursoconceptopk = new CursoConceptoPK();

	@Override
	public Object[] getArrayObjectDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Curso getCurso() {
		
		return this.cursoconceptopk.getCurso();
		
	}
	
	public void setCurso(Curso curso) {
		
		this.cursoconceptopk.setCurso(curso);
		
	}
	
	public Concepto getConcepto() {
		
		return this.cursoconceptopk.getConcepto();
		
	}
	
	public void setConcepto(Concepto concepto) {
		
		this.cursoconceptopk.setConcepto(concepto);
		
	}

	public CursoConceptoPK getCursoconceptopk() {
		return cursoconceptopk;
	}

	public void setCursoconceptopk(CursoConceptoPK cursoconceptopk) {
		this.cursoconceptopk = cursoconceptopk;
	}
	
	
	

}
