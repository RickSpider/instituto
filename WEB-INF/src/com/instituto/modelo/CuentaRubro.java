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
@Table(name="CuentasRubros")
public class CuentaRubro extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7112563409550881379L;


	@Id
	@Column(name ="cuentarubroid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cuentarubroid;
	
	private String cuentaRubro;
	
	@ManyToOne
	@JoinColumn(name = "rubroid")
	private Rubro rubro;
	
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


	public Long getCuentarubroid() {
		return cuentarubroid;
	}


	public void setCuentarubroid(Long cuentarubroid) {
		this.cuentarubroid = cuentarubroid;
	}


	public String getCuentaRubro() {
		return cuentaRubro;
	}


	public void setCuentaRubro(String cuentaRubro) {
		this.cuentaRubro = cuentaRubro;
	}


	public Rubro getRubro() {
		return rubro;
	}


	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}
	
	

}
