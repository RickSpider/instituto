package com.instituto.serchModel;

import java.util.Date;

public class SifenDocumentoSearchModel {
	
	private long id;
	private long cobranzaid;
	private Date fecha;
	private String comprobanteNum;
	private String ruc;
	private String razonSocial;
	private double total;
	private long comprobanteTipoId;
	
	
	
	public SifenDocumentoSearchModel(long id, long cobranzaid, Date fecha, String comprobanteNum, String ruc,
			String razonSocial, double total, long comprobanteTipoId) {
		super();
		this.id = id;
		this.cobranzaid = cobranzaid;
		this.fecha = fecha;
		this.comprobanteNum = comprobanteNum;
		this.ruc = ruc;
		this.razonSocial = razonSocial;
		this.total = total;
		this.comprobanteTipoId = comprobanteTipoId;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCobranzaid() {
		return cobranzaid;
	}
	public void setCobranzaid(long cobranzaid) {
		this.cobranzaid = cobranzaid;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getComprobanteNum() {
		return comprobanteNum;
	}
	public void setComprobanteNum(String comprobanteNum) {
		this.comprobanteNum = comprobanteNum;
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
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public long getComprobanteTipoId() {
		return comprobanteTipoId;
	}
	public void setComprobanteTipoId(long comprobanteTipoId) {
		this.comprobanteTipoId = comprobanteTipoId;
	}
	public String toString() {
		return this.comprobanteNum;
	}

}
