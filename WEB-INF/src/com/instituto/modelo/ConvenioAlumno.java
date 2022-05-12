package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="ConveniosAlumnos")
public class ConvenioAlumno extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 725844431680718742L;

	@EmbeddedId
	private ConvenioAlumnoPK convenioalumnopk = new ConvenioAlumnoPK();
	
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

	public ConvenioAlumnoPK getConvenioalumnopk() {
		return convenioalumnopk;
	}

	public void setConvenioalumnopk(ConvenioAlumnoPK convenioalumnopk) {
		this.convenioalumnopk = convenioalumnopk;
	}
	
	public Convenio getConvenio() {
		
		return this.convenioalumnopk.getConvenio();
		
	}
	
	public void setConvenio(Convenio convenio) {
		
		this.convenioalumnopk.setConvenio(convenio);
		
	}
	
	public Alumno getAlumno() {
		
		return this.convenioalumnopk.getAlumno();
		
	}
	
	public void setAlumno(Alumno alumno) {
		
		this.convenioalumnopk.setAlumno(alumno);
		
	}


}
