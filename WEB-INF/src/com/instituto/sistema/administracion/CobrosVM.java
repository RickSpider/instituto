package com.instituto.sistema.administracion;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.instituto.fe.model.Contribuyente;
import com.instituto.fe.model.EventoCancelar;
import com.instituto.fe.util.MetodosCE;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.modelo.SifenDocumento;
import com.instituto.sistema.reporte.ReporteKudePDF;
import com.instituto.util.EmailServiceModoboa;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;
import com.instituto.util.UtilLocalMetodo;

import net.sf.jasperreports.engine.JRException;

public class CobrosVM extends TemplateViewModelLocal{

	private List<Object[]> lCobranzas;
	private List<Object[]> lCobranzasOri;
	private boolean opAnularCobro;
	
	@Init(superclass = true)
	public void initCobrosVM() {
		
		cargarCobros();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeCobrosVM() {

	}
	
	@Override
	protected void inicializarOperaciones() {
		
		this.opAnularCobro = this.operacionHabilitada(ParamsLocal.OP_ANULAR_COBRO);
		
	}
	
	private void cargarCobros() {

		String sql = this.um.getSql("cobros.sql");
		
		this.lCobranzas = this.reg.sqlNativo(sql);
		//this.lCobranzas = this.reg.getAllObjectsByCondicionOrder(Cobranza.class.getName(), null, "cobranzaid desc");
		this.lCobranzasOri = this.lCobranzas;

	}
	
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[8]; 

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}
	
	@Command
	@NotifyChange("lCobranzas")
	public void filtrarCobranzas() {

		this.lCobranzas = this.filtrarListaObject(this.filtroColumns, this.lCobranzasOri);

	}
	
