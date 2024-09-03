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
@Table(name = "presupuestos")
public class Presupuesto extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9213685649008754134L;

	@Id
	@Column(name = "presupuestoid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long presupuestoid;

	private int anho;

	@OrderBy("presupuestodetalleid ASC")
	@OneToMany(mappedBy = "presupuesto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PresupuestoDetalle> detalles = new ArrayList<PresupuestoDetalle>();

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

	public Long getPresupuestoid() {
		return presupuestoid;
	}

	public void setPresupuestoid(Long presupuestoid) {
		this.presupuestoid = presupuestoid;
	}

	public int getAnho() {
		return anho;
	}

	public void setAnho(int anho) {
		this.anho = anho;
	}

	public List<PresupuestoDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<PresupuestoDetalle> detalles) {
		this.detalles = detalles;
	}

}
