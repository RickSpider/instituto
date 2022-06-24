package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="CursosVigentesAlumnos")
public class CursoVigenteAlumno extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -868565793620497625L;

	@EmbeddedId
	private CursoVigenteAlumnoPK cursovigentealumnopk = new CursoVigenteAlumnoPK();

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CursoVigenteAlumnoPK getCursovigentealumnopk() {
		return cursovigentealumnopk;
	}

	public void setCursovigentealumnopk(CursoVigenteAlumnoPK cursovigentealumnopk) {
		this.cursovigentealumnopk = cursovigentealumnopk;
	}
	
	public Alumno getAlumno() {
		
		return this.cursovigentealumnopk.getAlumno();
		
	}
	
	public void setAlumno(Alumno alumno) {
		
		this.cursovigentealumnopk.setAlumno(alumno);
		
	}
	
	public CursoVigente getCursoVigente() {
		
		return this.cursovigentealumnopk.getCursoVigente();
		
	}
	
	public void setCursoVigente(CursoVigente cursoVigente) {
		
		this.cursovigentealumnopk.setCursoVigente(cursoVigente);
		
	}

	@Override
	public Object[] getArrayObjectDatos() {
		
		Object[] o = {this.getAlumno().getPersona().getApellido()+", "+this.getAlumno().getPersona().getNombre(), this.getAlumno().getPersona().getDocumentoNum()};
		
		return o;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
