package com.instituto.sistema.tesoreria;

import java.text.DecimalFormat;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.doxacore.components.Statbox;
import com.doxacore.components.finder.FinderModel;
import com.instituto.modelo.Cuenta;
import com.instituto.modelo.Transaccion;
import com.instituto.util.TemplateViewModelLocal;

public class CuentaSaldoVM extends TemplateViewModelLocal {

	private Statbox statboxSelected;
	private Cuenta cuentaSelected;
	private List<Transaccion>lTransacciones;

	@Init(superclass = true)
	public void initCajaVM() {

		this.statboxSelected = new Statbox("0", "SALDO", "0", "fa-money", Statbox.styleGreenDarker);

		this.inicializarFinders();
	}

	@AfterCompose(superclass = true)
	public void afterCajaVM() {

	}

	@Override
	protected void inicializarOperaciones() {

	}

	public Statbox getStatboxSelected() {
		return statboxSelected;
	}

	public void setStatboxSelected(Statbox statboxSelected) {
		this.statboxSelected = statboxSelected;
	}

	private FinderModel cuentaFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		String sqlCuenta = this.um.getSql("cuenta/listaCuentas.sql").replace("?1", this.getCurrentSede().getSedeid()+"");
		
		cuentaFinder = new FinderModel("Cuenta", sqlCuenta);
		
				
		

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.setListFinder(this.filtrarListaObject(filter, this.cuentaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaSelected = this.reg.getObjectById(Cuenta.class.getName(), id);
			
			List<Object[]> result = this.reg.sqlNativo("Select coalesce(sum(monto),0) as total, '' from transacciones\n" + 
					"where cuentaid = "+this.cuentaSelected.getCuentaid()+";");
			
			this.statboxSelected.setValue(new DecimalFormat("#,###").format(result.get(0)[0]).toString());

			this.cargarTransacciones();

		}

	}
	
	@NotifyChange("*")
	public void cargarTransacciones() {
		
		this.lTransacciones = this.reg.getAllObjectsByCondicionOrder(Transaccion.class.getName(), "cuentaid = "+this.cuentaSelected.getCuentaid(), "transaccionid desc");
				
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

	public List<Transaccion> getlTransacciones() {
		return lTransacciones;
	}

	public void setlTransacciones(List<Transaccion> lTransacciones) {
		this.lTransacciones = lTransacciones;
	}

}
