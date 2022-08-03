package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name = "CobranzasDetallesCobros")
public class CobranzaDetalleCobro  extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8601693248579254130L;

	@EmbeddedId
	private CobranzaDetalleCobroPK cobranzacobrodetallepk;
	
	private double monto;

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

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CobranzaDetalleCobroPK getCobranzacobrodetallepk() {
		return cobranzacobrodetallepk;
	}

	public void setCobranzacobrodetallepk(CobranzaDetalleCobroPK cobranzacobrodetallepk) {
		this.cobranzacobrodetallepk = cobranzacobrodetallepk;
	}

	public Cobranza getCobranza() {
		
		return this.cobranzacobrodetallepk.getCobranza();
	};
	
	public void setCobranza(Cobranza cobranza) {
		
		this.cobranzacobrodetallepk.setCobranza(cobranza);
	}
	
	public Tipo getCobranzaTipo() {
		return this.cobranzacobrodetallepk.getCobranzaTipo();
	}
	
	public void setCobranzaTipo(Tipo cobranzaTipo) {
		this.cobranzacobrodetallepk.setCobranzaTipo(cobranzaTipo);
	}
	
}
