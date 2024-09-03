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

import com.instituto.modelo.Rubro;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class RubroVM extends TemplateViewModelLocal {
	
	private List<Object[]> lRubros;
	private List<Object[]> lRubrosOri;
	private Rubro rubroSelected;

	private boolean opCrearRubro;
	private boolean opEditarRubro;
	private boolean opBorrarRubro;

	@Init(superclass = true)
	public void initRubroVM() {

		cargarRubros();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeRubroVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearRubro = this.operacionHabilitada(ParamsLocal.OP_CREAR_RUBRO);
		this.opEditarRubro = this.operacionHabilitada(ParamsLocal.OP_EDITAR_RUBRO);
		this.opBorrarRubro = this.operacionHabilitada(ParamsLocal.OP_BORRAR_RUBRO);

	}

	private void cargarRubros() {

		String sqlRubro = this.um.getSql("rubro/listaRubro.sql");
		
		this.lRubros = this.reg.sqlNativo(sqlRubro);
		this.lRubrosOri = this.lRubros;
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
	@NotifyChange("lRubros")
	public void filtrarRubro() {

		this.lRubros = this.filtrarListaObject(this.filtroColumns, this.lRubrosOri);

	}

	// fin seccion

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalRubroAgregar() {

		if (!this.opCrearRubro)
			return;

		this.editar = false;
		modalRubro(-1);

	}

	@Command
	public void modalRubro(@BindingParam("rubroid") long rubroid) {
		
		//this.inicializarFinders();

		if (rubroid != -1) {

			if (!this.opEditarRubro)
				return;
			
			this.rubroSelected = this.reg.getObjectById(Rubro.class.getName(), rubroid);

		} else {

			rubroSelected = new Rubro();

		}

		modal = (Window) Executions.createComponents("/instituto/zul/compra/rubroModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private boolean verificarCampos() {

		if (this.rubroSelected.getRubro() == null || this.rubroSelected.getRubro().length() <= 0) {

			return false;

		}

		return true;
	}

	@Command
	@NotifyChange("lRubros")
	public void guardar() {

		if (!verificarCampos()) {
			return;
		}

		this.save(rubroSelected);

		this.rubroSelected = null;

		this.cargarRubros();

		this.modal.detach();

		if (editar) {

			Notification.show("La Rubro fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("La Rubro fue agregada.");
		}

	}

	// fin modal

	@Command
	public void borrarRubroConfirmacion(@BindingParam("rubroid") final long rubroid) {

		if (!this.opBorrarRubro)
			return;
		
		Rubro s = this.reg.getObjectById(Rubro.class.getName(), rubroid);

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarRubro(s);

				}

			}

		};

		this.mensajeEliminar("El Rubro sera eliminada. \n Continuar?", event);
	}

	private void borrarRubro(Rubro rubro) {

		this.reg.deleteObject(rubro);

		this.cargarRubros();

		BindUtils.postNotifyChange(null, null, this, "lRubros");

	}

	public List<Object[]> getlRubros() {
		return lRubros;
	}

	public void setlRubros(List<Object[]> lRubros) {
		this.lRubros = lRubros;
	}

	public Rubro getRubroSelected() {
		return rubroSelected;
	}

	public void setRubroSelected(Rubro rubroSelected) {
		this.rubroSelected = rubroSelected;
	}

	public boolean isOpCrearRubro() {
		return opCrearRubro;
	}

	public void setOpCrearRubro(boolean opCrearRubro) {
		this.opCrearRubro = opCrearRubro;
	}

	public boolean isOpEditarRubro() {
		return opEditarRubro;
	}

	public void setOpEditarRubro(boolean opEditarRubro) {
		this.opEditarRubro = opEditarRubro;
	}

	public boolean isOpBorrarRubro() {
		return opBorrarRubro;
	}

	public void setOpBorrarRubro(boolean opBorrarRubro) {
		this.opBorrarRubro = opBorrarRubro;
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
	
	

}
