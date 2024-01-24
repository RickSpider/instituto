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
import com.doxacore.modelo.Usuario;

@Entity
@Table(name = "Cajas")
public class Caja extends Modelo implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5249114870126368828L;


	@Id
	@Column(name ="cajaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cajaid;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date apertura;
	private double montoApertura;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date cierre;
	
	private Double montoCierre;
	
	@ManyToOne
	@JoinColumn(name = "usuariocajaid")
	private Usuario usuarioCaja;
	
	@ManyToOne
	@JoinColumn(name = "usuariocierreid")
	private Usuario usuarioCierre;
	
	@ManyToOne
	@JoinColumn(name = "sedeid")
	private Sede sede;
		

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

	public Long getCajaid() {
		return cajaid;
	}

	public void setCajaid(Long cajaid) {
		this.cajaid = cajaid;
	}

	public Date getApertura() {
		return apertura;
	}

	public void setApertura(Date apertura) {
		this.apertura = apertura;
	}

	public double getMontoApertura() {
		return montoApertura;
	}

	public void setMontoApertura(double montoApertura) {
		this.montoApertura = montoApertura;
	}

	public Date getCierre() {
		return cierre;
	}

	public void setCierre(Date cierre) {
		this.cierre = cierre;
	}

	public Double getMontoCierre() {
		return montoCierre;
	}

	public void setMontoCierre(Double montoCierre) {
		this.montoCierre = montoCierre;
	}

	public Sede getSede() {
		return sede;
	}

	public void setSede(Sede sede) {
		this.sede = sede;
	}

	public Usuario getUsuarioCierre() {
		return usuarioCierre;
	}

	public void setUsuarioCierre(Usuario usuarioCierre) {
		this.usuarioCierre = usuarioCierre;
	}

	public Usuario getUsuarioCaja() {
		return usuarioCaja;
	}

	public void setUsuarioCaja(Usuario usuarioCaja) {
		this.usuarioCaja = usuarioCaja;
	}

	
}
