package com.instituto.sistema.tesoreria;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;


import com.doxacore.components.finder.FinderInterface;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Usuario;
import com.instituto.modelo.Caja;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CajaVM extends TemplateViewModelLocal implements FinderInterface{

	private List<Object[]> lCajas;
	private List<Object[]> lCajasOri;
	private Caja cajaSelected;
	
	private boolean opCrearCaja;
	private boolean opEditarCaja;
	private boolean opBorrarCaja;

	@Init(superclass = true)
	public void initCajaVM() {

		
		this.cargarCajas();
		this.inicializarFiltros();
		
	}

	@AfterCompose(superclass = true)
	public void afterCajaVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearCaja = this.operacionHabilitada(ParamsLocal.OP_CREAR_CAJA);
		this.opEditarCaja = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CAJA);
		this.opBorrarCaja = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CAJA);
		
		
	}
	
	public void cargarCajas() {
		
		String sqlCajas = this.um.getSql("caja/listaCaja.sql").replace("?1", this.getCurrentSede().getSedeid()+"");
		
		this.lCajas = this.reg.sqlNativo(sqlCajas);
		this.lCajasOri = this.lCajas;
		
		
	}
	
	private String filtroColumns[];
	
	private void inicializarFiltros() {

		this.filtroColumns = new String[6]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el modelo
											

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}
	
	@Command
	@NotifyChange("lComprobantesElectronicos")
	public void filtrarComprobanteElectronico() {

		this.lCajas = filtrarListaObject(this.filtroColumns, this.lCajasOri);

	}
	
	private Window modal;
	private boolean editar = false;

	@Command
	public void modalCajaAgregar() {

		if(!this.opCrearCaja)
			return;

		this.editar = false;
		modalCaja(-1);

	}

	@Command
	public void modalCaja(@BindingParam("cajaid") long cajaid) {
		
		this.inicializarFinders();

		if (cajaid != -1) {

			if(!this.opEditarCaja)
				return;
			
			this.cajaSelected = this.reg.getObjectById(Caja.class.getName(), cajaid);
			this.editar = true;

		} else {
			
			cajaSelected = new Caja();
			this.cajaSelected.setSede(this.getCurrentSede());
			this.cajaSelected.setUsuarioCaja(this.getCurrentUser());
			

		}

		modal = (Window) Executions.createComponents("/instituto/zul/tesoreria/cajaModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lCajas")
	public void guardar() {
		
		if(this.cajaSelected.getApertura() == null) {
			
			this.cajaSelected.setApertura(new Date());		
			
		}
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		/*String sqlCaja = "select * from cajas \n" + 
				"WHERE sedeid = "+this.getCurrentSede().getSedeid()+
				" and DATE(apertura) = '"+sdf.format(this.cajaSelected.getApertura())+
				"' and usuariocajaid = "+this.cajaSelected.getUsuarioCaja().getUsuarioid()+" ;";
		
		List<Object[]> result = this.reg.sqlNativo(sqlCaja);*/
		
		List<Caja> cajas1 = this.reg.getAllObjectsByCondicionOrder(Caja.class.getName(), 
				"sedeid = "+this.getCurrentSede().getSedeid()+
				" and DATE(apertura) = '"+sdf.format(this.cajaSelected.getApertura())+
				"' and usuariocajaid = "+this.cajaSelected.getUsuarioCaja().getUsuarioid(), 
				null);

		
		if (cajas1.size() > 0) {
			
			this.mensajeError("Ya existe una caja con este usuario para esta fecha en esta sede.");
			return;
		}
		
						
		List<Caja> cajas2 = this.reg.getAllObjectsByCondicionOrder(Caja.class.getName(), 
				"sedeid = "+this.getCurrentSede().getSedeid()+"\n"+
				"and usuariocajaid = "+this.cajaSelected.getUsuarioCaja().getUsuarioid()+"\n", 
				"cajaid desc");
		
		if (cajas2.size() > 0) {
			
			for (Caja x : cajas2) {
				
				if (x.getCierre() == null) {
					
					this.mensajeError("Este Usuario tiene Cajas no cerradas.");
					return;
					
				}
				
			}
			
		}
		
		
		this.save(cajaSelected);
			
		this.cajaSelected = null;

		this.cargarCajas();

		this.modal.detach();
		
		if (editar) {
			
			Notification.show("La Caja fue Actualizada.");
			this.editar = false;
		}else {
			
			Notification.show("La Caja fue agregada.");
		}
		
		

	}
	
	@Command
	public void modalCerrar(@BindingParam("cajaid") long cajaid) {
		
		this.cajaSelected = this.reg.getObjectById(Caja.class.getName(), cajaid);
		
				
		modal = (Window) Executions.createComponents("/instituto/zul/tesoreria/cajaCerrarModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}
	
	@Command
	@NotifyChange("*")
	public void cerrarCaja() {
		
		this.cajaSelected.setCierre(new Date());
		this.cajaSelected.setUsuarioCierre(this.getCurrentUser());
		
		this.save(this.cajaSelected);
		
		this.cargarCajas();
		
		this.modal.detach();
		
	}
	
	//Seccion Finder
	
	private FinderModel usuarioFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlUsuario = "Select usuarioid, account as usuario from usuarios\n"
				+"order by usuarioid asc";

		// String sqlContribuyente =
		// this.um.getSql("contribuyente/listaContribuyentes.sql");

		/*if (!this.isUserRolMaster()) {

			sqlContribuyente = sqlContribuyente.replace("--", "").replace("?1",
					this.getCurrentUser().getUsuarioid() + "");

		}*/

		usuarioFinder = new FinderModel("Contribuyente", sqlUsuario);

		

		// System.out.println("El tamaño de lContribuyetes es:
		// "+this.lContribuyentes.size());
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.usuarioFinder.getNameFinder()) == 0) {

			this.usuarioFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.usuarioFinder, "listFinder");

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.usuarioFinder.getNameFinder()) == 0) {

			this.usuarioFinder
					.setListFinder(this.filtrarListaObject(filter, this.usuarioFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.usuarioFinder, "listFinder");

		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.usuarioFinder.getNameFinder()) == 0) {

			this.cajaSelected.setUsuarioCaja(this.reg.getObjectById(Usuario.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "contribuyenteFinder");
		}

		

	}


	public List<Object[]> getlCajas() {
		return lCajas;
	}

	public void setlCajas(List<Object[]> lCajas) {
		this.lCajas = lCajas;
	}

	public Caja getCajaSelected() {
		return cajaSelected;
	}

	public void setCajaSelected(Caja cajaSelected) {
		this.cajaSelected = cajaSelected;
	}

	public boolean isOpCrearCaja() {
		return opCrearCaja;
	}

	public void setOpCrearCaja(boolean opCrearCaja) {
		this.opCrearCaja = opCrearCaja;
	}

	public boolean isOpEditarCaja() {
		return opEditarCaja;
	}

	public void setOpEditarCaja(boolean opEditarCaja) {
		this.opEditarCaja = opEditarCaja;
	}

	public boolean isOpBorrarCaja() {
		return opBorrarCaja;
	}

	public void setOpBorrarCaja(boolean opBorrarCaja) {
		this.opBorrarCaja = opBorrarCaja;
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

	public FinderModel getUsuarioFinder() {
		return usuarioFinder;
	}

	public void setUsuarioFinder(FinderModel usuarioFinder) {
		this.usuarioFinder = usuarioFinder;
	}

	
	
	

	
		

}
