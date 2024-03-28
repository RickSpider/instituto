package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name ="Entidades")
public class Entidad extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7304312873046741769L;
	
	@Id
	@Column(name ="entidadid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long entidadid;
	private String entidad;

	@Override
	public Object[] getArrayObjectDatos() {
		Object[] o = {this.entidad};
		return o;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return this.entidadid+" "+this.entidad;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getEntidadid() {
		return entidadid;
	}

	public void setEntidadid(Long entidadid) {
		this.entidadid = entidadid;
	}

}
