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
@Table(name = "Cobranzas")
public class Cobranza extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9175243805383056607L;
	
	@Id
	@Column(name ="cobranzaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cobranzaid;
	
	private boolean esFactura = false;
	private boolean esRecibo = false;
	
	@ManyToOne
	@JoinColumn(name = "alumnoid")
	private Alumno alumno;
	
	@ManyToOne
	@JoinColumn(name = "personaFacturacionid")
	private Persona personaFacturacion;
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isEsFactura() {
		return esFactura;
	}

	public void setEsFactura(boolean esFactura) {
		this.esFactura = esFactura;
	}

	public boolean isEsRecibo() {
		return esRecibo;
	}

	public void setEsRecibo(boolean esRecibo) {
		this.esRecibo = esRecibo;
	}

	public Long getCobranzaid() {
		return cobranzaid;
	}

	public void setCobranzaid(Long cobranzaid) {
		this.cobranzaid = cobranzaid;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Persona getPersonaFacturacion() {
		return personaFacturacion;
	}

	public void setPersonaFacturacion(Persona personaFacturacion) {
		this.personaFacturacion = personaFacturacion;
	}

}
