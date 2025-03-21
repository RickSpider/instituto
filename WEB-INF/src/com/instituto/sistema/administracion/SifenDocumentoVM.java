package com.instituto.sistema.administracion;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.instituto.fe.model.ConsultaCDC;
import com.instituto.fe.util.MetodosCE;
import com.instituto.modelo.SifenDocumento;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class SifenDocumentoVM extends TemplateViewModelLocal{
	
	private SifenDocumento sifenDocumentoSelected;
	
	private List<Object[]> lSifenDocumentos;
	private List<Object[]> lSifenDocumentosOri;
	
	private boolean opCrearSifenDocumento;
	private boolean opEditarSifenDocumento;
	private boolean opBorrarSifenDocumento;
	

	@Init(superclass = true)
	public void initCobrosVM() {
		
		cargarSifenDocumentos();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeCobrosVM() {

	}
	
	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearSifenDocumento = this.operacionHabilitada(ParamsLocal.OP_CREAR_SIFENDOCUMENTO);
		this.opEditarSifenDocumento = this.operacionHabilitada(ParamsLocal.OP_EDITAR_SIFENDOCUMENTO);
		this.opBorrarSifenDocumento = this.operacionHabilitada(ParamsLocal.OP_BORRAR_SIFENDOCUMENTO);
		
	}
	
	private void cargarSifenDocumentos() {

		String sql = this.um.getSql("sifenDocumento/sifenDocumentos.sql");
		
		this.lSifenDocumentos = this.reg.sqlNativo(sql);
		this.lSifenDocumentosOri = this.lSifenDocumentos;

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

		this.lSifenDocumentos = this.filtrarListaObject(this.filtroColumns, this.lSifenDocumentosOri);

	}
	
	
	@Command
	public void openQrSifen(@BindingParam("sifenDocumentoid") long sifenDocumentoid) {
		
		SifenDocumento sd = this.reg.getObjectById(SifenDocumento.class.getName(), sifenDocumentoid);
		
		if (!sd.isEnviado()) {
			
			this.mensajeInfo("El documento no fue enviado, envielo y vuelva a intentar luego de ser aprobado.");
			
			return;
			
		}

		Executions.getCurrent().sendRedirect(sd.getQr(), "_blank");

	}
	
	
	@Command
	@NotifyChange("*")
	public void enviarComprobante(@BindingParam("sifenDocumentoid") long sifenDocumentoid) {
		
		SifenDocumento sd = this.reg.getObjectById(SifenDocumento.class.getName(), sifenDocumentoid);
		
		if (sd.isCancelado()) {
			
			this.mensajeInfo("El documento ya fue cancelado.");
			
			return;
			
		}
		
		if (sd.isEnviado()) {
			
			this.mensajeInfo("El documento ya fue enviado.");
			
			return;
			
		}
		
		MetodosCE ctce = new MetodosCE();
		
		if (sd.getCobranza().getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
			
			SifenDocumento sd2 = ctce.enviarComprobante(sd, MetodosCE.FACTURA);
			
			if (sd2 == null) {
				
				this.mensajeError("Problema al envia, intentelo mas tarde.");
				return;
				
			}else {
				
				this.mensajeInfo("Enviado Correctamente");
				this.cargarSifenDocumentos();
			}
			
		}
	
	}
	
	@Command
	@NotifyChange("*")
	public void consultarComprobante(@BindingParam("sifenDocumentoid")long sifenDocumentoid) {
		
		SifenDocumento sd = this.reg.getObjectById(SifenDocumento.class.getName(), sifenDocumentoid);
		
		if (!sd.isEnviado()) {
			
			this.mensajeInfo("El documento no fue enviado.");
			
			return;
			
		}
		MetodosCE ctce = new MetodosCE();
		
		ConsultaCDC ccdc = ctce.consultarCDC(sd.getCdc());
		
		if (ccdc == null) {
			
			this.mensajeError("Error al consultar, intente mas tarde.");
			return;
		}
		
		sd.setEstado(ccdc.getEstado());
		
		this.save(sd);
		this.cargarSifenDocumentos();		
		
		
	}
	
	@Command
	public void generarKude(@BindingParam("sifenDocumentoid")long sifenDocumentoid) {

		SifenDocumento sd = this.reg.getObjectById(SifenDocumento.class.getName(), sifenDocumentoid);
		
	
		Executions.getCurrent().sendRedirect(
				"/instituto/zul/administracion/kudeReporte.zul?id=" + sd.getCobranza().getCobranzaid(),
				"_blank");

	}
	
	private Window modal;
	
	@Command
	public void modalSifenDocumento(@BindingParam("sifendocumentoid") long sifendocumentoid) {
		
		this.sifenDocumentoSelected = this.reg.getObjectById(SifenDocumento.class.getName(), sifendocumentoid);
		
		/*Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Object jsonObject = gson.fromJson(this.sifenDocumentoSelected.getJson(), Object.class);
        
        this.sifenDocumentoSelected.setJson(gson.toJson(jsonObject));*/
		

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/sifenDocuemntoModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lSifenDocumentos")
	public void guardar() {

		this.save(this.sifenDocumentoSelected);
		
		this.sifenDocumentoSelected = null;
	
		this.modal.detach();
		
		this.cargarSifenDocumentos();
		
		Notification.show("El Documento electronico fue actualizado, reenvielo.");
		
	}
	
	
	public List<Object[]> getlSifenDocumentos() {
		return lSifenDocumentos;
	}

	public void setlSifenDocumentos(List<Object[]> lSifenDocumentos) {
		this.lSifenDocumentos = lSifenDocumentos;
	}

	public boolean isOpCrearSifenDocumento() {
		return opCrearSifenDocumento;
	}

	public void setOpCrearSifenDocumento(boolean opCrearSifenDocumento) {
		this.opCrearSifenDocumento = opCrearSifenDocumento;
	}

	public boolean isOpEditarSifenDocumento() {
		return opEditarSifenDocumento;
	}

	public void setOpEditarSifenDocumento(boolean opEditarSifenDocumento) {
		this.opEditarSifenDocumento = opEditarSifenDocumento;
	}

	public boolean isOpBorrarSifenDocumento() {
		return opBorrarSifenDocumento;
	}

	public void setOpBorrarSifenDocumento(boolean opBorrarSifenDocumento) {
		this.opBorrarSifenDocumento = opBorrarSifenDocumento;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public SifenDocumento getSifenDocumentoSelected() {
		return sifenDocumentoSelected;
	}

	public void setSifenDocumentoSelected(SifenDocumento sifenDocumentoSelected) {
		this.sifenDocumentoSelected = sifenDocumentoSelected;
	}

	

}
