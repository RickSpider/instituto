package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CursoConceptoPK implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7798966360532346006L;

	@ManyToOne
	@JoinColumn(name="cursoid")
	private Curso curso;
	
	@ManyToOne
	@JoinColumn(name="conceptoid")
	private Concepto concepto;

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
