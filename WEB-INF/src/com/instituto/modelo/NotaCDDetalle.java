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

import org.hibernate.annotations.ColumnDefault;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name = "notacddetalles")
public class NotaCDDetalle extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="notacddetalleid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long notacddetalleid;
	
	@ManyToOne
	@JoinColumn(name = "notacdid")
	private NotaCD notacd;
	
	
	@ManyToOne
	@JoinColumn(name="estadocuentaid")
	private EstadoCuenta estadoCuenta;
	
	@ManyToOne
	@JoinColumn(name = "servicioid")
	private Servicio servicio;
	
	private String descripcion;
	
	private double monto = 0.0;
	
	@ColumnDefault("0.0")
	private double exento = 0;
	@ColumnDefault("0.0")
	private double iva10 = 0;
	@ColumnDefault("0.0")
	private double iva5 = 0;
	public Long getNotacddetalleid() {
		return notacddetalleid;
	}
	public void setNotacddetalleid(Long notacddetalleid) {
		this.notacddetalleid = notacddetalleid;
	}
	public NotaCD getNotacd() {
		return notacd;
	}
	public void setNotacd(NotaCD notacd) {
		this.notacd = notacd;
	}
	public EstadoCuenta getEstadoCuenta() {
		return estadoCuenta;
	}
	public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
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
	public double getExento() {
		return exento;
	}
	public void setExento(double exento) {
		this.exento = exento;
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
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
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
	
	

}
