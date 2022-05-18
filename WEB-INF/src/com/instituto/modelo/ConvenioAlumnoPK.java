package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ConvenioAlumnoPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3028415535527026562L;

	@ManyToOne
	@JoinColumn(name="convenioid")
	private Convenio convenio;
	
	@ManyToOne
	@JoinColumn(name="alumnoid")
	private Alumno alumno;

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
