package com.instituto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name="cvadocumentos")
public class CVADocumento extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8260319160382565277L;

	@Id
	@Column(name ="cvadocumentoid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cvadocumentoid;
	
	@ManyToOne
	@JoinColumn(name = "cursovigenteid")
	private CursoVigente cursoVigente;
	
	@ManyToOne
	@JoinColumn(name = "alumnoid")
	private Alumno alumnoid;
	
	@ManyToOne
	@JoinColumn(name = "documentoalumnoid")
	private Tipo documentoAlumnoTipo;


	@ManyToOne
	@JoinColumn(name = "documentoEstadoid")
	private Tipo documentoEstadoTipo;
	
	
	private String observacion;
	
	@OrderBy("cvadocumentodetalleid ASC")
	@OneToMany(mappedBy = "cvadocumento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CVADocumentoDetalle> detalles = new ArrayList<CVADocumentoDetalle>();
	
	public Long getCvadocumentoid() {
		return cvadocumentoid;
	}

	public void setCvadocumentoid(Long cvadocumentoid) {
		this.cvadocumentoid = cvadocumentoid;
	}

	public CursoVigente getCursoVigente() {
		return cursoVigente;
	}

	public void setCursoVigente(CursoVigente cursoVigente) {
		this.cursoVigente = cursoVigente;
	}

	public Alumno getAlumnoid() {
		return alumnoid;
	}

	public void setAlumnoid(Alumno alumnoid) {
		this.alumnoid = alumnoid;
	}

	public Tipo getDocumentoAlumnoTipo() {
		return documentoAlumnoTipo;
	}

	public void setDocumentoAlumnoTipo(Tipo documentoAlumnoTipo) {
		this.documentoAlumnoTipo = documentoAlumnoTipo;
	}
	
	

	public Tipo getDocumentoEstadoTipo() {
		return documentoEstadoTipo;
	}

	public void setDocumentoEstadoTipo(Tipo documentoEstadoTipo) {
		this.documentoEstadoTipo = documentoEstadoTipo;
	}

	public List<CVADocumentoDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<CVADocumentoDetalle> detalles) {
		this.detalles = detalles;
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

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	
	
}
