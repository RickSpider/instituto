package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name="ConveniosConceptos")
public class ConvenioConcepto extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7032586821729381562L;
	
	@EmbeddedId
	private ConvenioConceptoPK convenioconeptopk = new ConvenioConceptoPK();
	
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

	public ConvenioConceptoPK getConvenioconeptopk() {
		return convenioconeptopk;
	}

	public void setConvenioconeptopk(ConvenioConceptoPK convenioconeptopk) {
		this.convenioconeptopk = convenioconeptopk;
	}

	public Concepto getConcepto() {
		
		return this.convenioconeptopk.getConcepto();
		
	}
	
	public void setConcepto(Concepto concepto) {
		
		this.convenioconeptopk.setConcepto(concepto);
		
	}
	
	public Convenio getConvenio() {
		
		return this.convenioconeptopk.getConvenio();
		
	}
	
	public void setConvenio(Convenio convenio) {
		
		this.convenioconeptopk.setConvenio(convenio);
		
	}
	
}
