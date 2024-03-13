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

import org.hibernate.annotations.ColumnDefault;

import com.doxacore.modelo.Modelo;


@Entity
@Table(name = "SifenDocumentos")
public class SifenDocumento extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6188877939685219692L;

	@Id
	@Column(name ="sifendocumentoid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long sifendocumentoid;
	
	@Column(columnDefinition="text")
	private String json;
	private String cdc;
	
	@Column(columnDefinition="text")
	private String qr;
	private String estado;
	
	@ColumnDefault("false")
	private boolean enviado = false;
	
	@Column(columnDefinition="text")
	private String respuesta;
	
	@ManyToOne
	@JoinColumn(name="cobranzaid")
	private Cobranza cobranza;
	
	@ColumnDefault("false")
	private boolean cancelado;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date canceladoFecha;
	
	private String canceladoJson;
	
	private String canceladoEstado;

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

	public Long getSifendocumentoid() {
		return sifendocumentoid;
	}

	public void setSifendocumentoid(Long sifendocumentoid) {
		this.sifendocumentoid = sifendocumentoid;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getCdc() {
		return cdc;
	}

	public void setCdc(String cdc) {
		this.cdc = cdc;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}

	public String getCanceladoJson() {
		return canceladoJson;
	}

	public void setCanceladoJson(String canceladoJson) {
		this.canceladoJson = canceladoJson;
	}

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}

	public Date getCanceladoFecha() {
		return canceladoFecha;
	}

	public void setCanceladoFecha(Date canceladoFecha) {
		this.canceladoFecha = canceladoFecha;
	}

	public String getCanceladoEstado() {
		return canceladoEstado;
	}

	public void setCanceladoEstado(String canceladoEstado) {
		this.canceladoEstado = canceladoEstado;
	}
	

}
