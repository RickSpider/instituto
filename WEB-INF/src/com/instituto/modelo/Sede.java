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

import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Modelo;

@Entity
@Table(name ="sedes")
public class Sede extends Modelo implements Serializable{

	private static final long serialVersionUID = -8287263550414896230L;
	
	@Id
	@Column(name ="sedeid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long sedeid;
	
	private String sede;
	
	private String descripcion;
	private String direccion;
	private String telefono;
	private String email;
	private String establecimiento;
	
	@ManyToOne
	@JoinColumn(name = "ciudadid")
	private Ciudad ciudad;
	
	
	@Override
	public Object[] getArrayObjectDatos() {
		
		Object[] o = {this.sede, this.descripcion, this.direccion, this.telefono, this.email};
		
		return o;
	}

	@Override
	public String getStringDatos() {
		return this.sedeid + " " + this.sede + " " + this.descripcion +" " + this.direccion+" " + this.email;
	}

	public Long getSedeid() {
		return sedeid;
	}

	public void setSedeid(Long sedeid) {
		this.sedeid = sedeid;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	
	
}
