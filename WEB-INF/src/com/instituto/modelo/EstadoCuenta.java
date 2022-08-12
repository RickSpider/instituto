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


@Entity
@Table(name="estadoscuentas")
public class EstadoCuenta extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2438628348177674128L;

	@Id
	@Column(name ="estadocuentaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long estadocuentaid;
	
	@ManyToOne
	@JoinColumn(name = "alumnoid")
	private Alumno alumno;
	
	@ManyToOne
	@JoinColumn(name = "cursovigenteid")
	private CursoVigente cursoVigente;
	
	@ManyToOne
	@JoinColumn(name = "conceptoid")
	private Concepto Concepto;
	
	@Temporal(TemporalType.DATE)
	private Date vencimiento;
	
	private int periodo;
	private double montoDescuento = 0.0;
	private double monto = 0.0;
	private double pago = 0.0;
	
	
	private boolean inactivo = false;
	
    @Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInactivacion;
	private String motivoInactivacion; 
	
	
	public Alumno getAlumno() {
		return alumno;
	}
	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public CursoVigente getCursoVigente() {
		return cursoVigente;
	}
	public void setCursoVigente(CursoVigente cursoVigente) {
		this.cursoVigente = cursoVigente;
	}
	public Concepto getConcepto() {
		return Concepto;
	}
	public void setConcepto(Concepto concepto) {
		Concepto = concepto;
	}
	public Date getVencimiento() {
		return vencimiento;
	}
	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}
	public int getPeriodo() {
		return periodo;
	}
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	public double getMontoDescuento() {
		return montoDescuento;
	}
	public void setMontoDescuento(double montoDescuento) {
		this.montoDescuento = montoDescuento;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public double getPago() {
		return pago;
	}
	public void setPago(double pago) {
		this.pago = pago;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isInactivo() {
		return inactivo;
	}
	public void setInactivo(boolean inactivo) {
		this.inactivo = inactivo;
	}
	public Date getFechaInactivacion() {
		return fechaInactivacion;
	}
	public void setFechaInactivacion(Date fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}
	public String getMotivoInactivacion() {
		return motivoInactivacion;
	}
	public void setMotivoInactivacion(String motivoInactivacion) {
		this.motivoInactivacion = motivoInactivacion;
	}
	public Long getEstadocuentaid() {
		return estadocuentaid;
	}
	public void setEstadocuentaid(Long estadocuentaid) {
		this.estadocuentaid = estadocuentaid;
	}
	
	

}
