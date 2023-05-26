package com.instituto.sistema.administracion;

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

import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CobrosVM extends TemplateViewModelLocal{

	private List<Cobranza> lCobranzas;
	private List<Cobranza> lCobranzasOri;
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

		this.lCobranzas = this.reg.getAllObjectsByCondicionOrder(Cobranza.class.getName(), null, "cobranzaid desc");
		this.lCobranzasOri = this.lCobranzas;

	}
	
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[7]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}
	
	@Command
	@NotifyChange("lCobranzas")
	public void filtrarCobranzas() {

		this.lCobranzas = this.filtrarLT(this.filtroColumns, this.lCobranzasOri);

	}
	
	@Command
	public void anularCobranzaConfirmacion(@BindingParam("cobranza") Cobranza cobranza) {
		
		if (!this.opAnularCobro)
			return;
		
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
		
		this.mensajeEliminar("Anular "+cobranza.getComprobanteTipo().getTipo()+" Nº "+cobranza.getComprobanteNum()+", una vez anulado no se podra revertir el proceso. \n Continuar?", event);
	}
	
	private void anularCobranza(Cobranza cobranza) {
		
		List<CobranzaDetalle> lDetalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(), "cobranzaid = "+cobranza.getCobranzaid(), null);
		
		for (CobranzaDetalle x : lDetalles) {
			
			EstadoCuenta ec = x.getEstadoCuenta();
			
			ec.setPago(ec.getPago() - x.getMonto());
			ec.setMontoDescuento(ec.getMontoDescuento() - x.getMontoDescuento());
			
			this.save(ec);
			
		}
		
		cobranza.setAnulado(true);
		cobranza.setFechaAnulacion(new Date());
		cobranza.setUsuarioAnulacion(this.getCurrentUser().getAccount());
		
		this.save(cobranza);
		
		this.cargarCobros();
		BindUtils.postNotifyChange(null,null,this,"lCobranzas");
	}
	
	@Command
	public void verComprobante(@BindingParam("cobranza") Cobranza cobranza) {
		
		if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) == 0) {
			
			Executions.getCurrent().sendRedirect("/instituto/zul/administracion/reciboReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
			
		}
		
		if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
			
			Executions.getCurrent().sendRedirect("/instituto/zul/administracion/facturaReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
			
		}

	}

	public List<Cobranza> getlCobranzas() {
		return lCobranzas;
	}

	public void setlCobranzas(List<Cobranza> lCobranzas) {
		this.lCobranzas = lCobranzas;
	}

	public List<Cobranza> getlCobranzasOri() {
		return lCobranzasOri;
	}

	public void setlCobranzasOri(List<Cobranza> lCobranzasOri) {
		this.lCobranzasOri = lCobranzasOri;
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


}
