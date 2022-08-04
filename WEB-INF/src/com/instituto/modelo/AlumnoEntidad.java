package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="AlumnosEntidades")
public class AlumnoEntidad extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2567767576133627818L;
	@EmbeddedId
	private AlumnoEntidadPK alumnoentidadpk = new AlumnoEntidadPK();
	
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

	public AlumnoEntidadPK getAlumnoentidadpk() {
		return alumnoentidadpk;
	}

	public void setAlumnoentidadpk(AlumnoEntidadPK alumnoentidadpk) {
		this.alumnoentidadpk = alumnoentidadpk;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Alumno getAlumno() {
		return this.alumnoentidadpk.getAlumno();
	}
	
	public void setAlumno(Alumno alumno) {
		this.alumnoentidadpk.setAlumno(alumno);
	}
	
	public Entidad getEntidad() {
		return this.alumnoentidadpk.getEntidad();
	}
	
	public void setEntidad(Entidad entidad) {
		this.alumnoentidadpk.setEntidad(entidad);
	}
	
	public String getCuenta() {
		return this.alumnoentidadpk.getCuenta();
	}
	
	public void setCuenta(String cuenta) {
		this.alumnoentidadpk.setCuenta(cuenta);
	}

}
