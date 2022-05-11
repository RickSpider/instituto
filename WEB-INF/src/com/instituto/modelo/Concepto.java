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
@Table(name="conceptos")
public class Concepto extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4357875969021415027L;
	
	@Id
	@Column(name ="conceptoid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long conceptoid;
	private String concepto;
	private String descripcion;
	private Integer importe;

	@Override
	public Object[] getArrayObjectDatos() {
		Object[] o = {this.concepto, this.descripcion};
		return o;
	}

	@Override
	public String getStringDatos() {
		return this.conceptoid +" "+this.concepto+" "+this.descripcion;
	}

	public Long getConceptoid() {
		return conceptoid;
	}

	public void setConceptoid(Long conceptoid) {
		this.conceptoid = conceptoid;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getImporte() {
		return importe;
	}

	public void setImporte(Integer importe) {
		this.importe = importe;
	}
	
	
	

}
