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
@Table(name="cursos")
public class Curso extends Modelo implements Serializable {

	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7392185203473737694L;
	
	@Id
	@Column(name ="cursoid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cursoid;
	private String curso;
	private String Descripcion;
	private int duracion;
	
	@Override
	public Object[] getArrayObjectDatos() {
		Object[] o = {this.curso, this.Descripcion, this.duracion};
		return o;
	}

	@Override
	public String getStringDatos() {
		
		return this.cursoid +" "+this.curso+" "+this.Descripcion+" "+this.duracion;
		
	}

	public Long getCursoid() {
		return cursoid;
	}

	public void setCursoid(Long cursoid) {
		this.cursoid = cursoid;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
