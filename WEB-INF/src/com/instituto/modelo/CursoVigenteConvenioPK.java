package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CursoVigenteConvenioPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5963718263880409209L;

	@ManyToOne
	@JoinColumn(name="cursovigenteid")
	private CursoVigente cursoVigente;
	
	@ManyToOne
	@JoinColumn(name="convenioid")
	private Convenio convenio;

	public CursoVigente getCursoVigente() {
		return cursoVigente;
	}

	public void setCursoVigente(CursoVigente cursoVigente) {
		this.cursoVigente = cursoVigente;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
