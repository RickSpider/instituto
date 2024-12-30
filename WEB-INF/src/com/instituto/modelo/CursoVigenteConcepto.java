package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="cursosvigentesconceptos")
public class CursoVigenteConcepto extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3496137868436220906L;
	
	@EmbeddedId
	private CursoVigenteConceptoPK cursovigenteconceptopk = new CursoVigenteConceptoPK();
	
	private double importe;
	
	@Override
	public Object[] getArrayObjectDatos() {
		Object [] o = {this.getConcepto().getConcepto(), this.importe};
		return o;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public CursoVigente getCursoVigente() {
		
		return this.cursovigenteconceptopk.getCursoVigente();
		
	}
	
	public void setCursoVigente(CursoVigente cursovigente) {
		
		this.cursovigenteconceptopk.setCursoVigente(cursovigente);
		
	}
	
	public Concepto getConcepto() {
		
		return this.cursovigenteconceptopk.getConcepto();
		
	}
	
	public void setConcepto(Concepto concepto) {
		
		this.cursovigenteconceptopk.setConcepto(concepto);
		
	}

	public CursoVigenteConceptoPK getCursoconceptopk() {
		return cursovigenteconceptopk;
	}

	public void setCursoconceptopk(CursoVigenteConceptoPK cursoconceptopk) {
		this.cursovigenteconceptopk = cursoconceptopk;
	}

	public CursoVigenteConceptoPK getCursovigenteconceptopk() {
		return cursovigenteconceptopk;
	}

	public void setCursovigenteconceptopk(CursoVigenteConceptoPK cursovigenteconceptopk) {
		this.cursovigenteconceptopk = cursovigenteconceptopk;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}
	

}
