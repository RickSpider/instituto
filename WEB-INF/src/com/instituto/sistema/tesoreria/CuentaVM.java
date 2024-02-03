package com.instituto.sistema.tesoreria;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.components.finder.FinderModel;
import com.instituto.modelo.Cuenta;
import com.instituto.modelo.Entidad;
import com.instituto.modelo.Institucion;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CuentaVM extends TemplateViewModelLocal {

	private List<Object[]> lCuentas;
	private List<Object[]> lCuentasOri;

	private Cuenta cuentaSelected;

	private boolean opCrearCuenta;
	private boolean opEditarCuenta;
	private boolean opBorrarCuenta;

	@Init(superclass = true)
	public void initCuentaVM() {

		this.cargarCuentas();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterCuentaVM() {

	}

	@Override
	protected void inicializarOperaciones() {
	
		this.opCrearCuenta = this.operacionHabilitada(ParamsLocal.OP_CREAR_CUENTA);
		this.opEditarCuenta = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CUENTA);
		this.opBorrarCuenta = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CUENTA);

	}

	private void cargarCuentas() {

		String sqlCuentas = this.um.getSql("cuenta/listaCuentas.sql").replace("?1", this.getCurrentSede().getSedeid()+"");
		
		this.lCuentas = this.reg.sqlNativo(sqlCuentas);
		this.lCuentasOri = this.lCuentas;
		
	}

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lCuentas")
	public void filtrarCuenta() {

		this.lCuentas = filtrarListaObject(this.filtroColumns, this.lCuentasOri);

	}

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalCuentaAgregar() {

		if (!this.opCrearCuenta)
			return;

		this.editar = false;
		modalCuenta(-1);

	}

	@Command
	public void modalCuenta(@BindingParam("cuentaid") long cuentaid) {

		this.inicializarFinders();

		if (cuentaid != -1) {

			if (!this.opEditarCuenta)
				return;

			this.cuentaSelected = this.reg.getObjectById(Cuenta.class.getName(), cuentaid);
			this.editar = true;

		} else {

			cuentaSelected = new Cuenta();
			this.cuentaSelected.setSede(this.getCurrentSede());

		}

		modal = (Window) Executions.createComponents("/instituto/zul/tesoreria/cuentaModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	
	
	}
	
	@Command
	public void borrarCuentaConfirmar(@BindingParam("id") long id) {

		if (!this.opBorrarCuenta)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					Cuenta c = reg.getObjectById(Cuenta.class.getCanonicalName(), id);
					
					borrarInstitucion(c);

				}

			}

		};

		this.mensajeEliminar("La Cuenta sera eliminada. \n Continuar?", event);
	}

	private void borrarInstitucion(Cuenta cuenta) {

		
		this.reg.deleteObject(cuenta);

		this.cargarCuentas();

		BindUtils.postNotifyChange(null, null, this, "lCuentas");

	}

	private FinderModel entidadFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlEntidad = this.um.getSql("entidades.sql");
		entidadFinder = new FinderModel("Contribuyente", sqlEntidad);

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.entidadFinder.getNameFinder()) == 0) {

			this.entidadFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.entidadFinder, "listFinder");

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.entidadFinder.getNameFinder()) == 0) {

			this.entidadFinder.setListFinder(this.filtrarListaObject(filter, this.entidadFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.entidadFinder, "listFinder");

		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.entidadFinder.getNameFinder()) == 0) {

			this.cuentaSelected.setEntidad(this.reg.getObjectById(Entidad.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "contribuyenteFinder");
		}

	}
	
	@Command
	@NotifyChange("*")
	public void guardar() {
		
		this.save(this.cuentaSelected);
		this.modal.detach();
		
		this.cargarCuentas();
		
	}

	public List<Object[]> getlCuentas() {
		return lCuentas;
	}

	public void setlCuentas(List<Object[]> lCuentas) {
		this.lCuentas = lCuentas;
	}

	public Cuenta getCuentaSelected() {
		return cuentaSelected;
	}

	public void setCuentaSelected(Cuenta cuentaSelected) {
		this.cuentaSelected = cuentaSelected;
	}

	public boolean isOpCrearCuenta() {
		return opCrearCuenta;
	}

	public void setOpCrearCuenta(boolean opCrearCuenta) {
		this.opCrearCuenta = opCrearCuenta;
	}

	public boolean isOpEditarCuenta() {
		return opEditarCuenta;
	}

	public void setOpEditarCuenta(boolean opEditarCuenta) {
		this.opEditarCuenta = opEditarCuenta;
	}

	public boolean isOpBorrarCuenta() {
		return opBorrarCuenta;
	}

	public void setOpBorrarCuenta(boolean opBorrarCuenta) {
		this.opBorrarCuenta = opBorrarCuenta;
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

	public FinderModel getEntidadFinder() {
		return entidadFinder;
	}

	public void setEntidadFinder(FinderModel entidadFinder) {
		this.entidadFinder = entidadFinder;
	}

}
