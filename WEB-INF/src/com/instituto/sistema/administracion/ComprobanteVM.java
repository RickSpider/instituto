package com.instituto.sistema.administracion;

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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.TemplateViewModel;
import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Tipo;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.Comprobante;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class ComprobanteVM extends TemplateViewModelLocal {

	private List<Comprobante> lComprobantes;
	private List<Comprobante> lComprobantesOri;
	private Comprobante comprobanteSelected;

	private boolean opCrearComprobante;
	private boolean opEditarComprobante;
	private boolean opBorrarComprobante;

	@Init(superclass = true)
	public void initComprobanteVM() {

		cargarComprobantes();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeComprobanteVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearComprobante = this.operacionHabilitada(ParamsLocal.OP_CREAR_COMPROBANTE);
		this.opEditarComprobante = this.operacionHabilitada(ParamsLocal.OP_EDITAR_COMPROBANTE);
		this.opBorrarComprobante = this.operacionHabilitada(ParamsLocal.OP_BORRAR_COMPROBANTE);

	}

	private void cargarComprobantes() {

		this.lComprobantes = this.reg.getAllObjectsByCondicionOrder(Comprobante.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid(), "Comprobanteid asc");
		this.lComprobantesOri = this.lComprobantes;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[5]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lComprobantes")
	public void filtrarComprobante() {

		this.lComprobantes = this.filtrarLT(this.filtroColumns, this.lComprobantesOri);

	}

	// fin seccion
	
	//seccion modal
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalComprobanteAgregar() {

		if(!this.opCrearComprobante)
			return;

		this.editar = false;
		modalComprobante(-1);

	}

	@Command
	public void modalComprobante(@BindingParam("comprobanteid") long comprobanteid) {

		if (comprobanteid != -1) {

			if(!this.opEditarComprobante)
				return;
			
			this.comprobanteSelected = this.reg.getObjectById(Comprobante.class.getName(), comprobanteid);
			this.buscarComprobante = this.comprobanteSelected.getComprobanteTipo().getTipo();
			
			this.editar = true;

		} else {
			
			comprobanteSelected = new Comprobante();
			comprobanteSelected.setSede(this.getCurrentSede());
			this.buscarComprobante = "";
			

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/comprobanteModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private boolean verificarCampos() {
		
		return true;
	}	

	@Command
	@NotifyChange("lComprobantes")
	public void guardar() {
		
		if (!verificarCampos()) {
			return;
		}

		this.save(comprobanteSelected);

		this.comprobanteSelected = null;

		this.cargarComprobantes();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("La Comprobante fue Actualizada.");
			this.editar = false;
		}else {
			
			Notification.show("La Comprobante fue agregada.");
		}
		
		

	}

	
	//fin modal
	
	@Command
	public void borrarComprobanteConfirmacion(@BindingParam("comprobante") final Comprobante comprobante) {
		
		if (!this.opBorrarComprobante)
			return;
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					borrarComprobante(comprobante);
					
				}
				
			}

		};
		
		this.mensajeEliminar("La Comprobante sera eliminada. \n Continuar?", event);
	}
	
	private void borrarComprobante (Comprobante comprobante) {
		
		this.reg.deleteObject(comprobante);
		
		this.cargarComprobantes();
		
		BindUtils.postNotifyChange(null,null,this,"lComprobantes");
		
	}
	
	// buscarTipo

		private List<Object[]> lTiposbuscarOri;
		private List<Object[]> lTiposBuscar;

		private String filtroBuscarTipo;

		private String buscarComprobante = "";

		@Command
		@NotifyChange("lTiposBuscar")
		public void filtrarTipoBuscar() {

			this.lTiposBuscar = this.filtrarListaObject(filtroBuscarTipo, this.lTiposbuscarOri);

		}

		@Command
		@NotifyChange("lTiposBuscar")
		public void generarListaBuscarTipo(String sigla) {

			String sqlBuscarTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", sigla);

			this.lTiposBuscar = this.reg.sqlNativo(sqlBuscarTipo);
			this.lTiposbuscarOri = this.lTiposBuscar;
		}

		@Command
		@NotifyChange("lTiposBuscar")
		public void generarListaBuscarFormaPago() {

			generarListaBuscarTipo(ParamsLocal.SIGLA_FORMA_PAGO);

		}

		@Command
		@NotifyChange("lTiposBuscar")
		public void generarListaBuscarMoneda() {

			generarListaBuscarTipo(ParamsLocal.SIGLA_MONEDA);

		}

		@Command
		@NotifyChange("lTiposBuscar")
		public void generarListaBuscarComprobante() {

			generarListaBuscarTipo(ParamsLocal.SIGLA_COMPROBANTE);

		}
		@Command
		@NotifyChange("buscarComprobante")
		public void onSelectComprobante(@BindingParam("id") long id) {

			Tipo comprobante = this.reg.getObjectById(Tipo.class.getName(), id);
			this.comprobanteSelected.setComprobanteTipo(comprobante);
			this.buscarComprobante = comprobante.getTipo();
			this.filtroBuscarTipo = "";
		}

		// fin buscarTipo
	

	public List<Comprobante> getlComprobantes() {
		return lComprobantes;
	}

	public void setlComprobantes(List<Comprobante> lComprobantes) {
		this.lComprobantes = lComprobantes;
	}

	public Comprobante getComprobanteSelected() {
		return comprobanteSelected;
	}

	public void setComprobanteSelected(Comprobante comprobanteSelected) {
		this.comprobanteSelected = comprobanteSelected;
	}

	public boolean isOpCrearComprobante() {
		return opCrearComprobante;
	}

	public void setOpCrearComprobante(boolean opCrearComprobante) {
		this.opCrearComprobante = opCrearComprobante;
	}

	public boolean isOpEditarComprobante() {
		return opEditarComprobante;
	}

	public void setOpEditarComprobante(boolean opEditarComprobante) {
		this.opEditarComprobante = opEditarComprobante;
	}

	public boolean isOpBorrarComprobante() {
		return opBorrarComprobante;
	}

	public void setOpBorrarComprobante(boolean opBorrarComprobante) {
		this.opBorrarComprobante = opBorrarComprobante;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public List<Object[]> getlTiposBuscar() {
		return lTiposBuscar;
	}

	public void setlTiposBuscar(List<Object[]> lTiposBuscar) {
		this.lTiposBuscar = lTiposBuscar;
	}

	public String getFiltroBuscarTipo() {
		return filtroBuscarTipo;
	}

	public void setFiltroBuscarTipo(String filtroBuscarTipo) {
		this.filtroBuscarTipo = filtroBuscarTipo;
	}

	public String getBuscarComprobante() {
		return buscarComprobante;
	}

	public void setBuscarComprobante(String buscarComprobante) {
		this.buscarComprobante = buscarComprobante;
	}

	
}
