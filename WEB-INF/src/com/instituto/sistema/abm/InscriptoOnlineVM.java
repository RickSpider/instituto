package com.instituto.sistema.abm;

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
import com.instituto.modelo.InscriptoOnline;
import com.instituto.modelo.Servicio;
import com.instituto.util.ParamsLocal;

public class InscriptoOnlineVM extends TemplateViewModel {

	private List<Object[]> lInscriptosOnline;
	private List<Object[]> lInscriptosOnlineOri;
	private InscriptoOnline inscriptoOnlineSelected;
	
	private boolean opCrearInscriptoOnline;
	private boolean opEditarInscriptoOnline;
	private boolean opBorrarInscriptoOnline;

	

	@Init(superclass = true)
	public void initInscriptoOnlineVM() {

		cargarInscriptosOnline();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeInscriptoOnlineVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearInscriptoOnline = this.operacionHabilitada(ParamsLocal.OP_CREAR_INSCRIPTOONLINE);
		this.opEditarInscriptoOnline = this.operacionHabilitada(ParamsLocal.OP_EDITAR_INSCRIPTOONLINE);
		this.opBorrarInscriptoOnline = this.operacionHabilitada(ParamsLocal.OP_BORRAR_INSCRIPTOONLINE);

	}

	private void cargarInscriptosOnline() {

		String sqlInscriptoOnline = this.um.getSql("inscriptoOnline/listaInscriptoOnline.sql");
		
		this.lInscriptosOnline = this.reg.sqlNativo(sqlInscriptoOnline);
		this.lInscriptosOnlineOri = this.lInscriptosOnline;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[9]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lInscriptosOnline")
	public void filtrarInscriptoOnline() {

		this.lInscriptosOnline = this.filtrarListaObject(this.filtroColumns, this.lInscriptosOnlineOri);

	}
	
	private Window modal;

	
	@Command
	public void modalInscriptoOnline(@BindingParam("inscriptoOnlineid") long inscriptoOnlineid) {
		
		this.inscriptoOnlineSelected = this.reg.getObjectById(InscriptoOnline.class.getName(), inscriptoOnlineid);

		modal = (Window) Executions.createComponents("/instituto/zul/abm/inscriptoOnlineModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lInscriptosOnline")
	public void guardar() {
		
		this.save(inscriptoOnlineSelected);

		this.inscriptoOnlineSelected = null;

		this.cargarInscriptosOnline();

		this.modal.detach();
		
		Notification.show("La Inscripcion fue Actualizada.");
	
	}
	
	@Command
	public void borrarInscriptoOnlineConfirmacion(@BindingParam("inscriptoOnlineid") final long inscriptoonlineid) {

		if (!this.opBorrarInscriptoOnline)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarInscriptoOnline(reg.getObjectById(InscriptoOnline.class.getName(), inscriptoonlineid));

				}

			}

		};

		this.mensajeEliminar("Se eliminara la inscripcon. \n Continuar?", event);
	}

	private void borrarInscriptoOnline(InscriptoOnline inscriptoOnline) {

		this.reg.deleteObject(inscriptoOnline);

		this.cargarInscriptosOnline();

		BindUtils.postNotifyChange(null, null, this, "lInscriptosOnline");

	}
	
	

	

	public List<Object[]> getlInscriptosOnline() {
		return lInscriptosOnline;
	}

	public void setlInscriptosOnline(List<Object[]> lInscriptosOnline) {
		this.lInscriptosOnline = lInscriptosOnline;
	}

	public InscriptoOnline getInscriptoOnlineSelected() {
		return inscriptoOnlineSelected;
	}

	public void setInscriptoOnlineSelected(InscriptoOnline inscriptoOnlineSelected) {
		this.inscriptoOnlineSelected = inscriptoOnlineSelected;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public boolean isOpCrearInscriptoOnline() {
		return opCrearInscriptoOnline;
	}

	public void setOpCrearInscriptoOnline(boolean opCrearInscriptoOnline) {
		this.opCrearInscriptoOnline = opCrearInscriptoOnline;
	}

	public boolean isOpEditarInscriptoOnline() {
		return opEditarInscriptoOnline;
	}

	public void setOpEditarInscriptoOnline(boolean opEditarInscriptoOnline) {
		this.opEditarInscriptoOnline = opEditarInscriptoOnline;
	}

	public boolean isOpBorrarInscriptoOnline() {
		return opBorrarInscriptoOnline;
	}

	public void setOpBorrarInscriptoOnline(boolean opBorrarInscriptoOnline) {
		this.opBorrarInscriptoOnline = opBorrarInscriptoOnline;
	}
	
	
}
