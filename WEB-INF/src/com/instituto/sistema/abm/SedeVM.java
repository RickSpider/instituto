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
import com.doxacore.modelo.Ciudad;
import com.instituto.ParamsLocal;
import com.instituto.modelo.Sede;

public class SedeVM extends TemplateViewModel {

	private List<Sede> lSedes;
	private List<Sede> lSedesOri;
	private Sede sedeSelected;

	private boolean opCrearSede;
	private boolean opEditarSede;
	private boolean opBorrarSede;

	@Init(superclass = true)
	public void initSedeVM() {

		cargarSedes();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeSedeVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearSede = this.operacionHabilitada(ParamsLocal.OP_CREAR_SEDE);
		this.opEditarSede = this.operacionHabilitada(ParamsLocal.OP_EDITAR_SEDE);
		this.opBorrarSede = this.operacionHabilitada(ParamsLocal.OP_BORRAR_SEDE);

	}

	private void cargarSedes() {

		this.lSedes = this.reg.getAllObjectsByCondicionOrder(Sede.class.getName(), null, "Sedeid asc");
		this.lSedesOri = this.lSedes;
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
	@NotifyChange("lSedes")
	public void filtrarSede() {

		this.lSedes = this.filtrarLT(this.filtroColumns, this.lSedesOri);

	}

	// fin seccion
	
	//seccion modal
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalSedeAgregar() {

		if(!this.opCrearSede)
			return;

		this.editar = false;
		modalSede(-1);

	}

	@Command
	public void modalSede(@BindingParam("sedeid") long sedeid) {

		if (sedeid != -1) {

			if(!this.opEditarSede)
				return;
			
			this.sedeSelected = this.reg.getObjectById(Sede.class.getName(), sedeid);
			this.buscarCiudad = this.sedeSelected.getCiudad().getCiudad();
			this.editar = true;

		} else {
			
			sedeSelected = new Sede();
			this.buscarCiudad = "";

		}

		modal = (Window) Executions.createComponents("/instituto/zul/abm/sedeModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	@Command
	@NotifyChange("lSedes")
	public void guardar() {

		this.reg.saveObject(sedeSelected, getCurrentUser().getAccount());

		this.sedeSelected = null;

		this.cargarSedes();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("El Sede fue Actualizado.");
			this.editar = false;
		}else {
			
			Notification.show("El Sede fue agregado.");
		}
		
		

	}

	
	//fin modal
	
	@Command
	public void borrarSedeConfirmacion(@BindingParam("sede") final Sede sede) {
		
		if (!this.opBorrarSede)
			return;
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					borrarSede(sede);
					
				}
				
			}

		};
		
		this.mensajeEliminar("El rol sera eliminado. \n Continuar?", event);
	}
	
	private void borrarSede (Sede sede) {
		
		this.reg.deleteObject(sede);
		
		this.cargarSedes();
		
		BindUtils.postNotifyChange(null,null,this,"lSedes");
		
	}
	
	// buscador de Ciudad
	
	

		private List<Object[]> lCiudadesbuscarOri = null;
		private List<Object[]> lCiudadesBuscar = null;
		private Ciudad buscarSelectedCiudad = null;
		private String buscarCiudad = "";

		@Command
		@NotifyChange("lCiudadesBuscar")
		public void filtrarCiudadBuscar() {

			this.lCiudadesBuscar = this.filtrarListaObject(buscarCiudad, this.lCiudadesbuscarOri);

		}
		
		@Command
		@NotifyChange("lCiudadesBuscar")
		public void generarListaBuscarCiudad() {
			
			System.out.println("entre en generar lista ciudad");
			
			this.lCiudadesBuscar = this.reg.sqlNativo(
					"select c.ciudadid,"
					+ " c.ciudad,"
					+ " d.departamento,"
					+ " p.pais \n" + 
					"from ciudades c\n" + 
					"left join departamentos d on d.departamentoid = c.departamentoid\n" + 
					"left join paises p on p.paisid = d.paisid\n" + 
					"order by c.ciudadid asc;");
			
			this.lCiudadesbuscarOri = this.lCiudadesBuscar;
		}
		
		@Command
		@NotifyChange("buscarCiudad")
		public void onSelectCiudad(@BindingParam("id") long id) {
			
			this.buscarSelectedCiudad = this.reg.getObjectById(Ciudad.class.getName(), id);	
			this.buscarCiudad = this.buscarSelectedCiudad.getCiudad();
			this.sedeSelected.setCiudad(buscarSelectedCiudad);

		}
		


		
		//fin buscador de ciudad 
	

	public List<Sede> getlSedes() {
		return lSedes;
	}

	public void setlSedes(List<Sede> lSedes) {
		this.lSedes = lSedes;
	}

	public Sede getSedeSelected() {
		return sedeSelected;
	}

	public void setSedeSelected(Sede sedeSelected) {
		this.sedeSelected = sedeSelected;
	}

	public boolean isOpCrearSede() {
		return opCrearSede;
	}

	public void setOpCrearSede(boolean opCrearSede) {
		this.opCrearSede = opCrearSede;
	}

	public boolean isOpEditarSede() {
		return opEditarSede;
	}

	public void setOpEditarSede(boolean opEditarSede) {
		this.opEditarSede = opEditarSede;
	}

	public boolean isOpBorrarSede() {
		return opBorrarSede;
	}

	public void setOpBorrarSede(boolean opBorrarSede) {
		this.opBorrarSede = opBorrarSede;
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

	public List<Object[]> getlCiudadesBuscar() {
		return lCiudadesBuscar;
	}

	public void setlCiudadesBuscar(List<Object[]> lCiudadesBuscar) {
		this.lCiudadesBuscar = lCiudadesBuscar;
	}

	public String getBuscarCiudad() {
		return buscarCiudad;
	}

	public void setBuscarCiudad(String buscarCiudad) {
		this.buscarCiudad = buscarCiudad;
	}

}