	@Command
	public void anularCobranzaConfirmacion(@BindingParam("cobranzaid") Long cobranzaid) {
		
		if (!this.opAnularCobro)
			return;
		
		Cobranza cobranza = this.reg.getObjectById(Cobranza.class.getName(), cobranzaid);
		
		if(cobranza.isAnulado()) {
			
			this.mensajeError("El cobro ya esta anulado");
			return;
			
		}
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					anularCobranza(cobranza);
					
				}
				
			}

		};
		
		this.mensajeEliminar("Anular "+cobranza.getComprobanteTipo().getTipo()+" Nº "+cobranza.getComprobanteNum()+", una vez anulado no se podra revertir el proceso. \n Continuar?!", event);
	}
	
	private void anularCobranza(Cobranza cobranza) {
		
		/*SifenDocumento sd = null ;
		
		if (cobranza.isComprobanteElectronico()) {
			
			sd = this.reg.getObjectByCondicion(SifenDocumento.class.getName(), "cobranzaid = "+cobranza.getCobranzaid());
			
			if (sd != null) {
				
				if (sd.getCdc() == null || sd.getCdc().length() == 0) {
					
					this.mensajeError("El comprobante electronico no posee CDC, no se puede anular.");
					return;
					
				}
				
			}
			
		}*/
		
		List<CobranzaDetalle> lDetalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(), "cobranzaid = "+cobranza.getCobranzaid(), null);
		
		for (CobranzaDetalle x : lDetalles) {
			
			EstadoCuenta ec = x.getEstadoCuenta();
			
			if (cobranza.getCondicionVentaTipo() == null  || cobranza.getCondicionVentaTipo().getSigla().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CREDITO) != 0 ) {
				
				ec.setPago(ec.getPago() - x.getMonto());
				ec.setMontoDescuento(ec.getMontoDescuento() - x.getMontoDescuento());
				
				
				
			}else {
				
				if (ec.getPago() == 0) {
					
					ec.setMonto(0);
					
				}else {
					

					this.mensajeError("No se puede anular, ya existen pagos realizados.");					
					return;
					
					
				}
				
			}
			
			this.save(ec);
			
			
		}
		
		cobranza.setAnulado(true);
		cobranza.setFechaAnulacion(new Date());
		cobranza.setUsuarioAnulacion(this.getCurrentUser().getAccount());
		
		this.save(cobranza);
		
		this.cargarCobros();
		BindUtils.postNotifyChange(null,null,this,"lCobranzas");
		
		if (cobranza.isComprobanteElectronico()) {
			
			SifenDocumento sd = this.reg.getObjectByCondicion(SifenDocumento.class.getName(), "cobranzaid = "+cobranza.getCobranzaid());
			
			if (sd != null ) {
				
				
				if (sd.getCdc() == null || sd.getCdc().length() == 0) {
		
					sd.setEstado("Anulado antes del envio");
					sd.setCancelado(true);
					sd.setEnviado(true);
					
				}else{
				
					Contribuyente c = new Contribuyente();
					c.setContribuyenteid(Long.parseLong(this.getSistemaPropiedad("FE_ID").getValor()));
					c.setPass(this.getSistemaPropiedad("FE_PASS").getValor());
					
					EventoCancelar eventoC = new EventoCancelar();
					eventoC.setContribuyente(c);
					eventoC.setFecha(new Date());
					eventoC.setCdc(sd.getCdc());
					
					MetodosCE mce = new MetodosCE();
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
					
					sd.setCanceladoFecha(eventoC.getFecha());
					sd.setCanceladoJson(gson.toJson(eventoC));
					
					this.save(sd);
					
					String link = this.getSistemaPropiedad("FE_HOST").getValor()+MetodosCE.EVENTO_CANCELAR_FACTURA;
					
					mce.enviarJson(link, sd.getCanceladoJson());
				
				}
				
			}
			
		}
		
	}
	
	@Command
	public void verComprobante(@BindingParam("cobranzaid") Long cobranzaid) {
		
		Cobranza cobranza = this.reg.getObjectById(Cobranza.class.getName(), cobranzaid);
		
		if (cobranza.isComprobanteElectronico()) {
			
			if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
				
				Executions.getCurrent().sendRedirect("/instituto/zul/administracion/kudeReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
				
			}
			
		}else {
		
		
			if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) == 0) {
				
				Executions.getCurrent().sendRedirect("/instituto/zul/administracion/reciboReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
				
			}
			
			if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
				
				Executions.getCurrent().sendRedirect("/instituto/zul/administracion/facturaReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
				
			}
		}

	}
	
	
	public void enviaremailFEConfirmacion(@BindingParam("cobranzaid") long id) {
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					enviarEmailFE(id);
					
				}
				
			}

		};
		
		this.mensajeSiNo("Se enviara nuevamente el correo electronico.\nContinuar?", "Enviar Correo", event);
		
	}
	

	public void enviarEmailFE(@BindingParam("cobranzaid") long id) throws KeyManagementException, NoSuchAlgorithmException, IOException, JRException {
				
		UtilLocalMetodo ulm = new UtilLocalMetodo();
		
		String email = ulm.getEmailCobranza(id);
		
		if (email == null) {
						
			this.mensajeError("No hay correo cargado para el envio.");
			return;
			
		}
		
		//email="rrgi89@hotmail.com";
		
		EmailServiceModoboa esm = new EmailServiceModoboa(this.getSistemaPropiedad("EMAIL_HOST").getValor(),this.getSistemaPropiedad("EMAIL_USER").getValor(),this.getSistemaPropiedad("EMAIL_PASS").getValor());
		
		ReporteKudePDF rk = new ReporteKudePDF(id, "kude"+id);
		
		esm.sent(email, "Facturacion Electronica", "El archivo adjunto es una representacion grafica del Documento Electronico.", rk.getPDF());
		
	}

	public boolean isOpAnularCobro() {
		return opAnularCobro;
	}

	public void setOpAnularCobro(boolean opAnularCobro) {
		this.opAnularCobro = opAnularCobro;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public List<Object[]> getlCobranzas() {
		return lCobranzas;
	}

	public void setlCobranzas(List<Object[]> lCobranzas) {
		this.lCobranzas = lCobranzas;
	}


}
