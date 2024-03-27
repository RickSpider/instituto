package com.instituto.fe.model;

public class ConsultaCDC {

	private String cdc;
	private String respuesta;
	private String estado;
	private boolean enviado;
	private boolean envioPorLote;
	private boolean envioLote;
	private String enlaceQR;
	
	public String getCdc() {
		return cdc;
	}
	public void setCdc(String cdc) {
		this.cdc = cdc;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
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
	public boolean isEnvioPorLote() {
		return envioPorLote;
	}
	public void setEnvioPorLote(boolean envioPorLote) {
		this.envioPorLote = envioPorLote;
	}
	public boolean isEnvioLote() {
		return envioLote;
	}
	public void setEnvioLote(boolean envioLote) {
		this.envioLote = envioLote;
	}
	public String getEnlaceQR() {
		return enlaceQR;
	}
	public void setEnlaceQR(String enlaceQR) {
		this.enlaceQR = enlaceQR;
	}
	
}
