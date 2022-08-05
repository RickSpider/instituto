package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="PersonaEntidad")
public class PersonaEntidad extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2567767576133627818L;
	@EmbeddedId
	private PersonaEntidadPK personaentidadpk = new PersonaEntidadPK();
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Persona getPersona() {
		return this.personaentidadpk.getPersona();
	}
	
	public void setPersona(Persona persona) {
		this.personaentidadpk.setPersona(persona);
	}
	
	public Entidad getEntidad() {
		return this.personaentidadpk.getEntidad();
	}
	
	public void setEntidad(Entidad entidad) {
		this.personaentidadpk.setEntidad(entidad);
	}
	
	public String getCuenta() {
		return this.personaentidadpk.getCuenta();
	}
	
	public void setCuenta(String cuenta) {
		this.personaentidadpk.setCuenta(cuenta);
	}

	public PersonaEntidadPK getPersonaentidadpk() {
		return personaentidadpk;
	}

	public void setPersonaentidadpk(PersonaEntidadPK personaentidadpk) {
		this.personaentidadpk = personaentidadpk;
	}

}
