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
import com.doxacore.modelo.Tipo;

@Embeddable
public class CobranzaDetalleCobroPK  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4826678252033815596L;

	@ManyToOne
	@JoinColumn(name="cobranzaid")
	private Cobranza cobranza;
	
	@ManyToOne
	@JoinColumn(name="cobranzatipoid")
	private Tipo cobranzaTipo;


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Cobranza getCobranza() {
		return cobranza;
	}


	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}


	public Tipo getCobranzaTipo() {
		return cobranzaTipo;
	}


	public void setCobranzaTipo(Tipo cobranzaTipo) {
		this.cobranzaTipo = cobranzaTipo;
	}
	
	
	
}
