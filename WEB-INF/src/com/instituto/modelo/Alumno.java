package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;


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
	
	@ManyToOne
	@JoinColumn(name = "sedeid")
	private Sede sede;
	
	private boolean activo;

	@Override
	public Object[] getArrayObjectDatos() {
		
		String institucion = "";
		if (this.persona.getInstitucion()!=null)
			institucion = this.persona.getInstitucion().getInstitucion();
		
		Object[] o = {this.persona.getNombre(), this.persona.getApellido(), this.persona.getCiudad().getCiudad() ,this.persona.getGradoAcademico().getDescripcion(), institucion};
		
		return o;
	}

	@Override
	public String getStringDatos() {
		return this.alumnoid + " " + this.persona.getNombre() + " " + this.persona.getApellido() + " " +this.persona.getCiudad().getCiudad()+ " " + this.persona.getGradoAcademico().getDescripcion()+ " " + this.persona.getInstitucion().getInstitucion();
		
	}
	
	public String getFullNombre() {
		
		return this.persona.getApellido()+", "+this.persona.getNombre();
		
	}
	
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
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

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
