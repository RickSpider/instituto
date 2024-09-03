package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;

@Entity
@Table(name ="PagosDetallesDocs")
public class PagoDetalleDoc extends Modelo implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6498346887045147338L;

	@Id
	@Column(name ="pagodetalledocid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long pagodetalledocid;
	
	@ManyToOne
	@JoinColumn(name = "pagodetalleid", nullable = false)
	private PagoDetalle pagoDetalle;
	
	@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "cursovigenteid", referencedColumnName = "cursovigenteid"),
        @JoinColumn(name = "materiaid", referencedColumnName = "materiaid")
    })
	private CursoVigenteMateria cursoVigenteMateria;

	
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

	public Long getPagodetalledocid() {
		return pagodetalledocid;
	}

	public void setPagodetalledocid(Long pagodetalledocid) {
		this.pagodetalledocid = pagodetalledocid;
	}

	public PagoDetalle getPagoDetalle() {
		return pagoDetalle;
	}

	public void setPagoDetalle(PagoDetalle pagoDetalle) {
		this.pagoDetalle = pagoDetalle;
	}

	public CursoVigenteMateria getCursoVigenteMateria() {
		return cursoVigenteMateria;
	}

	public void setCursoVigenteMateria(CursoVigenteMateria cursoVigenteMateria) {
		this.cursoVigenteMateria = cursoVigenteMateria;
	}

	
	
	
}
