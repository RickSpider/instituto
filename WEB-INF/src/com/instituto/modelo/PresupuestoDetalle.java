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
import com.doxacore.modelo.Tipo;

@Entity
@Table(name ="presupuestosdetalles")
public class PresupuestoDetalle extends Modelo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2731869838103544009L;

	@Id
	@Column(name ="presupuestodetalleid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long presupuestodetalleid;
	
	@ManyToOne
	@JoinColumn(name = "presupuestoid", nullable = false)
	private Presupuesto presupuesto;
	
	@ManyToOne
	@JoinColumn(name = "rubroid", nullable = false)
	private Rubro rubro;
	private double monto = 0;
	
	@ManyToOne
	@JoinColumn(name = "monedaTipoid")
	private Tipo monedaTipo;
	
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
	public Long getPresupuestodetalleid() {
		return presupuestodetalleid;
	}
	public void setPresupuestodetalleid(Long presupuestodetalleid) {
		this.presupuestodetalleid = presupuestodetalleid;
	}
	public Presupuesto getPresupuesto() {
		return presupuesto;
	}
	public void setPresupuesto(Presupuesto presupuesto) {
		this.presupuesto = presupuesto;
	}
	public Rubro getRubro() {
		return rubro;
	}
	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public Tipo getMonedaTipo() {
		return monedaTipo;
	}
	public void setMonedaTipo(Tipo monedaTipo) {
		this.monedaTipo = monedaTipo;
	}
	
	
	
	
}
