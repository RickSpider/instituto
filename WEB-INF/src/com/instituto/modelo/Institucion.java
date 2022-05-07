package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Modelo;

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

}
