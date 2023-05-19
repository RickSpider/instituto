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
import com.sun.istack.NotNull;


@Entity
@Table(name ="alumnos")
public class Alumno extends Modelo implements Serializable{

	/**
	 * 
	 */
	//private static final long serialVersionUID = -3897754450329909812L;
	private static final long serialVersionUID = -3897754450329909813L;

	@Id
	private Long alumnoid;
	
	@OneToOne
	@JoinColumn(name="alumnoid")
	@NotNull
	private Persona persona;
	
	@ManyToOne
	@JoinColumn(name = "sedeid")
	private Sede sede;
	
	private String comentario;
	
	@ManyToOne
	@JoinColumn(name = "personaFacturacionid")
	private Persona personaFacturacion;
	
	private boolean activo;

	@Override
	public Object[] getArrayObjectDatos() {
		
		String institucion = "";
		if (this.persona.getInstitucion()!=null)
			institucion = this.persona.getInstitucion().getInstitucion();
		
		Object[] o = {this.persona.getNombre(), this.persona.getApellido(), this.persona.getDocumentoNum(),this.persona.getCiudad().getCiudad() ,this.persona.getGradoAcademico().getGradoacademico(), institucion};
		
		return o;
	}

	@Override
	public String getStringDatos() {
		return this.alumnoid + " " + this.persona.getNombre() + " " + this.persona.getApellido() + " " +this.persona.getDocumentoNum() +" "+ this.persona.getCiudad().getCiudad()+ " " + this.persona.getGradoAcademico().getDescripcion()+ " " + this.persona.getInstitucion().getInstitucion();
		
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

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Persona getPersonaFacturacion() {
		return personaFacturacion;
	}

	public void setPersonaFacturacion(Persona personaFacturacion) {
		this.personaFacturacion = personaFacturacion;
	}
	
	
	
}
