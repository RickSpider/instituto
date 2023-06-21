package com.instituto.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

import bsh.This;

@Entity
@Table(name="cursosvigentesmaterias")
public class CursoVigenteMateria extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1752716860981653289L;

	@EmbeddedId
	private CursoVigenteMateriaPK cursovigentemateriapk = new CursoVigenteMateriaPK();
	
	@ManyToOne
	@JoinColumn(name = "estadoid")
	private Tipo estado;
	
	@Override
	public Object[] getArrayObjectDatos() {
		Object [] o = {this.getOrden(), this.getMateria().getMateria(), getMateria().getMateriaTipo().getTipo(),this.estado.getTipo()}; 
		return o;
	}
	
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;
	
	@Temporal(TemporalType.DATE)
	private Date fechaFin;


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CursoVigenteMateriaPK getCursovigentemateriapk() {
		return cursovigentemateriapk;
	}

	public void setCursovigentemateriapk(CursoVigenteMateriaPK cursovigentemateriapk) {
		this.cursovigentemateriapk = cursovigentemateriapk;
	}
	
	public CursoVigente getCursoVigente() {
		
		return this.cursovigentemateriapk.getCursoVigente();
		
	}
	
	public void setCursoVigente(CursoVigente cursoVigente) {
		
		this.cursovigentemateriapk.setCursoVigente(cursoVigente);
		
	}
	
	public Materia getMateria() {
		
		return this.cursovigentemateriapk.getMateria();
		
	}
	
	public void setMateria(Materia materia) {
		
		this.cursovigentemateriapk.setMateria(materia);
		
	}

	public Tipo getEstado() {
		return estado;
	}

	public void setEstado(Tipo estado) {
		this.estado = estado;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return null;
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
	

}
