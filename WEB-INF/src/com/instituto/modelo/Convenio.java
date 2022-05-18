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
@Table(name="convenios")
public class Convenio extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3997002060420537319L;
	
	@Id
	@Column(name ="convenioid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long convenioid;
	
	private String descripcion;
	
	@ManyToOne
	@JoinColumn(name = "institucionid")
	private Institucion institucion;
	
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;
	
	@Temporal(TemporalType.DATE)
	private Date fechaFin;
	
	private boolean activo;

	@Override
	public Object[] getArrayObjectDatos() {
		Object[] o = {this.institucion, this.institucion.getInstitucion() ,this.activo};
		return o;
	}

	@Override
	public String getStringDatos() {
		
		return this.convenioid+" "+this.institucion.getInstitucion()+" "+this.activo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getConvenioid() {
		return convenioid;
	}

	public void setConvenioid(Long convenioid) {
		this.convenioid = convenioid;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Institucion getInstitucion() {
		return institucion;
	}

	public void setInstitucion(Institucion institucion) {
		this.institucion = institucion;
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

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	
	
}
