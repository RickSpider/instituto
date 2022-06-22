package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CursoVigenteMateriaPK implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5403716663355084339L;

	@ManyToOne
	@JoinColumn(name="cursovigenteid")
	private CursoVigente cursoVigente;
	
	@ManyToOne
	@JoinColumn(name="materiaid")
	private Materia materia;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CursoVigente getCursoVigente() {
		return cursoVigente;
	}

	public void setCursoVigente(CursoVigente cursoVigente) {
		this.cursoVigente = cursoVigente;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}
	
	

}
