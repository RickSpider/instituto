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
import com.doxacore.modelo.Tipo;
import com.instituto.modelo.Cuenta;
import com.instituto.modelo.Transaccion;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class TransaccionVM extends TemplateViewModelLocal {

	private List<Object[]> lTransacciones;
	private List<Object[]> lTransaccionesOri;

	private Transaccion transaccionSelected;

	private boolean opCrearTransaccion;
	private boolean opEditarTransaccion;
	private boolean opBorrarTransaccion;

	@Init(superclass = true)
	public void initTransaccionVM() {

		this.cargarTransacciones();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterTransaccionVM() {

	}

	@Override
	protected void inicializarOperaciones() {
	
		this.opCrearTransaccion = this.operacionHabilitada(ParamsLocal.OP_CREAR_TRANSACCION);
		this.opEditarTransaccion = this.operacionHabilitada(ParamsLocal.OP_EDITAR_TRANSACCION);
		this.opBorrarTransaccion = this.operacionHabilitada(ParamsLocal.OP_BORRAR_TRANSACCION);

	}

	private void cargarTransacciones() {

		String sqlTransacciones = this.um.getSql("transaccion/listaTransacciones.sql");
		
		this.lTransacciones = this.reg.sqlNativo(sqlTransacciones);
		this.lTransaccionesOri = this.lTransacciones;
		
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
	@NotifyChange("lTransacciones")
	public void filtrarTransaccion() {

		this.lTransacciones = filtrarListaObject(this.filtroColumns, this.lTransaccionesOri);

	}

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalTransaccionAgregar() {

		if (!this.opCrearTransaccion)
			return;

		this.editar = false;
		modalTransaccion(-1);

	}

	@Command
	public void modalTransaccion(@BindingParam("transaccionid") long transaccionid) {
		
		this.inicializarFinders();

		if (transaccionid != -1) {

			if (!this.opEditarTransaccion)
				return;

			this.transaccionSelected = this.reg.getObjectById(Transaccion.class.getName(), transaccionid);
			this.editar = true;

		} else {

			transaccionSelected = new Transaccion();
			
		}

		modal = (Window) Executions.createComponents("/instituto/zul/tesoreria/transaccionModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	
	
	}
	
	@Command
	public void borrarTransaccionConfirmar(@BindingParam("id") long id) {

		if (!this.opBorrarTransaccion)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					Transaccion c = reg.getObjectById(Transaccion.class.getCanonicalName(), id);
					
					borrarInstitucion(c);

				}

			}

		};

		this.mensajeEliminar("La Transaccion sera eliminada. \n Continuar?", event);
	}

	private void borrarInstitucion(Transaccion transaccion) {

		
		this.reg.deleteObject(transaccion);

		this.cargarTransacciones();

		BindUtils.postNotifyChange(null, null, this, "lTransacciones");

	}

	private FinderModel cuentaFinder;
	private FinderModel transaccionTipoFinder;

	
	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlCuenta = this.um.getSql("cuenta/listaCuentas.sql").replace("?1", this.getCurrentSede().getSedeid()+"");
		cuentaFinder = new FinderModel("cuenta", sqlCuenta);
		

		String sqlTipoTransaccion = "select tipoid as Id, t.tipo as Tipo from tipos t\n" + 
				"left join tipotipos tt on tt.tipotipoid = t.tipotipoid\n" + 
				"where tt.sigla like 'TRANSACCION'\n" + 
				"order by tipoid asc;";
		
		transaccionTipoFinder = new FinderModel("transaccionTipo", sqlTipoTransaccion);

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

		}
		
		if (finder.compareTo(this.transaccionTipoFinder.getNameFinder()) == 0) {

			this.transaccionTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.transaccionTipoFinder, "listFinder");

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.setListFinder(this.filtrarListaObject(filter, this.cuentaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

		}
		

		if (finder.compareTo(this.transaccionTipoFinder.getNameFinder()) == 0) {

			this.transaccionTipoFinder.setListFinder(this.filtrarListaObject(filter, this.transaccionTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.transaccionTipoFinder, "listFinder");

		}


	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.transaccionSelected.setCuenta(this.reg.getObjectById(Cuenta.class.getName(), id));
			
		}
		
		if (finder.compareTo(this.transaccionTipoFinder.getNameFinder()) == 0) {

			this.transaccionSelected.setTransaccionTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
		}

	}
	
	@Command
	@NotifyChange("*")
	public void guardar() {
		
		this.save(this.transaccionSelected);
		this.modal.detach();
		
		this.cargarTransacciones();
		
	}

	public List<Object[]> getlTransacciones() {
		return lTransacciones;
	}

	public void setlTransacciones(List<Object[]> lTransacciones) {
		this.lTransacciones = lTransacciones;
	}

	public Transaccion getTransaccionSelected() {
		return transaccionSelected;
	}

	public void setTransaccionSelected(Transaccion transaccionSelected) {
		this.transaccionSelected = transaccionSelected;
	}

	public boolean isOpCrearTransaccion() {
		return opCrearTransaccion;
	}

	public void setOpCrearTransaccion(boolean opCrearTransaccion) {
		this.opCrearTransaccion = opCrearTransaccion;
	}

	public boolean isOpEditarTransaccion() {
		return opEditarTransaccion;
	}

	public void setOpEditarTransaccion(boolean opEditarTransaccion) {
		this.opEditarTransaccion = opEditarTransaccion;
	}

	public boolean isOpBorrarTransaccion() {
		return opBorrarTransaccion;
	}

	public void setOpBorrarTransaccion(boolean opBorrarTransaccion) {
		this.opBorrarTransaccion = opBorrarTransaccion;
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

	public FinderModel getCuentaFinder() {
		return cuentaFinder;
	}

	public void setCuentaFinder(FinderModel cuentaFinder) {
		this.cuentaFinder = cuentaFinder;
	}

	public FinderModel getTransaccionTipoFinder() {
		return transaccionTipoFinder;
	}

	public void setTransaccionTipoFinder(FinderModel transaccionTipoFinder) {
		this.transaccionTipoFinder = transaccionTipoFinder;
	}
	
}
