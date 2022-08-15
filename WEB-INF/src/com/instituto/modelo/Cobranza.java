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
@Table(name = "Cobranzas")
public class Cobranza extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9175243805383056607L;
	
	@Id
	@Column(name ="cobranzaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cobranzaid;
	
	@ManyToOne
	@JoinColumn(name = "alumnoid")
	private Alumno alumno;
	
	@ManyToOne
	@JoinColumn(name = "comprobanteTipoid")
	private Tipo comprobanteTipo;
	
	@ManyToOne
	@JoinColumn(name = "condicionVentaTipoid")
	private Tipo condicionVentaTipo;
	
	private String comprobanteNum;
	
	@CreationTimestamp 
    @Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	private String ruc;
	private String razonSocial;
	private String direccion;
	private String telefono;
	
	private double iva10;
	private double iva5;
	private double exento;
	
	private double totalDetalle;
	private double totalDetalleCobro;
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getCobranzaid() {
		return cobranzaid;
	}

	public void setCobranzaid(Long cobranzaid) {
		this.cobranzaid = cobranzaid;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
	}

	public String getComprobanteNum() {
		return comprobanteNum;
	}

	public void setComprobanteNum(String comprobanteNum) {
		this.comprobanteNum = comprobanteNum;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Tipo getCondicionVentaTipo() {
		return condicionVentaTipo;
	}

	public void setCondicionVentaTipo(Tipo condicionVentaTipo) {
		this.condicionVentaTipo = condicionVentaTipo;
	}

	public double getIva10() {
		return iva10;
	}

	public void setIva10(double iva10) {
		this.iva10 = iva10;
	}

	public double getIva5() {
		return iva5;
	}

	public void setIva5(double iva5) {
		this.iva5 = iva5;
	}

	public double getExento() {
		return exento;
	}

	public void setExento(double exento) {
		this.exento = exento;
	}

	public double getTotalDetalle() {
		return totalDetalle;
	}

	public void setTotalDetalle(double totalDetalle) {
		this.totalDetalle = totalDetalle;
	}

	public double getTotalDetalleCobro() {
		return totalDetalleCobro;
	}

	public void setTotalDetalleCobro(double totalDetalleCobro) {
		this.totalDetalleCobro = totalDetalleCobro;
	}

}
