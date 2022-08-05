package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class PersonaEntidadPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1268738427063601519L;

	@ManyToOne
	@JoinColumn(name="personaid")
	private Persona persona;
	
	@ManyToOne
	@JoinColumn(name="entidadid")
	private Entidad entidad;
	private String cuenta;

	public Entidad getEntidad() {
		return entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}


	
}
