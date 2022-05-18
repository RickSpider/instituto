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
@Table(name="instituciones")
public class Institucion extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -965099407395814155L;
	
	@Id
	@Column(name ="institucionid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long institucionid;
	private String institucion;
	
	@ManyToOne
	@JoinColumn(name = "ciudadid")
	private Ciudad ciudad;
	private String direccion;
	
	private String telefono;
	private String email;
	

	@Override
	public Object[] getArrayObjectDatos() {
		
		Object [] o = {this.institucion, this.ciudad.getDepartamento().getDepartamento() ,this.ciudad.getCiudad(), this.direccion, this.telefono, this.email };
		return o;
		
	}

	@Override
	public String getStringDatos() {
	
		return this.institucionid +" "+ this.institucion +" "+ this.ciudad.getDepartamento().getDepartamento() +" "+ this.ciudad.getCiudad() +" "+ this.direccion +" "+ this.telefono +" "+ this.email;
	}

	public Long getInstitucionid() {
		return institucionid;
	}

	public void setInstitucionid(Long institucionid) {
		this.institucionid = institucionid;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
