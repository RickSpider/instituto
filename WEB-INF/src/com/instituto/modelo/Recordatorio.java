package com.instituto.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="recordatorios")
public class Recordatorio extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1920856363775759778L;
	
	@Id
	@Column(name ="recordatorioid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long recordatorioid;
	
	private String titulo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaIni;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaFin;
	
    @Column(columnDefinition="text")
	private String recordatorio;
	

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

	public Long getRecordatorioid() {
		return recordatorioid;
	}

	public void setRecordatorioid(Long recordatorioid) {
		this.recordatorioid = recordatorioid;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getRecordatorio() {
		return recordatorio;
	}

	public void setRecordatorio(String recordatorio) {
		this.recordatorio = recordatorio;
	}

}
