package com.instituto.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name="cvadocumentosdetalles")
public class CVADocumentoDetalle extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5863925915529156324L;
	
	@Id
	@Column(name ="cvadocumentodetalleid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cvadocumentodetalleid;
	
	@ManyToOne
	@JoinColumn(name = "cvadocumentoid")
	private CVADocumento cvadocumento;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name = "documentoEstadoid")
	private Tipo documentoEstadoTipo;
	
	private String observacion;

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
	

	public Long getCvadocumentodetalleid() {
		return cvadocumentodetalleid;
	}

	public void setCvadocumentodetalleid(Long cvadocumentodetalleid) {
		this.cvadocumentodetalleid = cvadocumentodetalleid;
	}

	public CVADocumento getCvadocumento() {
		return cvadocumento;
	}

	public void setCvadocumento(CVADocumento cvadocumento) {
		this.cvadocumento = cvadocumento;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Tipo getDocumentoEstadoTipo() {
		return documentoEstadoTipo;
	}

	public void setDocumentoEstadoTipo(Tipo documentoEstadoTipo) {
		this.documentoEstadoTipo = documentoEstadoTipo;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	
	
	
	
}
