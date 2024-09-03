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

import org.hibernate.annotations.ColumnDefault;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name ="Pagos")
public class Pago extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7754328216490615495L;
	
	@Id
	@Column(name ="pagoid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long pagoid;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha = new Date();
	
	@ManyToOne
	@JoinColumn(name = "proveedorid", nullable = false)
	private Proveedor proveedor;
	
	@ManyToOne
	@JoinColumn(name = "ordenCompraid")
	private OrdenCompra ordenCompra;
	
	@ManyToOne
	@JoinColumn(name = "monedaTipoid", nullable = false)
	private Tipo monedaTipo;
	
	private double monedaCambio = 1;
	
	private String descripcion;
	
	@ManyToOne
	@JoinColumn(name = "comprobanteTipoid", nullable = false)
	private Tipo comprobanteTipo;
	
	@ManyToOne
	@JoinColumn(name = "condicionVentaTipoid")
	private Tipo condicionVentaTipo;
	
	private Long timbrado;
	private String comprobanteNum;
	
	@ColumnDefault("false")
	private boolean comprobanteElectronico;
	
	private String cdc;
	
	@ManyToOne
    @JoinColumn(name = "pagoRelacionadoID")
    private Pago pagoRelacionado;

	@OrderBy("pagodetalleid ASC")
	@OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PagoDetalle> detalles = new ArrayList<PagoDetalle>();

	private double total10 = 0;
	private double total5 = 0;
	private double totalExento = 0;
	
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

	public Long getPagoid() {
		return pagoid;
	}

	public void setPagoid(Long pagoid) {
		this.pagoid = pagoid;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public OrdenCompra getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(OrdenCompra ordenCompra) {
		this.ordenCompra = ordenCompra;
	}

	public Tipo getMonedaTipo() {
		return monedaTipo;
	}

	public void setMonedaTipo(Tipo monedaTipo) {
		this.monedaTipo = monedaTipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
	}

	public Tipo getCondicionVentaTipo() {
		return condicionVentaTipo;
	}

	public void setCondicionVentaTipo(Tipo condicionVentaTipo) {
		this.condicionVentaTipo = condicionVentaTipo;
	}

	public Long getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(Long timbrado) {
		this.timbrado = timbrado;
	}

	public String getComprobanteNum() {
		return comprobanteNum;
	}

	public void setComprobanteNum(String comprobanteNum) {
		this.comprobanteNum = comprobanteNum;
	}

	public Pago getPagoRelacionado() {
		return pagoRelacionado;
	}

	public void setPagoRelacionado(Pago pagoRelacionado) {
		this.pagoRelacionado = pagoRelacionado;
	}

	public List<PagoDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<PagoDetalle> detalles) {
		this.detalles = detalles;
	}

	public double getTotal10() {
		return total10;
	}

	public void setTotal10(double total10) {
		this.total10 = total10;
	}

	public double getTotal5() {
		return total5;
	}

	public void setTotal5(double total5) {
		this.total5 = total5;
	}

	public double getTotalExento() {
		return totalExento;
	}

	public void setTotalExento(double totalExento) {
		this.totalExento = totalExento;
	}

	public double getMonedaCambio() {
		return monedaCambio;
	}

	public void setMonedaCambio(double monedaCambio) {
		this.monedaCambio = monedaCambio;
	}

	public boolean isComprobanteElectronico() {
		return comprobanteElectronico;
	}

	public void setComprobanteElectronico(boolean comprobanteElectronico) {
		this.comprobanteElectronico = comprobanteElectronico;
	}

	public String getCdc() {
		return cdc;
	}

	public void setCdc(String cdc) {
		this.cdc = cdc;
	}
	
	

}
