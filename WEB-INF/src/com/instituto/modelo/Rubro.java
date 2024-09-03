package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name = "Rubros")
public class Rubro extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2126957227843737405L;
	
	@Id
	@Column(name ="rubroid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long rubroid;
	private String rubro;
	private String descripcion;
	
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
	public Long getRubroid() {
		return rubroid;
	}
	public void setRubroid(Long rubroid) {
		this.rubroid = rubroid;
	}
	public String getRubro() {
		return rubro;
	}
	public void setRubro(String rubro) {
		this.rubro = rubro;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	
}
