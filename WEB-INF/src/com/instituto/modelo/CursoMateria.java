package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="CursosMaterias")
public class CursoMateria extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8749801252642631573L;
	
	@EmbeddedId
	private CursoMateriaPK cursomateriapk = new CursoMateriaPK();

	public CursoMateriaPK getCursomateriapk() {
		return cursomateriapk;
	}

	public void setCursomateriapk(CursoMateriaPK cursomateriapk) {
		this.cursomateriapk = cursomateriapk;
	}
	
	public Curso getCurso() {
		
		return this.cursomateriapk.getCurso();
		
	}
	
	public void setCurso(Curso curso) {
		
		this.cursomateriapk.setCurso(curso);
		
	}
	
	public Materia getMateria() {
		
		return this.cursomateriapk.getMateria();
	}
	
	public void setMateria(Materia materia) {
		
		this.cursomateriapk.setMateria(materia);
		
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
	

}
