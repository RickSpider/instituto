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
import com.instituto.modelo.Cotizacion;
import com.instituto.util.ParamsLocal;

public class CotizacionVM extends TemplateViewModel {

	private List<Cotizacion> lCotizaciones;
	private List<Cotizacion> lCotizacionesOri;
	private Cotizacion cotizacionSelected;

	private boolean opCrearCotizacion;
	private boolean opEditarCotizacion;
	private boolean opBorrarCotizacion;

	@Init(superclass = true)
	public void initCotizacionVM() {

		cargarCotizaciones();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeCotizacionVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearCotizacion = this.operacionHabilitada(ParamsLocal.OP_CREAR_COTIZACION);
		this.opEditarCotizacion = this.operacionHabilitada(ParamsLocal.OP_EDITAR_COTIZACION);
		this.opBorrarCotizacion = this.operacionHabilitada(ParamsLocal.OP_BORRAR_COTIZACION);

	}

	private void cargarCotizaciones() {

		this.lCotizaciones = this.reg.getAllObjectsByCondicionOrder(Cotizacion.class.getName(), null, "Cotizacionid asc");
		this.lCotizacionesOri = this.lCotizaciones;
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
	@NotifyChange("lCotizaciones")
	public void filtrarCotizacion() {

		this.lCotizaciones = this.filtrarLT(this.filtroColumns, this.lCotizacionesOri);

	}

	// fin seccion
	
	//seccion modal
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalCotizacionAgregar() {

		if(!this.opCrearCotizacion)
			return;

		this.editar = false;
		modalCotizacion(-1);

	}

	@Command
	public void modalCotizacion(@BindingParam("cotizacionid") long cotizacionid) {

		if (cotizacionid != -1) {

			if(!this.opEditarCotizacion)
				return;
			
			this.cotizacionSelected = this.reg.getObjectById(Cotizacion.class.getName(), cotizacionid);
			this.buscarMoneda = this.cotizacionSelected.getMonedaTipo().getTipo();
			this.editar = true;

		} else {
			
			cotizacionSelected = new Cotizacion();
			this.buscarMoneda = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cotizacionModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private boolean verificarCampos() {
		
		return true;
	}	

	@Command
	@NotifyChange("lCotizaciones")
	public void guardar() {
		
		if (!verificarCampos()) {
			return;
		}

		this.save(cotizacionSelected);

		this.cotizacionSelected = null;

		this.cargarCotizaciones();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("La Cotizacion fue Actualizada.");
			this.editar = false;
		}else {
			
			Notification.show("La Cotizacion fue agregada.");
		}
		
		

	}

	
	//fin modal
	
	@Command
	public void borrarCotizacionConfirmacion(@BindingParam("cotizacion") final Cotizacion cotizacion) {
		
		if (!this.opBorrarCotizacion)
			return;
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					borrarCotizacion(cotizacion);
					
				}
				
			}

		};
		
		this.mensajeEliminar("La Cotizacion sera eliminada. \n Continuar?", event);
	}
	
	private void borrarCotizacion (Cotizacion cotizacion) {
		
		this.reg.deleteObject(cotizacion);
		
		this.cargarCotizaciones();
		
		BindUtils.postNotifyChange(null,null,this,"lCotizaciones");
		
	}
	
	// buscarMoneda

		private List<Object[]> lMonedasbuscarOri;
		private List<Object[]> lMonedasBuscar;
		private String buscarMoneda = "";

		@Command
		@NotifyChange("lMonedasBuscar")
		public void filtrarMonedaBuscar() {

			this.lMonedasBuscar = this.filtrarListaObject(buscarMoneda, this.lMonedasbuscarOri);

		}

		@Command
		@NotifyChange("lMonedasBuscar")
		public void generarListaBuscarMoneda() {

			String sqlBuscarMoneda = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
					ParamsLocal.SIGLA_MONEDA);

			this.lMonedasBuscar = this.reg.sqlNativo(sqlBuscarMoneda);
			this.lMonedasbuscarOri = this.lMonedasBuscar;
		}

		@Command
		@NotifyChange("buscarMoneda")
		public void onSelectMoneda(@BindingParam("id") long id) {

			Tipo moneda = this.reg.getObjectById(Tipo.class.getName(), id);
			this.cotizacionSelected.setMonedaTipo(moneda);
			this.buscarMoneda = moneda.getTipo();
		}

	

	public List<Cotizacion> getlCotizaciones() {
		return lCotizaciones;
	}

	public void setlCotizaciones(List<Cotizacion> lCotizaciones) {
		this.lCotizaciones = lCotizaciones;
	}

	public Cotizacion getCotizacionSelected() {
		return cotizacionSelected;
	}

	public void setCotizacionSelected(Cotizacion cotizacionSelected) {
		this.cotizacionSelected = cotizacionSelected;
	}

	public boolean isOpCrearCotizacion() {
		return opCrearCotizacion;
	}

	public void setOpCrearCotizacion(boolean opCrearCotizacion) {
		this.opCrearCotizacion = opCrearCotizacion;
	}

	public boolean isOpEditarCotizacion() {
		return opEditarCotizacion;
	}

	public void setOpEditarCotizacion(boolean opEditarCotizacion) {
		this.opEditarCotizacion = opEditarCotizacion;
	}

	public boolean isOpBorrarCotizacion() {
		return opBorrarCotizacion;
	}

	public void setOpBorrarCotizacion(boolean opBorrarCotizacion) {
		this.opBorrarCotizacion = opBorrarCotizacion;
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

	public List<Object[]> getlMonedasBuscar() {
		return lMonedasBuscar;
	}

	public void setlMonedasBuscar(List<Object[]> lMonedasBuscar) {
		this.lMonedasBuscar = lMonedasBuscar;
	}

	public String getBuscarMoneda() {
		return buscarMoneda;
	}

	public void setBuscarMoneda(String buscarMoneda) {
		this.buscarMoneda = buscarMoneda;
	}
}
