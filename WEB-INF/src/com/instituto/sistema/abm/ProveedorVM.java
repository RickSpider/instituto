package com.instituto.sistema.abm;

import java.util.List;
import java.util.Objects;

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
import com.doxacore.modelo.Rol;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;
import com.doxacore.modelo.UsuarioRol;
import com.doxacore.util.UtilStaticMetodos;
import com.instituto.modelo.Proveedor;
import com.instituto.modelo.UsuarioSede;
import com.instituto.modelo.Persona;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class ProveedorVM extends TemplateViewModelLocal {

	private List<Object[]> lProveedores;
	private List<Object[]> lProveedoresOri;
	private Proveedor proveedorSelected;

	private boolean opCrearProveedor;
	private boolean opEditarProveedor;
	private boolean opBorrarProveedor;


	@Init(superclass = true)
	public void initProveedorVM() {

		cargarProveedores();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeProveedorVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearProveedor = this.operacionHabilitada(ParamsLocal.OP_CREAR_PROVEEDOR);
		this.opEditarProveedor = this.operacionHabilitada(ParamsLocal.OP_EDITAR_PROVEEDOR);
		this.opBorrarProveedor = this.operacionHabilitada(ParamsLocal.OP_BORRAR_PROVEEDOR);
	
	}

	private void cargarProveedores() {
		
		String sql = this.um.getSql("proveedor/listaProveedores.sql").replace("?1", this.getCurrentSede().getSedeid()+"");
		
		this.lProveedores = this.reg.sqlNativo(sql);

		/*this.lProveedores = this.reg.getAllObjectsByCondicionOrder(Proveedor.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid(), "Proveedorid asc");*/
		this.lProveedoresOri = this.lProveedores;
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[5]; 

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lProveedores")
	public void filtrarProveedor() {

	//	this.lProveedores = this.filtrarLT(this.filtroColumns, this.lProveedoresOri);
		this.lProveedores = this.filtrarListaObject(this.filtroColumns, this.lProveedoresOri);

	}

	// fin seccion

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalProveedorAgregar() {

		if (!this.opCrearProveedor)
			return;

		this.editar = false;
		modalProveedor(-1);

	}

	@Command
	public void modalProveedor(@BindingParam("proveedorid") long proveedorid) {
		
		this.inicializarFinders();

		if (proveedorid != -1) {

			if (!this.opEditarProveedor)
				return;

			this.proveedorSelected = this.reg.getObjectById(Proveedor.class.getName(), proveedorid);
			
			this.editar = true;
		
			if (this.proveedorSelected.getProveedorTipo().getSigla().compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) == 0
					&& Objects.isNull(this.proveedorSelected.getUsuario())) {
				
				Usuario u = new Usuario();
				u.setAccount(this.proveedorSelected.getPersona().getDocumentoNum());
				u.setFullName(this.proveedorSelected.getPersona().getNombreCompleto());
				u.setActivo(true);
				u.setPassword(UtilStaticMetodos.getSHA256(u.getAccount()));
				
				this.proveedorSelected.setUsuario(u);
				
			}
			

		} else {

			proveedorSelected = new Proveedor();
			proveedorSelected.setProveedorTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_PROVEEDOR_PROVEEDOR));
			proveedorSelected.setSede(getCurrentSede());
		
		}

		modal = (Window) Executions.createComponents("/instituto/zul/abm/proveedorModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	

	

	
	
	@Command
	@NotifyChange("lProveedores")
	public void guardar() {
		
		if (this.proveedorSelected.getProveedorTipo().getSigla().compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) == 0
				&& Objects.isNull(this.proveedorSelected.getUsuario().getUsuarioid())) {
			
			Usuario u = this.save(this.proveedorSelected.getUsuario());
			
			Rol rol = this.reg.getObjectByCondicion(Rol.class.getName(), "rol = 'Docente'");
			
			UsuarioRol ur = new UsuarioRol();
			ur.setRol(rol);
			ur.setUsuario(u);
			
			this.save(ur);
			
			UsuarioSede us = new UsuarioSede();
			
			us.setUsuario(u);
			us.setSede(this.getCurrentSede());
			us.setActivo(true);
			
			this.save(us);
			
			this.proveedorSelected.setUsuario(u);

			
		}else if (this.proveedorSelected.getProveedorTipo().getSigla().compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) == 0
				&&
				!Objects.isNull(this.proveedorSelected.getUsuario())) {
			
			this.save(this.proveedorSelected.getUsuario());
			
		}
		
		

		this.proveedorSelected.setSede(this.getCurrentSede());

		this.save(proveedorSelected);

		this.proveedorSelected = null;

		this.cargarProveedores();

		this.modal.detach();

		if (editar) {

			Notification.show("El Proveedor fue Actualizado.");
			this.editar = false;
		} else {

			Notification.show("El Proveedor fue agregado.");
		}

	}

	// fin modal

	@Command
	public void borrarProveedorConfirmacion(@BindingParam("docenteid") final long docenteid) {

		if (!this.opBorrarProveedor)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarProveedor(reg.getObjectById(Proveedor.class.getName(), docenteid));

				}

			}

		};

		this.mensajeEliminar("La Proveedor sera eliminada. \n Continuar?", event);
	}

	private void borrarProveedor(Proveedor docente) {

		this.reg.deleteObject(docente);

		this.cargarProveedores();

		BindUtils.postNotifyChange(null, null, this, "lProveedores");

	}
	
	
	private FinderModel personaFinder;
	private FinderModel proveedorTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlPersona = this.um.getSql("buscarPersonaNotProveedor.sql").replace("##SEDEID##", this.getCurrentSede().getSedeid()+"");

		personaFinder = new FinderModel("Persona", sqlPersona);
		
		String sqlProveedorTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_PROVEEDOR);
		
		proveedorTipoFinder = new FinderModel("Proveedor", sqlProveedorTipo);
		

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.proveedorTipoFinder.getNameFinder()) == 0) {

			this.proveedorTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.proveedorTipoFinder, "listFinder");

			return;

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.setListFinder(this.filtrarListaObject(filter, this.personaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.proveedorTipoFinder.getNameFinder()) == 0) {

			this.proveedorTipoFinder.setListFinder(this.filtrarListaObject(filter, this.proveedorTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.proveedorTipoFinder, "listFinder");
			
			
			return;

		}


	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.proveedorSelected.setPersona(this.reg.getObjectById(Persona.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}
		
		if (finder.compareTo(this.proveedorTipoFinder.getNameFinder()) == 0) {

			this.proveedorSelected.setProveedorTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");
			
			if (this.proveedorSelected.getProveedorTipo().getSigla().compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) == 0 ) {
				
				Usuario u = new Usuario();
				u.setAccount(this.proveedorSelected.getPersona().getDocumentoNum());
				u.setFullName(this.proveedorSelected.getPersona().getNombreCompleto());
				u.setActivo(true);
				u.setPassword(UtilStaticMetodos.getSHA256(u.getAccount()));
				
				this.proveedorSelected.setUsuario(u);
				
			}else {
				
				this.proveedorSelected.setUsuario(null);
				
			}


			return;
		}

	}
	
	public boolean getVisibleUsuario() {
		
		if(this.proveedorSelected.getProveedorTipo().getSigla().compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) ==0) {
			
			return true;
			
		}
		
		return false;
		
	}


	public Proveedor getProveedorSelected() {
		return proveedorSelected;
	}

	public void setProveedorSelected(Proveedor docenteSelected) {
		this.proveedorSelected = docenteSelected;
	}

	public boolean isOpCrearProveedor() {
		return opCrearProveedor;
	}

	public void setOpCrearProveedor(boolean opCrearProveedor) {
		this.opCrearProveedor = opCrearProveedor;
	}

	public boolean isOpEditarProveedor() {
		return opEditarProveedor;
	}

	public void setOpEditarProveedor(boolean opEditarProveedor) {
		this.opEditarProveedor = opEditarProveedor;
	}

	public boolean isOpBorrarProveedor() {
		return opBorrarProveedor;
	}

	public void setOpBorrarProveedor(boolean opBorrarProveedor) {
		this.opBorrarProveedor = opBorrarProveedor;
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

	public List<Object[]> getlProveedores() {
		return lProveedores;
	}

	public void setlProveedores(List<Object[]> lProveedores) {
		this.lProveedores = lProveedores;
	}

	public FinderModel getPersonaFinder() {
		return personaFinder;
	}

	public void setPersonaFinder(FinderModel personaFinder) {
		this.personaFinder = personaFinder;
	}

	public FinderModel getProveedorTipoFinder() {
		return proveedorTipoFinder;
	}

	public void setProveedorTipoFinder(FinderModel proveedorTipoFinder) {
		this.proveedorTipoFinder = proveedorTipoFinder;
	}
	
}
