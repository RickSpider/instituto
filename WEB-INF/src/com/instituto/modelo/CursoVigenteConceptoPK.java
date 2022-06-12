package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CursoVigenteConceptoPK implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7798966360532346006L;

	@ManyToOne
	@JoinColumn(name="cursovigenteid")
	private CursoVigente cursoVigente;
	
	@ManyToOne
	@JoinColumn(name="conceptoid")
	private Concepto concepto;

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CursoVigente getCursoVigente() {
		return cursoVigente;
	}

	public void setCursoVigente(CursoVigente cursoVigente) {
		this.cursoVigente = cursoVigente;
	}

}
