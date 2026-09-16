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
@Table(name = "transladosdetalles")
public class TransladoDetalle extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2006484741763349236L;

	@Id
	@Column(name = "transladodetalleid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transladodetalleid;
	
	@ManyToOne
	@JoinColumn(name = "transladoid", nullable = false)
	private Translado transladoAlumno;
	
	@ManyToOne
	@JoinColumn(name = "estadocuentaid", nullable = false)
	private EstadoCuenta estadoCuenta;
	
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

	

	public Long getTransladodetalleid() {
		return transladodetalleid;
	}

	public void setTransladodetalleid(Long transladodetalleid) {
		this.transladodetalleid = transladodetalleid;
	}

	public Translado getTransladoAlumno() {
		return transladoAlumno;
	}

	public void setTransladoAlumno(Translado transladoAlumno) {
		this.transladoAlumno = transladoAlumno;
	}

	public EstadoCuenta getEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}
	
	

}
