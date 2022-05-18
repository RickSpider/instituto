package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CursoMateriaPK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -514560671890935169L;

	@ManyToOne
	@JoinColumn(name="cursoid")
	private Curso curso;
	
	@ManyToOne
	@JoinColumn(name="materiaid")
	private Materia materia;

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
