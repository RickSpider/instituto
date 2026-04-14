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
@Table(name = "notacds")
public class NotaCD extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2978065344930808601L;

	@Id
	@Column(name ="notacdid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long notacdid;
	
    @Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha = new Date();
    
    @ManyToOne
	@JoinColumn(name = "comprobanteTipoid")
	private Tipo comprobanteTipo;
    
    @ManyToOne
   	@JoinColumn(name = "personaid")
   	private Persona persona;
    
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
	private String comprobanteNum;
	private Date comprobanteEmision;
	private Date comprobanteVencimiento;
	
	
	@ManyToOne
	@JoinColumn(name = "cobranzaid")
	private Cobranza cobranza;
	
	@ManyToOne
	@JoinColumn(name = "sifendocumentoid")
	private SifenDocumento sifenDocumento;
	
	@ColumnDefault("false")
	private boolean comprobanteElectronico;
	
	private String cdcAsociado;
	
	//Doc Fisico
	private Long docAsocTimbrado;
	private Date docAsocEmision;
	private Date docAsocNro;
	
	@ColumnDefault("false")
	private boolean anulado=false;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaAnulacion;
	private String usuarioAnulacion;
	
	@OrderBy("notacddetalleid ASC")
	@OneToMany(mappedBy = "notacd", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NotaCDDetalle> detalles = new ArrayList<NotaCDDetalle>();
	
	
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

	
	public Long getNotacdid() {
		return notacdid;
	}

	public void setNotacdid(Long notacdid) {
		this.notacdid = notacdid;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
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

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}

	public boolean isComprobanteElectronico() {
		return comprobanteElectronico;
	}

	public void setComprobanteElectronico(boolean comprobanteElectronico) {
		this.comprobanteElectronico = comprobanteElectronico;
	}

	public String getCdcAsociado() {
		return cdcAsociado;
	}

	public void setCdcAsociado(String cdcAsociado) {
		this.cdcAsociado = cdcAsociado;
	}

	public Long getDocAsocTimbrado() {
		return docAsocTimbrado;
	}

	public void setDocAsocTimbrado(Long docAsocTimbrado) {
		this.docAsocTimbrado = docAsocTimbrado;
	}

	public Date getDocAsocEmision() {
		return docAsocEmision;
	}

	public void setDocAsocEmision(Date docAsocEmision) {
		this.docAsocEmision = docAsocEmision;
	}

	public Date getDocAsocNro() {
		return docAsocNro;
	}

	public void setDocAsocNro(Date docAsocNro) {
		this.docAsocNro = docAsocNro;
	}

	public String getComprobanteNum() {
		return comprobanteNum;
	}

	public void setComprobanteNum(String comprobanteNum) {
		this.comprobanteNum = comprobanteNum;
	}

	public SifenDocumento getSifenDocumento() {
		return sifenDocumento;
	}

	public void setSifenDocumento(SifenDocumento sifenDocumento) {
		this.sifenDocumento = sifenDocumento;
	}

	public List<NotaCDDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<NotaCDDetalle> detalles) {
		this.detalles = detalles;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	

}
