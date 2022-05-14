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
@Table(name ="gradosacademicos")
public class GradoAcademico extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8052259194963768465L;

	@Id
	@Column(name ="gradoacademicoid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long gradoacademicoid;
	private String gradoacademico;
	private String descripcion;
	
	@Override
	public Object[] getArrayObjectDatos() {
		Object [] o = {this.gradoacademico, this.descripcion};
		return o;
	}

	@Override
	public String getStringDatos() {
		
		return this.gradoacademicoid +" "+ this.gradoacademico +" "+ this.descripcion;
	}

	public Long getGradoacademicoid() {
		return gradoacademicoid;
	}

	public void setGradoacademicoid(Long gradoacademicoid) {
		this.gradoacademicoid = gradoacademicoid;
	}

	public String getGradoacademico() {
		return gradoacademico;
	}

	public void setGradoacademico(String gradoacademico) {
		this.gradoacademico = gradoacademico;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}
