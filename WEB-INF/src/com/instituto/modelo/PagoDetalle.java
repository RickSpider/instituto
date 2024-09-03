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
@Table(name ="PagosDetalles")
public class PagoDetalle extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 71861784189570472L;

	@Id
	@Column(name ="pagodetalleid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long pagodetalleid;
	
	@ManyToOne
	@JoinColumn(name = "pagoid", nullable = false)
	private Pago pago;
	
	@ManyToOne
	@JoinColumn(name = "cuentarubroid", nullable = false)
	private CuentaRubro cuentaRubro;
	
	@ManyToOne
	@JoinColumn(name = "impuestotipoid")
	private Tipo impuestoTipo;
	
	private double monto = 0;
	
	@OneToMany(mappedBy = "pagoDetalle", cascade = CascadeType.ALL, fetch = FetchType.EAGER ,orphanRemoval = true)
	private List<PagoDetalleDoc> detallesDoc = new ArrayList<PagoDetalleDoc>();
	
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

	public Long getPagodetalleid() {
		return pagodetalleid;
	}

	public void setPagodetalleid(Long pagodetalleid) {
		this.pagodetalleid = pagodetalleid;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public CuentaRubro getCuentaRubro() {
		return cuentaRubro;
	}

	public void setCuentaRubro(CuentaRubro cuentaRubro) {
		this.cuentaRubro = cuentaRubro;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Tipo getImpuestoTipo() {
		return impuestoTipo;
	}

	public void setImpuestoTipo(Tipo impuestoTipo) {
		this.impuestoTipo = impuestoTipo;
	}

	public List<PagoDetalleDoc> getDetallesDoc() {
		return detallesDoc;
	}

	public void setDetallesDoc(List<PagoDetalleDoc> detallesDoc) {
		this.detallesDoc = detallesDoc;
	}

	
	
}
