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
@Table(name ="comprobantes")
public class Comprobante extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1092221832415878123L;

	@Id
	@Column(name ="comprobanteid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long comprobanteid;
	
	@ManyToOne
	@JoinColumn(name = "sedeid")
	private Sede sede;
	
	@ManyToOne
	@JoinColumn(name = "comprobanteTipoid")
	private Tipo comprobanteTipo;
	
	private Long timbrado;
	
	private String puntoExpdicion;
	
	@Temporal(TemporalType.DATE)
	private Date emision;
	
	@Temporal(TemporalType.DATE)
	private Date vencimiento;
	
	private Long inicio;
	private Long fin;
	
	private Long siguiente;
	
	private boolean activo;

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

	public Long getComprobanteid() {
		return comprobanteid;
	}

	public void setComprobanteid(Long comprobanteid) {
		this.comprobanteid = comprobanteid;
	}

	public Sede getSede() {
		return sede;
	}

	public void setSede(Sede sede) {
		this.sede = sede;
	}

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
	}

	public Long getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(Long timbrado) {
		this.timbrado = timbrado;
	}

	public String getPuntoExpdicion() {
		return puntoExpdicion;
	}

	public void setPuntoExpdicion(String puntoExpdicion) {
		this.puntoExpdicion = puntoExpdicion;
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

	public Long getInicio() {
		return inicio;
	}

	public void setInicio(Long inicio) {
		this.inicio = inicio;
	}

	public Long getFin() {
		return fin;
	}

	public void setFin(Long fin) {
		this.fin = fin;
	}

	public Long getSiguiente() {
		return siguiente;
	}

	public void setSiguiente(Long siguiente) {
		this.siguiente = siguiente;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	

}
