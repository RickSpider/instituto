package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ConvenioConceptoPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4594181756449827490L;

	@ManyToOne
	@JoinColumn(name="convenioid")
	private Convenio convenio;
	
	@ManyToOne
	@JoinColumn(name="conceptoid")
	private Concepto concepto;
	
	public Convenio getConvenio() {
		return convenio;
	}
	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
	public Concepto getConcepto() {
		return concepto;
	}
	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
