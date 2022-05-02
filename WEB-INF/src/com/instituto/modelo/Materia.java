package com.instituto.modelo;

import java.io.Serializable;

import com.doxacore.modelo.Modelo;

public class Materia extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7207233943384320624L;
	
	private Long materiaid;
	private String materia;
	private String descripcion;

	@Override
	public Object[] getArrayObjectDatos() {
		Object[] o = {this.materia, this.descripcion};
		return o;
	}

	@Override
	public String getStringDatos() {
		return this.materiaid+" "+this.materia+" "+this.descripcion;
	}

	public Long getMateriaid() {
		return materiaid;
	}

	public void setMateriaid(Long materiaid) {
		this.materiaid = materiaid;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}
