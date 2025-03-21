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

import com.doxacore.modelo.Modelo;
import com.sun.istack.NotNull;

@Entity
@Table(name ="escalasdetalles")
public class EscalaDetalle extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3443034042335004963L;

	@Id
	@Column(name ="escaladetalleid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long escaladetalleid;
	
	@ManyToOne
	@JoinColumn(name="escalaid")
	@NotNull
	private Escala escala;
	
	private double limiteInferior = 0;
	private double limiteSuperior = 0;
	private double nota = 0;
	
	public Long getEscaladetalleid() {
		return escaladetalleid;
	}
	public void setEscaladetalleid(Long escaladetalleid) {
		this.escaladetalleid = escaladetalleid;
	}
	
	public double getNota() {
		return nota;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
	public Escala getEscala() {
		return escala;
	}
	public void setEscala(Escala escala) {
		this.escala = escala;
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
	public double getLimiteInferior() {
		return limiteInferior;
	}
	public void setLimiteInferior(double limiteInferior) {
		this.limiteInferior = limiteInferior;
	}
	public double getLimiteSuperior() {
		return limiteSuperior;
	}
	public void setLimiteSuperior(double limiteSuperior) {
		this.limiteSuperior = limiteSuperior;
	}
	
	
	
}
