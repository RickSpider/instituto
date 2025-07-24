package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name = "CobranzasDetalles")
public class CobranzaDetalle extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5289738875919476975L;


	@EmbeddedId
	private CobranzaDetallePK cobranzadetallepk = new CobranzaDetallePK();
	
	private double monto = 0.0;
	private double montoDescuento = 0.0;
	
	@ManyToOne
	@JoinColumn(name = "servicioid")
	private Servicio servicio;

	private String descripcion;
	
	private String dncpG;
	private String dncpE;
	
	@ColumnDefault("0.0")
	private double exento = 0;
	@ColumnDefault("0.0")
	private double iva10 = 0;
	@ColumnDefault("0.0")
	private double iva5 = 0;
	
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

	public double getMontoDescuento() {
		return montoDescuento;
	}

	public void setMontoDescuento(double montoDescuento) {
		this.montoDescuento = montoDescuento;
	}
	
	public double getSaldo() {
		return this.getEstadoCuenta().getMonto() - (getEstadoCuenta().getPago() + getEstadoCuenta().getMontoDescuento());
	}

	public double getIva10() {
		return iva10;
	}

	public void setIva10(double iva10) {
		this.iva10 = iva10;
	}

	public double getIva5() {
		return iva5;
	}

	public void setIva5(double iva5) {
		this.iva5 = iva5;
	}

	public double getExento() {
		return exento;
	}

	public void setExento(double exento) {
		this.exento = exento;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDncpG() {
		return dncpG;
	}

	public void setDncpG(String dncpG) {
		this.dncpG = dncpG;
	}

	public String getDncpE() {
		return dncpE;
	}

	public void setDncpE(String dncpE) {
		this.dncpE = dncpE;
	}
	
	
	
}
