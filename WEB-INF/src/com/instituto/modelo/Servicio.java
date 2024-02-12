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
@Table(name ="servicios")
public class Servicio extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5176274889227937649L;
	
	@Id
	@Column(name ="servicioid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long servicioid;
	
	@ManyToOne
	@JoinColumn(name = "conceptoid")
	private Concepto concepto;
	
	private String servicio;

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

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public Long getServicioid() {
		return servicioid;
	}

	public void setServicioid(Long servicioid) {
		this.servicioid = servicioid;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}
	
	

}
