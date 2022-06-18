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
@Table(name = "Materias")
public class Materia extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7207233943384320624L;
	
	@Id
	@Column(name ="materiaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long materiaid;
	private String materia;
	private String descripcion;
	
	@Column(columnDefinition = "int default 0")
	private int cargaHoraria;
	
	@ManyToOne
	@JoinColumn(name = "materiatipoid")
	private Tipo materiaTipo;

	@Override
	public Object[] getArrayObjectDatos() {
		Object[] o = {this.materia, this.descripcion, this.cargaHoraria};
		return o;
	}

	@Override
	public String getStringDatos() {
		return this.materiaid+" "+this.materia+" "+this.descripcion+" "+this.cargaHoraria;
	}

	public Long getMateriaid() {
		return materiaid;
	}

	public void setMateriaid(Long materiaid) {
		this.materiaid = materiaid;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(int cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Tipo getMateriaTipo() {
		return materiaTipo;
	}

	public void setMateriaTipo(Tipo materiaTipo) {
		this.materiaTipo = materiaTipo;
	}
	
}
