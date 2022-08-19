package com.instituto.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	private CobranzaDetalleCobroPK cobranzacobrodetallepk = new CobranzaDetalleCobroPK();
	
	@ManyToOne
	@JoinColumn(name = "monedaTipoid")
	private Tipo monedaTipo;
	private double monedaCambio = 1;
	
	@ManyToOne
	@JoinColumn(name = "entidadid")
	private Entidad Entidad;
	
	private double monto = 0.0;
	
	private String cuentaNum;
	private String transaccionNum;
	private String chequeNum;
	
	private String Titular;
	private Long autorizacionNum;
	
	
	@Temporal(TemporalType.DATE)
	private Date emision;
	
	@Temporal(TemporalType.DATE)
	private Date vencimiento;

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
	
	public Tipo getFormaPago() {
		return this.cobranzacobrodetallepk.getFormaPago();
	}
	
	public void setFormaPago(Tipo formaPago) {
		this.cobranzacobrodetallepk.setFormaPago(formaPago);
	}

	public Tipo getMonedaTipo() {
		return monedaTipo;
	}

	public void setMonedaTipo(Tipo monedaTipo) {
		this.monedaTipo = monedaTipo;
	}

	public Entidad getEntidad() {
		return Entidad;
	}

	public void setEntidad(Entidad entidad) {
		Entidad = entidad;
	}

	public String getCuentaNum() {
		return cuentaNum;
	}

	public void setCuentaNum(String cuentaNum) {
		this.cuentaNum = cuentaNum;
	}

	public String getTransaccionNum() {
		return transaccionNum;
	}

	public void setTransaccionNum(String transaccionNum) {
		this.transaccionNum = transaccionNum;
	}

	public String getChequeNum() {
		return chequeNum;
	}

	public void setChequeNum(String chequeNum) {
		this.chequeNum = chequeNum;
	}

	public String getTitular() {
		return Titular;
	}

	public void setTitular(String titular) {
		Titular = titular;
	}

	public Date getEmision() {
		return emision;
	}

	public void setEmision(Date emision) {
		this.emision = emision;
	}

	public Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}

	public double getMonedaCambio() {
		return monedaCambio;
	}

	public void setMonedaCambio(double monedaCambio) {
		this.monedaCambio = monedaCambio;
	}

	public Long getAutorizacionNum() {
		return autorizacionNum;
	}

	public void setAutorizacionNum(Long autorizacionNum) {
		this.autorizacionNum = autorizacionNum;
	}
	
}
