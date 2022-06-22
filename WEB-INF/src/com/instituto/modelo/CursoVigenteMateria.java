package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="cursosvigentesmaterias")
public class CursoVigenteMateria extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6430353924616240202L;

	@EmbeddedId
	private CursoVigenteMateriaPK cursovigentemateriapk = new CursoVigenteMateriaPK();
	
	private double importe;
	
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

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
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
	
	public void setCursoVigetne(CursoVigente cursoVigente) {
		
		this.cursovigentemateriapk.setCursoVigente(cursoVigente);
		
	}
	
	public Materia getMateria() {
		
		return this.cursovigentemateriapk.getMateria();
		
	}
	
	public void setMateria(Materia materia) {
		
		this.cursovigentemateriapk.setMateria(materia);
		
	}
	
	
	
	

}
