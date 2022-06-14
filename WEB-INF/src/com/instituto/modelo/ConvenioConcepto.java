package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name = "ConveniosConceptos")
public class ConvenioConcepto extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7032586821729381562L;

	@EmbeddedId
	private ConvenioConceptoPK convenioconceptopk = new ConvenioConceptoPK();

	private boolean porcentaje = false;

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

	public Concepto getConcepto() {

		return this.convenioconceptopk.getConcepto();

	}

	public void setConcepto(Concepto concepto) {

		this.convenioconceptopk.setConcepto(concepto);

	}

	public Convenio getConvenio() {

		return this.convenioconceptopk.getConvenio();

	}

	public void setConvenio(Convenio convenio) {

		this.convenioconceptopk.setConvenio(convenio);

	}

	public ConvenioConceptoPK getConvenioconceptopk() {
		return convenioconceptopk;
	}

	public void setConvenioconceptopk(ConvenioConceptoPK convenioconceptopk) {
		this.convenioconceptopk = convenioconceptopk;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(boolean porcentaje) {
		this.porcentaje = porcentaje;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

}
