package com.instituto.sistema.administracion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.modelo.Tipo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.instituto.fe.model.Contribuyente;
import com.instituto.fe.model.EventoCancelar;
import com.instituto.fe.util.MetodosCE;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.Comprobante;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.modelo.NotaCD;
import com.instituto.modelo.NotaCDDetalle;
import com.instituto.modelo.SifenDocumento;
import com.instituto.modelo.UsuarioSede;
import com.instituto.serchModel.SifenDocumentoSearchModel;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class NotaCreditoVM extends TemplateViewModelLocal{
	
	
	private List<Object[]> lNotacds;
	private List<Object[]> lNotacdsOri;
	private NotaCD notacdSelected;

	private boolean opCrearNotacd;
	private boolean opEditarNotacd;
	private boolean opBorrarNotacd;

	@Init(superclass = true)
	public void initNotacdVM() {

		this.cargarNotacds();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeNotacdVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearNotacd = this.operacionHabilitada(ParamsLocal.OP_CREAR_NOTACD);
		this.opEditarNotacd = this.operacionHabilitada(ParamsLocal.OP_EDITAR_NOTACD);
		this.opBorrarNotacd = this.operacionHabilitada(ParamsLocal.OP_BORRAR_NOTACD);

	}

	private void cargarNotacds() {

		this.lNotacds = this.reg.sqlNativo(this.um.getSql("notacd/listaNotacd.sql"));
		this.lNotacdsOri = this.lNotacds;
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[8]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}
	
	@Command
	@NotifyChange("lNotacdes")
	public void filtrarNotacd() {

		this.lNotacds = this.filtrarListaObject(this.filtroColumns, this.lNotacdsOri);

	}

	// fin seccion
	
	//seccion modal
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalNotacdAgregar() {

		if(!this.opCrearNotacd)
			return;

		this.editar = false;
		modalNotacd(-1);

	}

	@Command
	public void modalNotacd(@BindingParam("notacdid") long notacdid) {
		
		
		
		this.sifenDocumentoSearchModelSelected = null;
		
		this.generarSearchModels();

		if (notacdid != -1) {

			if(!this.opEditarNotacd)
				return;
			
			this.notacdSelected = this.reg.getObjectById(NotaCD.class.getName(), notacdid);
			this.editar = true;

		} else {
			
			notacdSelected = new NotaCD();
			this.notacdSelected.setComprobanteTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_NOTACREDITO));
			
			if (!this.existeComprobante()) {

				this.mensajeInfo("No hay comprobante vigente para esta operacion.");
				return;

			}

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/notaCreditoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private boolean existeComprobante() {

		UsuarioSede us = this.getCurrentUsuarioSede();

		Comprobante comprobante = this.reg.getObjectByCondicion(Comprobante.class.getName(),
				"activo = true " + "AND sedeid = " + us.getSede().getSedeid() + " " + "AND comprobantetipoid = "
						+ this.notacdSelected.getComprobanteTipo().getTipoid() + " " + "AND puntoExpdicion = '"
						+ us.getPuntoExpedicion() + "' " + "AND emision <= current_date "
						+ "AND vencimiento >= current_date " + "AND siguiente <= fin ");

		if (comprobante == null) {

			return false;
		}

		return true;

	}

	
	private synchronized Object[] getNumeroComprobante() {

		UsuarioSede us = this.getCurrentUsuarioSede();

		StringBuffer numero = new StringBuffer();
		Object[] out = new Object[5];

		Comprobante comprobante = this.reg.getObjectByCondicion(Comprobante.class.getName(),
				"activo = true " + "AND sedeid = " + us.getSede().getSedeid() + " " + "AND comprobantetipoid = "
						+ this.notacdSelected.getComprobanteTipo().getTipoid() + " " + "AND puntoExpdicion = '"
						+ us.getPuntoExpedicion() + "' " + "AND emision <= current_date "
						+ "AND vencimiento >= current_date " + "AND siguiente <= fin ");

		numero.append(this.getCurrentSede().getEstablecimiento() + "-" + comprobante.getPuntoExpdicion() + "-");

		for (int i = 0; i < 7 - comprobante.getSiguiente().toString().length(); i++) {

			numero.append("0");

		}

		numero.append(comprobante.getSiguiente());

		
		out[0] = comprobante.getTimbrado();
		out[1] = comprobante.getEmision();
		out[2] = comprobante.getVencimiento();
		out[3] = numero;
		
		//envia campo si es electronico
		out[4] = comprobante.isElectronico();

		comprobante.setSiguiente(comprobante.getSiguiente() + 1);

		this.save(comprobante);

		return out;
	}
	
	@Command
	@NotifyChange("lNotacds")
	public void guardar() {
		
		Object[] comprobante = this.getNumeroComprobante();

		this.notacdSelected.setTimbrado((Long) comprobante[0]);

		this.notacdSelected.setComprobanteEmision((Date) comprobante[1]);
		this.notacdSelected.setComprobanteVencimiento((Date) comprobante[2]);

		this.notacdSelected.setComprobanteNum(comprobante[3].toString());

		this.notacdSelected = this.save(this.notacdSelected);
		
		for (NotaCDDetalle x : this.notacdSelected.getDetalles()) {
			
			if (x.getEstadoCuenta() != null) {
				
				EstadoCuenta ec = x.getEstadoCuenta();
				ec.setPago(ec.getPago()-x.getMonto());
								
				this.save(ec);
			}
			
		}
		
		MetodosCE ctce = new MetodosCE();
		
		SifenDocumento sd = ctce.convertNCDAndSend(this.notacdSelected.getNotacdid(), this.getCurrentSede().getSedeid());
		
		if (sd == null) {
			
			this.mensajeError("Hubo un error al envio del documento al servidor, intente luego");
			
		}
		
		this.cargarNotacds();
		this.modal.detach();
	}

	private ListModelArray<SifenDocumentoSearchModel> lSifenDocumentoSearchModel;
	private SifenDocumentoSearchModel sifenDocumentoSearchModelSelected;
	
	private void generarSearchModels() {
	    this.lSifenDocumentoSearchModel = crearSearchModel(
	        this.um.getSql("notacd/buscarComprobante.sql"),
	        o -> new SifenDocumentoSearchModel(
	        	Long.parseLong(o[0].toString()),
	            Long.parseLong(o[1].toString()),
	            (Date) o[2],
	            o[3].toString(),
	            o[4].toString(),
	            o[5].toString(),
	            Double.parseDouble(o[6].toString()),
	            Long.parseLong(o[7].toString())
	        )
	    );
	}
	
	private <T> ListModelArray<T> crearSearchModel(String sql, java.util.function.Function<Object[], T> mapper) {
	    List<Object[]> resultados = this.reg.sqlNativo(sql);
	    List<T> lista = new ArrayList<>(resultados.size());

	    for (Object[] fila : resultados) {
	        lista.add(mapper.apply(fila));
	    }

	    ListModelArray<T> modelo = new ListModelArray<>(lista);
	    return modelo;
	}
	
	@Command
	@NotifyChange("notacdSelected")
	public void onSelectedSifenComprogante() {
		
		if (this.sifenDocumentoSearchModelSelected != null) {
			
			SifenDocumento sd = this.reg.getObjectById(SifenDocumento.class.getName(), this.sifenDocumentoSearchModelSelected.getId());
			
			this.notacdSelected.setCobranza(sd.getCobranza());
			this.notacdSelected.setCdcAsociado(sd.getCdc());
			this.notacdSelected.setRuc(sd.getCobranza().getRuc());
			this.notacdSelected.setRazonSocial(sd.getCobranza().getRazonSocial());
			this.notacdSelected.setSifenDocumento(sd);
			this.notacdSelected.setPersona(sd.getCobranza().getPersona());			
			this.notacdSelected.setComprobanteElectronico(true);
			this.notacdSelected.setTotalDetalle(sd.getCobranza().getTotalDetalle());
			
			List<CobranzaDetalle> lDetalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(),
					"cobranzaid = " + sd.getCobranza().getCobranzaid(), null);
			
			for (CobranzaDetalle x : lDetalles ) {
				
				NotaCDDetalle ncdd= new NotaCDDetalle();
				ncdd.setNotacd(this.notacdSelected);
				ncdd.setEstadoCuenta(Optional.ofNullable(x.getEstadoCuenta()).orElse(null));
				ncdd.setDescripcion(Optional.ofNullable(x.getDescripcion()).orElse(null));
				ncdd.setServicio(Optional.ofNullable(x.getServicio()).orElse(null));
				ncdd.setExento(Optional.ofNullable(x.getExento()).orElse(null));
				ncdd.setIva10(Optional.ofNullable(x.getIva10()).orElse(null));
				ncdd.setIva5(Optional.ofNullable(x.getIva5()).orElse(null));
				ncdd.setMonto(x.getMonto());
				this.notacdSelected.getDetalles().add(ncdd);
				
			}
						
		}else {
			
			this.notacdSelected = new NotaCD();
			this.notacdSelected.setComprobanteTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_NOTACREDITO));
		}
		
	}
	
	@Command
	public void anularNotaCreditoConfirmacion(@BindingParam("id") Long notacdid) {
		
		/*if (!this.opAnularn)
			return;*/
		
		NotaCD notacd = this.reg.getObjectById(NotaCD.class.getName(), notacdid);
		
		
		
		if(notacd.isAnulado()) {
			
			this.mensajeError("La Nota de Credito ya esta anulada");
			return;
			
		}
		
		
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					anularNotaCredito(notacd);
					
				}
				
			}

		};
		
		this.mensajeEliminar("Anular "+notacd.getComprobanteTipo().getTipo()+" Nº "+notacd.getComprobanteNum()+", una vez anulado no se podra revertir el proceso. \n Continuar?!", event);
	}
	
	@Command
	public void anularNotaCredito(NotaCD notacd) {
		
		SifenDocumento sd = this.reg.getObjectByCondicion(SifenDocumento.class.getName(), "notacdid = "+notacd.getNotacdid());
		
		if (sd.getCdc() != null) {
			
			if (sd.getEstado() == null || sd.getEstado().compareTo("Pendiente") == 0) {
				
				 this.mensajeInfo("Verifica que el estado del Comprobante electronico sea Aprobado o Rechazado antes de Cancelar");
				
				return;
				
			}
			
			
		}
		
		List<NotaCDDetalle> ncdDetalles = this.reg.getAllObjectsByCondicionOrder(NotaCDDetalle.class.getName(), "notacdid = "+notacd.getNotacdid(),null);
		
		for (NotaCDDetalle x : ncdDetalles) {
			
			EstadoCuenta ec = x.getEstadoCuenta();
			ec.setPago(ec.getPago()+x.getMonto());
			this.save(ec);
			
		}
		
		notacd.setAnulado(true);
		notacd.setFechaAnulacion(new Date());
		notacd.setUsuarioAnulacion(this.getCurrentUser().getAccount());
		
		this.save(notacd);
		
		if (sd.getCdc() == null || sd.getCdc().length() == 0) {
		
			sd.setCanceladoFecha(new Date());
			sd.setCancelado(true);
			sd.setCanceladoEstado("Cancelado antes del envio");
			
			this.save(sd);
			
		}else if (sd.getEstado().compareTo("Rechazado") == 0 ) {
			
			sd.setCanceladoFecha(new Date());
			sd.setCancelado(true);
			sd.setCanceladoEstado("Cancelado por Rechazo");
			
		}else {
			
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
			sd.setCancelado(true);
			
			this.save(sd);
			
			String link = this.getSistemaPropiedad("FE_HOST").getValor()+MetodosCE.EVENTO_CANCELAR_NOTACREDITO;
			
			mce.enviarJson(link, sd.getCanceladoJson());
			
		
		}
		
	}

	public List<Object[]> getlNotacds() {
		return lNotacds;
	}

	public void setlNotacds(List<Object[]> lNotacds) {
		this.lNotacds = lNotacds;
	}

	public NotaCD getNotacdSelected() {
		return notacdSelected;
	}

	public void setNotacdSelected(NotaCD notacdSelected) {
		this.notacdSelected = notacdSelected;
	}

	public boolean isOpCrearNotacd() {
		return opCrearNotacd;
	}

	public void setOpCrearNotacd(boolean opCrearNotacd) {
		this.opCrearNotacd = opCrearNotacd;
	}

	public boolean isOpEditarNotacd() {
		return opEditarNotacd;
	}

	public void setOpEditarNotacd(boolean opEditarNotacd) {
		this.opEditarNotacd = opEditarNotacd;
	}

	public boolean isOpBorrarNotacd() {
		return opBorrarNotacd;
	}

	public void setOpBorrarNotacd(boolean opBorrarNotacd) {
		this.opBorrarNotacd = opBorrarNotacd;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public Window getModal() {
		return modal;
	}

	public void setModal(Window modal) {
		this.modal = modal;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public ListModelArray<SifenDocumentoSearchModel> getlSifenDocumentoSearchModel() {
		return lSifenDocumentoSearchModel;
	}

	public void setlSifenDocumentoSearchModel(ListModelArray<SifenDocumentoSearchModel> lSifenDocumentoSearchModel) {
		this.lSifenDocumentoSearchModel = lSifenDocumentoSearchModel;
	}

	public SifenDocumentoSearchModel getSifenDocumentoSearchModelSelected() {
		return sifenDocumentoSearchModelSelected;
	}

	public void setSifenDocumentoSearchModelSelected(SifenDocumentoSearchModel sifenDocumentoSearchModelSelected) {
		this.sifenDocumentoSearchModelSelected = sifenDocumentoSearchModelSelected;
	}


	
	

}
