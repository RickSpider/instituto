package com.instituto.sistema.compra;

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

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.instituto.modelo.CuentaRubro;
import com.instituto.modelo.Rubro;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CuentaRubroVM extends TemplateViewModelLocal {

	private List<Object[]> lCuentasCuentaRubros;
	private List<Object[]> lCuentasCuentaRubrosOri;
	private CuentaRubro cuentaRubroSelected;

	private boolean opCrearCuentaRubro;
	private boolean opEditarCuentaRubro;
	private boolean opBorrarCuentaRubro;

	@Init(superclass = true)
	public void initCuentaRubroVM() {

		cargarCuentasCuentaRubros();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeCuentaRubroVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearCuentaRubro = this.operacionHabilitada(ParamsLocal.OP_CREAR_CUENTARUBRO);
		this.opEditarCuentaRubro = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CUENTARUBRO);
		this.opBorrarCuentaRubro = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CUENTARUBRO);

	}

	private void cargarCuentasCuentaRubros() {

		String sqlCuentaRubro = this.um.getSql("cuentaRubro/listaCuentaRubro.sql");

		this.lCuentasCuentaRubros = this.reg.sqlNativo(sqlCuentaRubro);
		this.lCuentasCuentaRubrosOri = this.lCuentasCuentaRubros;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[4]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lCuentasCuentaRubros")
	public void filtrarCuentaRubro() {

		this.lCuentasCuentaRubros = this.filtrarListaObject(this.filtroColumns, this.lCuentasCuentaRubrosOri);

	}

	// fin seccion

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalCuentaRubroAgregar() {

		if (!this.opCrearCuentaRubro)
			return;

		this.editar = false;
		modalCuentaRubro(-1);

	}

	@Command
	public void modalCuentaRubro(@BindingParam("cuentaRubroid") long cuentaRubroid) {

		 this.inicializarFinders();

		if (cuentaRubroid != -1) {

			if (!this.opEditarCuentaRubro)
				return;

			this.cuentaRubroSelected = this.reg.getObjectById(CuentaRubro.class.getName(), cuentaRubroid);

		} else {

			cuentaRubroSelected = new CuentaRubro();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/compra/cuentaRubroModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		if (this.cuentaRubroSelected.getCuentaRubro() == null
				|| this.cuentaRubroSelected.getCuentaRubro().length() <= 0) {

			return false;

		}

		return true;
	}

	@Command
	@NotifyChange("lCuentasCuentaRubros")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}

		this.save(cuentaRubroSelected);

		this.cuentaRubroSelected = null;

		this.cargarCuentasCuentaRubros();

		this.modal.detach();

		if (editar) {

			Notification.show("La CuentaRubro fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("La CuentaRubro fue agregada.");
		}

	}

	// fin modal

	@Command
	public void borrarCuentaRubroConfirmacion(@BindingParam("cuentaRubroid") final long cuentaRubroid) {

		if (!this.opBorrarCuentaRubro)
			return;

		CuentaRubro s = this.reg.getObjectById(CuentaRubro.class.getName(), cuentaRubroid);

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarCuentaRubro(s);

				}

			}

		};

		this.mensajeEliminar("El CuentaRubro sera eliminada. \n Continuar?", event);
	}

	private void borrarCuentaRubro(CuentaRubro cuentaRubro) {

		this.reg.deleteObject(cuentaRubro);

		this.cargarCuentasCuentaRubros();

		BindUtils.postNotifyChange(null, null, this, "lCuentasCuentaRubros");

	}

	// Seccion Finder

	private FinderModel rubroFinder;
	
	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlConcepto = this.um.getSql("rubro/buscarRubro.sql");

		rubroFinder = new FinderModel("Rubro", sqlConcepto);
		

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.rubroFinder.getNameFinder()) == 0) {

			this.rubroFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.rubroFinder, "listFinder");

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
	

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.rubroFinder.getNameFinder()) == 0) {

			this.cuentaRubroSelected.setRubro(this.reg.getObjectById(Rubro.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}

	}

	public List<Object[]> getlCuentasCuentaRubros() {
		return lCuentasCuentaRubros;
	}

	public void setlCuentasCuentaRubros(List<Object[]> lCuentasCuentaRubros) {
		this.lCuentasCuentaRubros = lCuentasCuentaRubros;
	}

	public CuentaRubro getCuentaRubroSelected() {
		return cuentaRubroSelected;
	}

	public void setCuentaRubroSelected(CuentaRubro cuentaRubroSelected) {
		this.cuentaRubroSelected = cuentaRubroSelected;
	}

	public boolean isOpCrearCuentaRubro() {
		return opCrearCuentaRubro;
	}

	public void setOpCrearCuentaRubro(boolean opCrearCuentaRubro) {
		this.opCrearCuentaRubro = opCrearCuentaRubro;
	}

	public boolean isOpEditarCuentaRubro() {
		return opEditarCuentaRubro;
	}

	public void setOpEditarCuentaRubro(boolean opEditarCuentaRubro) {
		this.opEditarCuentaRubro = opEditarCuentaRubro;
	}

	public boolean isOpBorrarCuentaRubro() {
		return opBorrarCuentaRubro;
	}

	public void setOpBorrarCuentaRubro(boolean opBorrarCuentaRubro) {
		this.opBorrarCuentaRubro = opBorrarCuentaRubro;
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

	public FinderModel getRubroFinder() {
		return rubroFinder;
	}

	public void setRubroFinder(FinderModel rubroFinder) {
		this.rubroFinder = rubroFinder;
	}

	
}
