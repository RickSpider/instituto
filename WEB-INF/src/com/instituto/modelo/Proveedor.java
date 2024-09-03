package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity 
@Table(name="proveedores")
public class Proveedor extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2169159564676036552L;
	
	@Id
	@Column(name ="proveedorid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long proveedorid;
	
	@ManyToOne
	@JoinColumn(name = "personaid")
	private Persona persona;
	
	@ManyToOne
	@JoinColumn(name = "sedeid")
	private Sede sede;
	
	@ManyToOne
	@JoinColumn(name = "proveedortipoid")
	private Tipo proveedorTipo;
	
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

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Sede getSede() {
		return sede;
	}

	public void setSede(Sede sede) {
		this.sede = sede;
	}

	public Long getProveedorid() {
		return proveedorid;
	}

	public void setProveedorid(Long proveedorid) {
		this.proveedorid = proveedorid;
	}

	public Tipo getProveedorTipo() {
		return proveedorTipo;
	}

	public void setProveedorTipo(Tipo proveedorTipo) {
		this.proveedorTipo = proveedorTipo;
	}
	
	

	
	
}
