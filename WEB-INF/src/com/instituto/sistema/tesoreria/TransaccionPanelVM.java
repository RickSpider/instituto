package com.instituto.sistema.tesoreria;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import com.doxacore.TemplateViewModel;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.instituto.modelo.Caja;
import com.instituto.modelo.CobranzaDetalleCobro;
import com.instituto.modelo.Cuenta;
import com.instituto.modelo.Transaccion;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;


public class TransaccionPanelVM extends TemplateViewModelLocal {
	
	private List<CobranzaDetalleCobro> lCobranzasDetallescobros;
	private List<CobranzaDetalleCobro> lCobranzasDetallescobrosAux;
	private Caja cajaSelected;
	private Cuenta cuentaSelected;
	

	@Init(superclass = true)
	public void initTransaccionVM() {

		this.inicializarFinders();
		
	}

	@AfterCompose(superclass = true)
	public void afterTransaccionVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		
		
		
		
	}
	
	@NotifyChange("*")
	public void cargarCobranzasDetallesCobros() {
		
		String sql1 = "select string_agg(cast(cobranzaid as text), ',') as ids, 'ids' as col from cobranzas \n" + 
				"where cajaid = "+this.cajaSelected.getCajaid()+"\n"
				+ "and anulado = false;";
		
		//System.out.println("===========\n"+sql1+"\n=============");
		
		
		List<Object[]> result1 = this.reg.sqlNativo(sql1);
		
		if (result1.size() > 0) {
			
			if (result1.get(0)[0] == null) {
				this.lCobranzasDetallescobros = null;
				return;
			}
		
			String ids = result1.get(0)[0].toString();
			this.lCobranzasDetallescobros = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalleCobro.class.getName(), "cobranzaid in ("+ids+") and depositado = false", null );
			
		}
		
				
	}
	
	//Seccion Finder
	
	private FinderModel cajaFinder;
	private FinderModel cuentaFinder;


	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlCaja = "select c.cajaid as id, u1.account as usuario ,c.apertura, c.cierre from cajas c\n" + 
				"left join usuarios u1 on u1.usuarioid = c.usuariocajaid\n" + 
				"where cierre is not null\n" + 
				"order by c.cajaid desc;";

		cajaFinder = new FinderModel("Caja", sqlCaja);
		
		String sqlCuenta = this.um.getSql("cuenta/listaCuentas.sql").replace("?1", this.getCurrentSede().getSedeid()+"");
		
		cuentaFinder = new FinderModel("Cuenta", sqlCuenta);
		
		

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.cajaFinder.getNameFinder()) == 0) {

			this.cajaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cajaFinder, "listFinder");

		}
		
		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cajaFinder.getNameFinder()) == 0) {

			this.cajaFinder
					.setListFinder(this.filtrarListaObject(filter, this.cajaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.cajaFinder, "listFinder");

		}
		
		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder
					.setListFinder(this.filtrarListaObject(filter, this.cuentaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

		}


	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cajaFinder.getNameFinder()) == 0) {

			this.cajaSelected = this.reg.getObjectById(Caja.class.getName(), id);
			this.cargarCobranzasDetallesCobros();
		}


		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaSelected = this.reg.getObjectById(Cuenta.class.getName(), id);
			
		}

		

	}
	
	@Command
	@NotifyChange("*")
	public void confirmar() {
		
		if (this.cuentaSelected == null) {
			
			this.mensajeInfo("Debes agregar una cuenta para poder continuar.");
			return;
		}
		
		if (this.lCobranzasDetallescobrosAux == null || this.lCobranzasDetallescobrosAux.size() == 0) {
			
			this.mensajeInfo("Tienes que seleccionar cobros para cargar a la cuenta.");
			return;
		}
		
		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					procesar();

				}

			}

		};

		this.mensajeEliminar("Ser realizara la carga a las cuentas. \n Continuar?", event);
		
	}
	
	public void procesar() {
				
		Tipo transaccionTipo = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_TRANSACCION_INGRESO);
		
		for (CobranzaDetalleCobro x :this.lCobranzasDetallescobrosAux) {
			
			Transaccion t = new Transaccion();
			
			t.setCuenta(this.cuentaSelected);
			t.setMonto(x.getMonto());
			t.setCobranzadetallecobro(x);
			t.setTransaccionTipo(transaccionTipo);
						
			this.save(t);
			
			x.setDepositado(true);
			this.save(x);
		}
		
		this.cajaSelected = null;
		this.cuentaSelected = null;
		this.lCobranzasDetallescobros = null;
		this.lCobranzasDetallescobrosAux =  null;
		
		BindUtils.postNotifyChange(null, null, this, "cajaSelected");
		BindUtils.postNotifyChange(null, null,  this,"cuentaSelected");
		BindUtils.postNotifyChange(null, null, this, "lCobranzasDetallescobros");
		BindUtils.postNotifyChange(null, null, this, "lCobranzasDetallescobrosAux");

	}

	public List<CobranzaDetalleCobro> getlCobranzasDetallescobros() {
		return lCobranzasDetallescobros;
	}

	public void setlCobranzasDetallescobros(List<CobranzaDetalleCobro> lCobranzasDetallescobros) {
		this.lCobranzasDetallescobros = lCobranzasDetallescobros;
	}

	public Caja getCajaSelected() {
		return cajaSelected;
	}

	public void setCajaSelected(Caja cajaSelected) {
		this.cajaSelected = cajaSelected;
	}

	public FinderModel getCajaFinder() {
		return cajaFinder;
	}

	public void setCajaFinder(FinderModel cajaFinder) {
		this.cajaFinder = cajaFinder;
	}

	public Cuenta getCuentaSelected() {
		return cuentaSelected;
	}

	public void setCuentaSelected(Cuenta cuentaSelected) {
		this.cuentaSelected = cuentaSelected;
	}

	public FinderModel getCuentaFinder() {
		return cuentaFinder;
	}

	public void setCuentaFinder(FinderModel cuentaFinder) {
		this.cuentaFinder = cuentaFinder;
	}

	public List<CobranzaDetalleCobro> getlCobranzasDetallescobrosAux() {
		return lCobranzasDetallescobrosAux;
	}

	public void setlCobranzasDetallescobrosAux(List<CobranzaDetalleCobro> lCobranzasDetallescobrosAux) {
		this.lCobranzasDetallescobrosAux = lCobranzasDetallescobrosAux;
	}
	

}
