package com.instituto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name ="OrdenesComprasDetalles")
public class OrdenCompraDetalle extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5988273733289525302L;
	
	@Id
	@Column(name ="ordencompradetalleid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ordencompradetalleid;
	
	@ManyToOne
	@JoinColumn(name = "ordencompraid", nullable = false)
	private OrdenCompra ordenCompra;
	
	@ManyToOne
	@JoinColumn(name = "cuentarubroid", nullable = false)
	private CuentaRubro cuentaRubro;
	
	private double monto = 0;
	
	@OneToMany(mappedBy = "ordenCompraDetalle", cascade = CascadeType.ALL, fetch = FetchType.EAGER ,orphanRemoval = true)
	private List<OrdenCompraDetalleDoc> detallesDoc = new ArrayList<OrdenCompraDetalleDoc>();
	
	@ManyToOne
	@JoinColumn(name = "impuestotipoid")
	private Tipo impuestoTipo;

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

	public Long getOrdencompradetalleid() {
		return ordencompradetalleid;
	}

	public void setOrdencompradetalleid(Long ordencompradetalleid) {
		this.ordencompradetalleid = ordencompradetalleid;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public OrdenCompra getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(OrdenCompra ordenCompra) {
		this.ordenCompra = ordenCompra;
	}

	public CuentaRubro getCuentaRubro() {
		return cuentaRubro;
	}

	public void setCuentaRubro(CuentaRubro cuentaRubro) {
		this.cuentaRubro = cuentaRubro;
	}

	public List<OrdenCompraDetalleDoc> getDetallesDoc() {
		return detallesDoc;
	}

	public void setDetallesDoc(List<OrdenCompraDetalleDoc> detallesDoc) {
		this.detallesDoc = detallesDoc;
	}

	public Tipo getImpuestoTipo() {
		return impuestoTipo;
	}

	public void setImpuestoTipo(Tipo impuestoTipo) {
		this.impuestoTipo = impuestoTipo;
	}

	
	

}
