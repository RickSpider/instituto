package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Embeddable
public class CobranzaDetallePK  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 267643597096488509L;

	@ManyToOne
	@JoinColumn(name="cobranzaid")
	private Cobranza cobranza;
	
	@ManyToOne
	@JoinColumn(name="estadocuentaid")
	private EstadoCuenta estadoCuenta;

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}

	public EstadoCuenta getEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
