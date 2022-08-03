package com.instituto.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name = "Cotizaciones")
public class Cotizacion extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6060835052591948713L;
	
	@Id
	@Column(name ="cotizacionid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cotizacionid;
	
	@ManyToOne
	@JoinColumn(name = "monedaTipoId")
	private Tipo monedaTipo;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	private double compra;
	private double venta;

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

	public Tipo getMonedaTipo() {
		return monedaTipo;
	}

	public void setMonedaTipo(Tipo monedaTipo) {
		this.monedaTipo = monedaTipo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getCompra() {
		return compra;
	}

	public void setCompra(double compra) {
		this.compra = compra;
	}

	public double getVenta() {
		return venta;
	}

	public void setVenta(double venta) {
		this.venta = venta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getCotizacionid() {
		return cotizacionid;
	}

	public void setCotizacionid(Long cotizacionid) {
		this.cotizacionid = cotizacionid;
	}

	
	
}
