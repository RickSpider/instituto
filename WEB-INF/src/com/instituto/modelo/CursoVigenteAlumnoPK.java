package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CursoVigenteAlumnoPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5724295842482366668L;

	@ManyToOne
	@JoinColumn(name="cursovigenteid")
	private CursoVigente cursoVigente;
	
	@ManyToOne
	@JoinColumn(name="alumnoid")
	private Alumno alumno;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CursoVigente getCursoVigente() {
		return cursoVigente;
	}

	public void setCursoVigente(CursoVigente cursoVigente) {
		this.cursoVigente = cursoVigente;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	
	
}
