package com.instituto.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name = "Cobranzas")
public class Cobranza extends Modelo implements Serializable {
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 9175243805383056607L;
	private static final long serialVersionUID = 9175243805383056608L;
	
	@Id
	@Column(name ="cobranzaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long cobranzaid;
	
	@ManyToOne
	@JoinColumn(name = "personaid")
	private Persona persona;
	
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
	
    @Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
    
	@Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreditoVencimiento;
	
	private String ruc;
	private String razonSocial;
	private String direccion;
	private String telefono;
	
	private double iva10;
	private double iva5;
	private double exento;
	
	private double totalDetalle;
	private double totalDetalleCobro;
	
	private Long timbrado;
	private Date comprobanteEmision;
	private Date comprobanteVencimiento;
	
	@ColumnDefault("false")
	private boolean anulado=false;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaAnulacion;
	private String usuarioAnulacion;
	
	@ManyToOne
	@JoinColumn(name = "cajaid")
	private Caja caja;
	
	@ColumnDefault("false")
	private boolean comprobanteElectronico;
	
	//seccion facturacion electronica
	
	/*@Column(columnDefinition="text")
	private String json;
	private String cdc;
	
	@Column(columnDefinition="text")
	private String qr;
	private String estado;
	
	@ColumnDefault("false")
	private boolean enviado = false;
	
	@Column(columnDefinition="text")
	private String respuesta;*/
	
	
	@Override
	public Object[] getArrayObjectDatos() {
		
		String timbrado = "";
		if (this.timbrado != null) {
			
			timbrado = this.timbrado.toString();

		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String fechaAnulacion = "";
		if (this.fechaAnulacion != null) {
			
			fechaAnulacion = sdf.format(this.fechaAnulacion);
			
		}

		Object[] o = {sdf.format(this.fecha), this.comprobanteTipo.getTipo(), timbrado, this.comprobanteNum, String.valueOf(this.anulado), fechaAnulacion };
		return o;
	}

	@Override
	public String getStringDatos() {
		
		String timbrado = "";
		if (this.timbrado != null) {
			
			timbrado = this.timbrado.toString();

		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fechaAnulacion = "";
		if (this.fechaAnulacion != null) {
			
			fechaAnulacion = sdf.format(this.fechaAnulacion);
			
		}

		
		return this.cobranzaid+" "+sdf.format(this.fecha)+" "+this.comprobanteTipo.getTipo()+" "+ timbrado+" "+ this.comprobanteNum+" "+ String.valueOf(this.anulado)+" "+ fechaAnulacion;
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

	public Long getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(Long timbrado) {
		this.timbrado = timbrado;
	}

	public Date getComprobanteEmision() {
		return comprobanteEmision;
	}

	public void setComprobanteEmision(Date comprobanteEmision) {
		this.comprobanteEmision = comprobanteEmision;
	}

	public Date getComprobanteVencimiento() {
		return comprobanteVencimiento;
	}

	public void setComprobanteVencimiento(Date comprobanteVencimiento) {
		this.comprobanteVencimiento = comprobanteVencimiento;
	}

	public boolean isAnulado() {
		return anulado;
	}

	public void setAnulado(boolean anulado) {
		this.anulado = anulado;
	}

	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public String getUsuarioAnulacion() {
		return usuarioAnulacion;
	}

	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Date getFechaCreditoVencimiento() {
		return fechaCreditoVencimiento;
	}

	public void setFechaCreditoVencimiento(Date fechaCreditoVencimiento) {
		this.fechaCreditoVencimiento = fechaCreditoVencimiento;
	}

	public boolean isComprobanteElectronico() {
		return comprobanteElectronico;
	}

	public void setComprobanteElectronico(boolean comprobanteElectronico) {
		this.comprobanteElectronico = comprobanteElectronico;
	}

	

}
