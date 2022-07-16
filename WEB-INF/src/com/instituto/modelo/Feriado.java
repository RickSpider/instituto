package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="feriados")
public class Feriado extends Modelo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -886038747788255419L;

	@Id
	@Column(name ="feriadoid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long feriadoid;
	
	private int mes;
	private int dia;
	
	private boolean activo = true;

	public Long getFeriadoid() {
		return feriadoid;
	}

	public void setFeriadoid(Long feriadoid) {
		this.feriadoid = feriadoid;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
