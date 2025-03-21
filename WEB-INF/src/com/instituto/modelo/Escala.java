package com.instituto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name ="escalas")
public class Escala extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8946260454188933087L;
	
	@Id
	@Column(name ="escalaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long escalaid;
	private String nombre;
	private double limiteSuperior = 0;
	
	@OrderBy("escaladetalleid ASC")
	@OneToMany(mappedBy = "escala", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EscalaDetalle> detalles = new ArrayList<EscalaDetalle>();
	
	
	public Long getEscalaid() {
		return escalaid;
	}
	public void setEscalaid(Long escalaid) {
		this.escalaid = escalaid;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getLimiteSuperior() {
		return limiteSuperior;
	}
	public void setLimiteSuperior(double limiteSuperior) {
		this.limiteSuperior = limiteSuperior;
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
	public List<EscalaDetalle> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<EscalaDetalle> detalles) {
		this.detalles = detalles;
	}
	
	
	
}
