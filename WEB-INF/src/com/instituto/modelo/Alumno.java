package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;


@Entity
@Table(name ="alumnos")
public class Alumno extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3897754450329909812L;

	@Id
	private Long alumnoid;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="alumnoid")
	private Persona persona;
	
	private String matricula;
	
	@ManyToOne
	@JoinColumn(name = "sedeid")
	private Sede sede;
	
	@ManyToOne
	@JoinColumn(name = "comprobantetipoid")
	private Tipo comprobanteTipo;
	
	private boolean activo;

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
	
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Sede getSede() {
		return sede;
	}

	public void setSede(Sede sede) {
		this.sede = sede;
	}

	public Long getAlumnoid() {
		return alumnoid;
	}

	public void setAlumnoid(Long alumnoid) {
		this.alumnoid = alumnoid;
	}

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	
	
}
