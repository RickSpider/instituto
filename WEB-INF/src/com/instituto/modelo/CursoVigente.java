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

@Entity
@Table(name="CursosVigentes")
public class CursoVigente extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3684064883292406048L;

	@Id
	@Column(name ="cursovigenteid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cursovigenteid;
	
	@ManyToOne
	@JoinColumn(name = "cursoid")
	private Curso curso;
	
	@ManyToOne
	@JoinColumn(name = "sedeid")
	private Sede sede;
	
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;
	
	@Temporal(TemporalType.DATE)
	private Date fechaFin;
	
	// do;lu;ma;mi;ju;vi;sa
	private String dias = "false;false;false;false;false;false;false";

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


	public Long getCursovigenteid() {
		return cursovigenteid;
	}

	public void setCursovigenteid(Long cursovigenteid) {
		this.cursovigenteid = cursovigenteid;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getDias() {
		return dias;
	}

	public void setDias(String dias) {
		this.dias = dias;
	}

	public Sede getSede() {
		return sede;
	}

	public void setSede(Sede sede) {
		this.sede = sede;
	}

	
	
	
}
