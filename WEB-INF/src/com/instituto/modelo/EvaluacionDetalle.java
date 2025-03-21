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

@Entity
@Table(name="evaluacionesdetalles")
public class EvaluacionDetalle extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 719156817368277941L;

	@Id
	@Column(name ="evaluaciondetalleid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long evaluaciondetalleid;
	
	@ManyToOne
	@JoinColumn(name="evaluacionid", nullable = false)
	private Evaluacion evaluacion;
	
	@ManyToOne
	@JoinColumn(name="alumnoid", nullable = false)
	private Alumno alumno;
	
	private double proceso1 = 0;
	private double proceso2 = 0;
	private double proceso3 = 0;
	private double proceso4 = 0;
	private double proceso5 = 0;
	private double pfinal = 0;
	private double calificacion;	
	
	public Long getEvaluaciondetalleid() {
		return evaluaciondetalleid;
	}
	public void setEvaluaciondetalleid(Long evaluaciondetalleid) {
		this.evaluaciondetalleid = evaluaciondetalleid;
	}
	public Evaluacion getEvaluacion() {
		return evaluacion;
	}
	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}
	public Alumno getAlumno() {
		return alumno;
	}
	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public double getProceso1() {
		return proceso1;
	}
	public void setProceso1(double proceso1) {
		this.proceso1 = proceso1;
	}
	public double getProceso2() {
		return proceso2;
	}
	public void setProceso2(double proceso2) {
		this.proceso2 = proceso2;
	}
	public double getProceso3() {
		return proceso3;
	}
	public void setProceso3(double proceso3) {
		this.proceso3 = proceso3;
	}
	public double getProceso4() {
		return proceso4;
	}
	public void setProceso4(double proceso4) {
		this.proceso4 = proceso4;
	}
	public double getProceso5() {
		return proceso5;
	}
	public void setProceso5(double proceso5) {
		this.proceso5 = proceso5;
	}
	public double getPfinal() {
		return pfinal;
	}
	public void setPfinal(double pfinal) {
		this.pfinal = pfinal;
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
	public double getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(double calificacion) {
		this.calificacion = calificacion;
	}
	
	
	
	
	

}
