package com.instituto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name ="OrdenesCompras")
public class OrdenCompra extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9048403936963949316L;

	@Id
	@Column(name ="ordencompraid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ordencompraid;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha = new Date();
	
	@Column(name ="fechavencimiento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaVencimiento;
	
	@ManyToOne
	@JoinColumn(name = "proveedorid")
	private Proveedor proveedor;
	
	@ManyToOne
	@JoinColumn(name = "estadotipoid")
	private Tipo estadoTipo;
	
	@ManyToOne
	@JoinColumn(name = "monedaTipoid")
	private Tipo monedaTipo;
	
	@OrderBy("ordencompradetalleid ASC")
	@OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrdenCompraDetalle> detalles = new ArrayList<OrdenCompraDetalle>();
	
	private String descripcion;
	
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

	public Long getOrdencompraid() {
		return ordencompraid;
	}

	public void setOrdencompraid(Long ordencompraid) {
		this.ordencompraid = ordencompraid;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Tipo getEstadoTipo() {
		return estadoTipo;
	}

	public void setEstadoTipo(Tipo estadoTipo) {
		this.estadoTipo = estadoTipo;
	}

	public List<OrdenCompraDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<OrdenCompraDetalle> detalles) {
		this.detalles = detalles;
	}

	public Tipo getMonedaTipo() {
		return monedaTipo;
	}

	public void setMonedaTipo(Tipo monedaTipo) {
		this.monedaTipo = monedaTipo;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
}
