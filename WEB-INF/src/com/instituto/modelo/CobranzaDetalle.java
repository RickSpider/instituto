package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name = "CobranzasDetalles")
public class CobranzaDetalle extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5289738875919476975L;


	@EmbeddedId
	private CobranzaDetallePK cobranzadetallepk;
	
	private double monto;
	
	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public CobranzaDetallePK getCobranzadetallepk() {
		return cobranzadetallepk;
	}

	public void setCobranzadetallepk(CobranzaDetallePK cobranzadetallepk) {
		this.cobranzadetallepk = cobranzadetallepk;
	}
	
	public Cobranza getCobranza() {
		
		return this.cobranzadetallepk.getCobranza();
	};
	
	public void setCobranza(Cobranza cobranza) {
		
		this.cobranzadetallepk.setCobranza(cobranza);
	}
	
	public EstadoCuenta getEstadoCuenta() {
		
		return this.cobranzadetallepk.getEstadoCuenta();
	}
	
	public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
		
		this.cobranzadetallepk.setEstadoCuenta(estadoCuenta);
		
	}
	
}
