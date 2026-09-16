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

import org.hibernate.annotations.CreationTimestamp;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name = "translados")
public class Translado extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5186664142586228434L;

	@Id
	@Column(name = "transladoid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transladoid;

	@ManyToOne
	@JoinColumn(name = "alumnoid", nullable = false)
	private Alumno alumno;

	@ManyToOne
	@JoinColumn(name = "cursovigenteorigenid", nullable = false)
	private CursoVigente cursoVigenteOrigen;

	@ManyToOne
	@JoinColumn(name = "cursovigentedestinoid", nullable = false)
	private CursoVigente cursoVigenteDestino;

	@ManyToOne
	@JoinColumn(name = "comprobantetipoid", nullable = false)
	private Tipo comprobanteTipo;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private double montoTransladado;

	private String transladoNro;

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

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public CursoVigente getCursoVigenteOrigen() {
		return cursoVigenteOrigen;
	}

	public void setCursoVigenteOrigen(CursoVigente cursoVigenteOrigen) {
		this.cursoVigenteOrigen = cursoVigenteOrigen;
	}

	public CursoVigente getCursoVigenteDestino() {
		return cursoVigenteDestino;
	}

	public void setCursoVigenteDestino(CursoVigente cursoVigenteDestino) {
		this.cursoVigenteDestino = cursoVigenteDestino;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getMontoTransladado() {
		return montoTransladado;
	}

	public void setMontoTransladado(double montoTransladado) {
		this.montoTransladado = montoTransladado;
	}

	public String getTransladoNro() {
		return transladoNro;
	}

	public void setTransladoNro(String transladoNro) {
		this.transladoNro = transladoNro;
	}

	public Long getTransladoid() {
		return transladoid;
	}

	public void setTransladoid(Long transladoid) {
		this.transladoid = transladoid;
	}

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
	}

}
