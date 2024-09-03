package com.instituto.sistema.compra;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.BindingParams;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.instituto.modelo.Presupuesto;
import com.instituto.modelo.PresupuestoDetalle;
import com.instituto.modelo.Rubro;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class PresupuestoVM extends TemplateViewModelLocal {

	private List<Object[]> lPresupuestos;
	private List<Object[]> lPresupuestosOri;
	private Presupuesto presupuestoSelected;

	private boolean opCrearPresupuesto;
	private boolean opEditarPresupuesto;
	private boolean opBorrarPresupuesto;

	@Init(superclass = true)
	public void initPresupuestoVM() {

		// cargarPresupuestos();
		this.presupuestoSelected = new Presupuesto();
		inicializarFiltros();
		this.inicializarFinders();

	}

	@AfterCompose(superclass = true)
	public void afterComposePresupuestoVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearPresupuesto = this.operacionHabilitada(ParamsLocal.OP_CREAR_PRESUPUESTO);
		this.opEditarPresupuesto = this.operacionHabilitada(ParamsLocal.OP_EDITAR_PRESUPUESTO);
		this.opBorrarPresupuesto = this.operacionHabilitada(ParamsLocal.OP_BORRAR_PRESUPUESTO);

	}

	private void cargarPresupuestos() {

		String sqlPresupuesto = this.um.getSql("presupuesto/listaPresupuesto.sql");

		this.lPresupuestos = this.reg.sqlNativo(sqlPresupuesto);
		this.lPresupuestosOri = this.lPresupuestos;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[2]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("*")
	public void guardar() {

		if (this.presupuestoSelected.getPresupuestoid() == null) {
			
			List<Presupuesto>  lpresu = this.reg.getAllObjectsByCondicionOrder(Presupuesto.class.getName(), "anho = "+this.presupuestoSelected.getAnho(), null);
			
			if (lpresu.size() > 0) {
				
				this.mensajeError("Ya existe presupuesto del año "+this.presupuestoSelected.getAnho()+".");
				return;
				
			}
			
		}
		
		this.presupuestoSelected = this.save(presupuestoSelected);
		
		Notification.show("Presupuesto Guardado.");
		

		//this.presupuestoSelected = null;

		// this.cargarPresupuestos();

	}

	// Seccion Finder

	private FinderModel rubroFinder;
	private FinderModel presupuestoFinder;
	private FinderModel monedaFinder;
	private PresupuestoDetalle presupuestoDetalleSelected;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlRubro = this.um.getSql("rubro/buscarRubro.sql");
		rubroFinder = new FinderModel("Rubro", sqlRubro);
		
		String sqlPresupuesto = this.um.getSql("presupuesto/buscarPresupuesto.sql");
		presupuestoFinder = new FinderModel("Presupuesto", sqlPresupuesto);
		
		String sqlMoneda = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_MONEDA);
		monedaFinder = new FinderModel("Moneda", sqlMoneda);
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.rubroFinder.getNameFinder()) == 0) {

			this.rubroFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.rubroFinder, "listFinder");

			return;

		}
		

		if (finder.compareTo(this.presupuestoFinder.getNameFinder()) == 0) {

			this.presupuestoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.presupuestoFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.monedaFinder.getNameFinder()) == 0) {

			this.monedaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.monedaFinder, "listFinder");

			return;

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.rubroFinder.getNameFinder()) == 0) {

			this.rubroFinder.setListFinder(this.filtrarListaObject(filter, this.rubroFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.rubroFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.presupuestoFinder.getNameFinder()) == 0) {

			this.presupuestoFinder.setListFinder(this.filtrarListaObject(filter, this.presupuestoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.presupuestoFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.monedaFinder.getNameFinder()) == 0) {

			this.monedaFinder.setListFinder(this.filtrarListaObject(filter, this.monedaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.monedaFinder, "listFinder");

			return;

		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.rubroFinder.getNameFinder()) == 0) {
			
			this.presupuestoDetalleSelected.setRubro(this.reg.getObjectById(Rubro.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}
		
		if (finder.compareTo(this.presupuestoFinder.getNameFinder()) == 0) {
			
			this.presupuestoSelected = this.reg.getObjectById(Presupuesto.class.getName(), id);
			this.editar = true;
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}
		
		if (finder.compareTo(this.monedaFinder.getNameFinder()) == 0) {
			
			this.presupuestoDetalleSelected.setMonedaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}

	}
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalPresupuestoDetalle() {
		
		this.presupuestoDetalleSelected = new PresupuestoDetalle();	
		this.presupuestoDetalleSelected.setPresupuesto(this.presupuestoSelected);
		
		this.inicializarFinders();
		
		modal = (Window) Executions.createComponents("/instituto/zul/compra/presupuestoDetalleModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("*")
	public void agregarDetalle() {
		
		boolean existe = false;
		
		for (PresupuestoDetalle x : this.presupuestoSelected.getDetalles()) {
			
			if (x.getRubro().getRubroid().longValue() == this.presupuestoDetalleSelected.getRubro().getRubroid().longValue()) {
				
				existe = true;
				break;
				
			}
			
		}
		
		if (!existe) {
			
			this.presupuestoSelected.getDetalles().add(this.presupuestoDetalleSelected);	
			modal.detach();
			
		}else {
		
			this.mensajeError("ya existe el rubro");
			
		}
			
		
	}
	
	@Command
	@NotifyChange("*")
	public void borrarDetalle(@BindingParam("dato") PresupuestoDetalle dato) {
		
		if (!this.opCrearPresupuesto || !this.opEditarPresupuesto) {
			
			return;
			
		}
		
		this.presupuestoSelected.getDetalles().remove(dato);
		
	}

	@Command
	@NotifyChange("lPresupuestos")
	public void filtrarPresupuesto() {

		this.lPresupuestos = this.filtrarListaObject(this.filtroColumns, this.lPresupuestosOri);

	}

	public List<Object[]> getlPresupuestos() {
		return lPresupuestos;
	}

	public void setlPresupuestos(List<Object[]> lPresupuestos) {
		this.lPresupuestos = lPresupuestos;
	}

	public Presupuesto getPresupuestoSelected() {
		return presupuestoSelected;
	}

	public void setPresupuestoSelected(Presupuesto presupuestoSelected) {
		this.presupuestoSelected = presupuestoSelected;
	}

	public boolean isOpCrearPresupuesto() {
		return opCrearPresupuesto;
	}

	public void setOpCrearPresupuesto(boolean opCrearPresupuesto) {
		this.opCrearPresupuesto = opCrearPresupuesto;
	}

	public boolean isOpEditarPresupuesto() {
		return opEditarPresupuesto;
	}

	public void setOpEditarPresupuesto(boolean opEditarPresupuesto) {
		this.opEditarPresupuesto = opEditarPresupuesto;
	}

	public boolean isOpBorrarPresupuesto() {
		return opBorrarPresupuesto;
	}

	public void setOpBorrarPresupuesto(boolean opBorrarPresupuesto) {
		this.opBorrarPresupuesto = opBorrarPresupuesto;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public FinderModel getRubroFinder() {
		return rubroFinder;
	}

	public void setRubroFinder(FinderModel rubroFinder) {
		this.rubroFinder = rubroFinder;
	}

	public PresupuestoDetalle getPresupuestoDetalleSelected() {
		return presupuestoDetalleSelected;
	}

	public void setPresupuestoDetalleSelected(PresupuestoDetalle presupuestoDetalleSelected) {
		this.presupuestoDetalleSelected = presupuestoDetalleSelected;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public FinderModel getPresupuestoFinder() {
		return presupuestoFinder;
	}

	public void setPresupuestoFinder(FinderModel presupuestoFinder) {
		this.presupuestoFinder = presupuestoFinder;
	}

	public FinderModel getMonedaFinder() {
		return monedaFinder;
	}

	public void setMonedaFinder(FinderModel monedaFinder) {
		this.monedaFinder = monedaFinder;
	}

	

	// fin seccion

}
