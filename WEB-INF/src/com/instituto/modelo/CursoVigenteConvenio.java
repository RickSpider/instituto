package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="cursosvigentesconvenios")
public class CursoVigenteConvenio extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6587474923891499023L;
	
	@EmbeddedId
	private CursoVigenteConvenioPK cursovigenteconveniopk = new CursoVigenteConvenioPK();

	
	public CursoVigente getCursoVigente() {
		
		return this.cursovigenteconveniopk.getCursoVigente();
		
	}
	
	public void setCursoVigente(CursoVigente cursoVigente) {
		
		this.cursovigenteconveniopk.setCursoVigente(cursoVigente);
	}
	
	public Convenio getConvenio() {
		
		return this.cursovigenteconveniopk.getConvenio();
		
	}
	
	public void setConvenio(Convenio convenio) {
		
		this.cursovigenteconveniopk.setConvenio(convenio);
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CursoVigenteConvenioPK getCursovigenteconveniopk() {
		return cursovigenteconveniopk;
	}

	public void setCursovigenteconveniopk(CursoVigenteConvenioPK cursovigenteconveniopk) {
		this.cursovigenteconveniopk = cursovigenteconveniopk;
	}
	
	
	

}
