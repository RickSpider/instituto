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

@Entity
@Table(name="Cuentas")
public class Cuenta extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -783929000764687783L;


	@Id
	@Column(name ="cuentaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cuentaid;
	
	@ManyToOne
	@JoinColumn(name = "sedeid")
	private Sede sede;
	
	private String numero;
	
	@ManyToOne
	@JoinColumn(name = "entidadid")
	private Entidad entidad;
	
	
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
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Entidad getEntidad() {
		return entidad;
	}
	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}
	public Sede getSede() {
		return sede;
	}
	public void setSede(Sede sede) {
		this.sede = sede;
	}
	public Long getCuentaid() {
		return cuentaid;
	}
	public void setCuentaid(Long cuentaid) {
		this.cuentaid = cuentaid;
	}
	
	

}
