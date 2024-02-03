package com.instituto.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name = "Transacciones")
public class Transaccion extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5709745154904767329L;

	@Id
	@Column(name = "transaccionid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transaccionid;

	@ManyToOne
	@JoinColumn(name = "cuentaid")
	private Cuenta cuenta;

	@ManyToOne
	@JoinColumn(name = "transacciontipoid")
	private Tipo transaccionTipo;

	private double monto = 0;
	private Date fecha = new Date();
	private String comentario;

	@ManyToOne
	@JoinColumns({ 
		@JoinColumn(name = "cobranzaid"),
		@JoinColumn(name = "Formapagoid") 
		})
	private CobranzaDetalleCobro cobranzadetallecobro;

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

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Tipo getTransaccionTipo() {
		return transaccionTipo;
	}

	public void setTransaccionTipo(Tipo transaccionTipo) {
		this.transaccionTipo = transaccionTipo;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Long getTransaccionid() {
		return transaccionid;
	}

	public void setTransaccionid(Long transaccionid) {
		this.transaccionid = transaccionid;
	}

	public CobranzaDetalleCobro getCobranzadetallecobro() {
		return cobranzadetallecobro;
	}

	public void setCobranzadetallecobro(CobranzaDetalleCobro cobranzadetallecobro) {
		this.cobranzadetallecobro = cobranzadetallecobro;
	}

}
