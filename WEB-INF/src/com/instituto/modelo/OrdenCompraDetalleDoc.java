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
@Table(name = "OrdenesComprasDetallesDocs")
public class OrdenCompraDetalleDoc extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9160308463307924631L;

	@Id
	@Column(name = "ordencompradetalledocid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ordencompradetalledocid;

	@ManyToOne
	@JoinColumn(name = "ordencompradetalleid", nullable = false)
	private OrdenCompraDetalle ordenCompraDetalle;

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

	public Long getOrdencompradetalledocid() {
		return ordencompradetalledocid;
	}

	public void setOrdencompradetalledocid(Long ordencompradetalledocid) {
		this.ordencompradetalledocid = ordencompradetalledocid;
	}

	public OrdenCompraDetalle getOrdenCompraDetalle() {
		return ordenCompraDetalle;
	}

	public void setOrdenCompraDetalle(OrdenCompraDetalle ordenCompraDetalle) {
		this.ordenCompraDetalle = ordenCompraDetalle;
	}

	public CursoVigenteMateria getCursoVigenteMateria() {
		return cursoVigenteMateria;
	}

	public void setCursoVigenteMateria(CursoVigenteMateria cursoVigenteMateria) {
		this.cursoVigenteMateria = cursoVigenteMateria;
	}

	
}
