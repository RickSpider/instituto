/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instituto.fe.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author BlackSpider
 */

//por el momento solo soporte para guaranies

public class ComprobanteElectronico {

	// private Long comprobanteid;

	// datos contribuyente
	private Contribuyente contribuyente;

	private Timbrado timbrado;

	private String sucursal;

	// conforme a la moneda en sifen
	private String operacionMoneda;

	private Receptor receptor;

	// private String receptorTipoPersona;

	/**
	 * OBLIGATORIO para comprobante tipo factura electronica y autofactura
	 */
	private Long tipoTransaccion;
	private String descripcionTipoTransaccion;

	// private int tipoImpuesto;

	private Date fecha;

	private Date fechaVencimiento; // para factura credito

	private CondicionOperacion condicionOperacion;

	private ArrayList<ComprobanteElectronicoDetalle> detalles = new ArrayList<ComprobanteElectronicoDetalle>();

	private Transporte transporte;

	private Remision remision;

	private NotaCreditoDebito notaCreditoDebito;

	private ArrayList<DocAsociado> docAsociados;

	private String infoFisco;

	private double totalComprobante;
	private double totalIVA10;
	private double totalIVA5;
	private double totalExcento;

	private String cdc;

	private String motivoEvento;

	public DocAsociado getDocAsociadoObject(){
		DocAsociado out = this.getDocAsociados().get(0);
		return out;
	}

	public Contribuyente getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(Contribuyente contribuyente) {
		this.contribuyente = contribuyente;
	}

	public Timbrado getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(Timbrado timbrado) {
		this.timbrado = timbrado;
	}

	public ArrayList<ComprobanteElectronicoDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<ComprobanteElectronicoDetalle> detalles) {
		this.detalles = detalles;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getOperacionMoneda() {
		return operacionMoneda;
	}

	public void setOperacionMoneda(String operacionMoneda) {
		this.operacionMoneda = operacionMoneda;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getFechaSDF() {
		return new SimpleDateFormat("dd-MM-yyyy").format(fecha);
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Receptor getReceptor() {
		return receptor;
	}

	public void setReceptor(Receptor receptor) {
		this.receptor = receptor;
	}

	public CondicionOperacion getCondicionOperacion() {
		return condicionOperacion;
	}

	public void setCondicionOperacion(CondicionOperacion condicionOperacion) {
		this.condicionOperacion = condicionOperacion;
	}

	public Remision getRemision() {
		return remision;
	}

	public void setRemision(Remision remision) {
		this.remision = remision;
	}

	public Transporte getTransporte() {
		return transporte;
	}

	public void setTransporte(Transporte transporte) {
		this.transporte = transporte;
	}

	public String getInfoFisco() {
		return infoFisco;
	}

	public void setInfoFisco(String infoFisco) {
		this.infoFisco = infoFisco;
	}

	public NotaCreditoDebito getNotaCreditoDebito() {
		return notaCreditoDebito;
	}

	public void setNotaCreditoDebito(NotaCreditoDebito notaCreditoDebito) {
		this.notaCreditoDebito = notaCreditoDebito;
	}

	public ArrayList<DocAsociado> getDocAsociados() {
		return docAsociados;
	}

	public void setDocAsociados(ArrayList<DocAsociado> docAsociados) {
		this.docAsociados = docAsociados;
	}

	public double getTotalComprobante() {
		return totalComprobante;
	}

	public void setTotalComprobante(double totalComprobante) {
		this.totalComprobante = totalComprobante;
	}

	public double getTotalIVA10() {
		return totalIVA10;
	}

	public void setTotalIVA10(double totalIVA10) {
		this.totalIVA10 = totalIVA10;
	}

	public double getTotalIVA5() {
		return totalIVA5;
	}

	public void setTotalIVA5(double totalIVA5) {
		this.totalIVA5 = totalIVA5;
	}

	public double getTotalExcento() {
		return totalExcento;
	}

	public void setTotalExcento(double totalExcento) {
		this.totalExcento = totalExcento;
	}

	public String getCdc() {
		return cdc;
	}

	public void setCdc(String cdc) {
		this.cdc = cdc;
	}

	public String getMotivoEvento() {
		return motivoEvento;
	}

	public void setMotivoEvento(String motivoEvento) {
		this.motivoEvento = motivoEvento;
	}

	public String getFechaVencimiento() {
		return new SimpleDateFormat("dd-MM-yyyy").format(fechaVencimiento);
	}

	/**
	 * Settear solo en casos de que sea factura credito
	 */
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getDescripcionTipoTransaccion() {
		return descripcionTipoTransaccion;
	}

	public void setDescripcionTipoTransaccion(String descripcionTipoTransaccion) {
		this.descripcionTipoTransaccion = descripcionTipoTransaccion;
	}

	public Long getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(Long tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	
	
}