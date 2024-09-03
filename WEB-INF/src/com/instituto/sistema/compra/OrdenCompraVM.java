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
import com.instituto.modelo.CursoVigenteMateria;
import com.instituto.modelo.OrdenCompra;
import com.instituto.modelo.OrdenCompraDetalle;
import com.instituto.modelo.OrdenCompraDetalleDoc;
import com.instituto.modelo.Proveedor;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;
import com.instituto.util.component.FinderModeloModel;

public class OrdenCompraVM extends TemplateViewModelLocal {

	private List<Object[]> lOrdenesCompras;
	private List<Object[]> lOrdenesComprasOri;
	private OrdenCompra ordenCompraSelected;
	private OrdenCompraDetalle ordenCompraDetalleSelected;

	private boolean opCrearOrdenCompra;
	private boolean opEditarOrdenCompra;
	private boolean opBorrarOrdenCompra;

	private CuentaRubro cuentaRubroSelected;
	private Tipo impuestoSelected;
	private double monto;
	private CursoVigenteMateria cursoVigenteMateriaSelected;

	private boolean verDetalleDoc = false;

	@Init(superclass = true)
	public void initOrdenCompraVM() {

		cargarOrdenesCompras();
		inicializarFiltros();
		inicializarFinders();

	}

	@AfterCompose(superclass = true)
	public void afterComposeOrdenCompraVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearOrdenCompra = this.operacionHabilitada(ParamsLocal.OP_CREAR_ORDENCOMPRA);
		this.opEditarOrdenCompra = this.operacionHabilitada(ParamsLocal.OP_EDITAR_ORDENCOMPRA);
		this.opBorrarOrdenCompra = this.operacionHabilitada(ParamsLocal.OP_BORRAR_ORDENCOMPRA);

	}

	private void cargarOrdenesCompras() {

		String sqlOrdenCompra = this.um.getSql("ordenCompra/listaOrdenCompra.sql");

		this.lOrdenesCompras = this.reg.sqlNativo(sqlOrdenCompra);
		this.lOrdenesComprasOri = this.lOrdenesCompras;
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
	@NotifyChange("lOrdenesCompras")
	public void filtrarOrdenCompra() {

		this.lOrdenesCompras = this.filtrarListaObject(this.filtroColumns, this.lOrdenesComprasOri);

	}

	// fin seccion

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalOrdenCompraAgregar() {

		if (!this.opCrearOrdenCompra)
			return;

		this.editar = false;
		modalOrdenCompra(-1);

	}

	@Command
	public void modalOrdenCompra(@BindingParam("ordenCompraid") long ordenCompraid) {

		this.monto = 0;
		this.cuentaRubroSelected = null;
		this.impuestoSelected = null;
		this.cursoVigenteMateriaSelected = null;
		this.ordenCompraDetalleSelected = null;
		this.verDetalleDoc = false;

		if (ordenCompraid != -1) {

			if (!this.opEditarOrdenCompra)
				return;

			this.ordenCompraSelected = this.reg.getObjectById(OrdenCompra.class.getName(), ordenCompraid);

			if (this.ordenCompraSelected.getProveedor().getProveedorTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) == 0) {

				this.verDetalleDoc = true;

			} else {

				this.verDetalleDoc = false;

			}

		} else {

			ordenCompraSelected = new OrdenCompra();
			ordenCompraSelected
					.setMonedaTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_MONEDA_GUARANI));
			ordenCompraSelected.setEstadoTipo(
					this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_ORDEN_COMPRA_ACTIVO));

		}

		modal = (Window) Executions.createComponents("/instituto/zul/compra/ordenCompraModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	@Command
	@NotifyChange("*")
	public void agregarDetalle() {

		if (this.ordenCompraSelected.getProveedor() == null) {

			this.mensajeError("No hay proveedor Cargado");
			return;

		}

		if (this.cuentaRubroSelected == null) {

			this.mensajeError("No hay Cuenta agregada");
			return;

		}

		if (this.monto == 0) {

			this.mensajeError("El monto debe ser mayor a 0");
			return;

		}
		
		if (this.impuestoSelected == null) {
			
			this.mensajeError("No hay impuesto Seleccionado");
			return;
		}

		this.ordenCompraDetalleSelected = new OrdenCompraDetalle();
		this.ordenCompraDetalleSelected.setCuentaRubro(this.cuentaRubroSelected);
		this.ordenCompraDetalleSelected.setMonto(this.monto);
		this.ordenCompraDetalleSelected.setOrdenCompra(this.ordenCompraSelected);
		this.ordenCompraDetalleSelected.setImpuestoTipo(this.impuestoSelected);

		this.ordenCompraSelected.getDetalles().add(this.ordenCompraDetalleSelected);
		this.ordenCompraDetalleSelected = null;

		this.cuentaRubroSelected = null;
		this.impuestoSelected = null;
		this.monto = 0;

	}
	
	
	@Command
	@NotifyChange("*")
	public void borrarDetalle(@BindingParam("dato") OrdenCompraDetalle dato) {
		
		this.ordenCompraSelected.getDetalles().remove(dato);
		
	}

	@Command
	@NotifyChange("lOrdenesCompras")
	public void guardar() {
		
		if (this.ordenCompraSelected.getFechaVencimiento() == null) {
			
			this.mensajeInfo("La orden de compra debe tener un vencimiento");
			return;
			
		}
		
		if (this.ordenCompraSelected.getDetalles().size() == 0) {
			
			this.mensajeInfo("No puedes guardar sin cargar por lo menos un detalle.");
			return;
			
		}

		this.save(ordenCompraSelected);

		this.ordenCompraSelected = null;

		this.cargarOrdenesCompras();

		this.modal.detach();

		if (editar) {

			Notification.show("La OrdenCompra fue Actualizada.");
			this.editar = false;
		} else {

			Notification.show("La OrdenCompra fue agregada.");
		}

	}

	// fin modal

	@Command
	public void borrarOrdenCompraConfirmacion(@BindingParam("ordenCompraid") final long ordenCompraid) {

		if (!this.opBorrarOrdenCompra)
			return;

		OrdenCompra s = this.reg.getObjectById(OrdenCompra.class.getName(), ordenCompraid);

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarOrdenCompra(s);

				}

			}

		};

		this.mensajeEliminar("El OrdenCompra sera eliminada. \n Continuar?", event);
	}

	private void borrarOrdenCompra(OrdenCompra ordenCompra) {

		this.reg.deleteObject(ordenCompra);

		this.cargarOrdenesCompras();

		BindUtils.postNotifyChange(null, null, this, "lOrdenesCompras");

	}

	private FinderModel cuentaFinder;
	private FinderModel monedaFinder;
	private FinderModel estadoTipoFinder;
	private FinderModel proveedorFinder;
	private FinderModel impuestoTipoFinder;

	private FinderModeloModel<CursoVigenteMateria> cursoVigenteMateriaFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlCuenta = this.um.getSql("cuentaRubro/buscarCuentaRubro.sql");
		cuentaFinder = new FinderModel("Cuenta", sqlCuenta);

		String sqlMoneda = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_MONEDA);
		monedaFinder = new FinderModel("Moneda", sqlMoneda);

		String sqlEstado = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_ORDEN_COMPRA);
		estadoTipoFinder = new FinderModel("OrdenCompraEstado", sqlEstado);

		String sqlProveedor = this.um.getSql("proveedor/buscarProveedorSede.sql").replace("?1",
				this.getCurrentSede().getSedeid() + "");
		proveedorFinder = new FinderModel("Proveedor", sqlProveedor);
		
		String sqlImpuestoTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_IMPUESTO);
		impuestoTipoFinder = new FinderModel("Impuesto", sqlImpuestoTipo);
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.proveedorFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.proveedorFinder, "listFinder");

			return;

		}

		if (finder.compareTo(this.monedaFinder.getNameFinder()) == 0) {

			this.monedaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.monedaFinder, "listFinder");

			return;

		}

		if (finder.compareTo(this.estadoTipoFinder.getNameFinder()) == 0) {

			this.estadoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.estadoTipoFinder, "listFinder");

			return;

		}

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.impuestoTipoFinder.getNameFinder()) == 0) {

			this.impuestoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.impuestoTipoFinder, "listFinder");

			return;

		}

		if (finder.compareTo(this.cursoVigenteMateriaFinder.getNameFinder()) == 0) {

			this.cursoVigenteMateriaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cursoVigenteMateriaFinder, "listFinderModelo");

			return;

		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.setListFinder(this.filtrarListaObject(filter, this.cuentaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

			return;

		}

		if (finder.compareTo(this.monedaFinder.getNameFinder()) == 0) {

			this.monedaFinder.setListFinder(this.filtrarListaObject(filter, this.monedaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.monedaFinder, "listFinder");

			return;

		}

		if (finder.compareTo(this.estadoTipoFinder.getNameFinder()) == 0) {

			this.estadoTipoFinder
					.setListFinder(this.filtrarListaObject(filter, this.estadoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.estadoTipoFinder, "listFinder");

			return;

		}

		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.proveedorFinder
					.setListFinder(this.filtrarListaObject(filter, this.proveedorFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.proveedorFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.impuestoTipoFinder.getNameFinder()) == 0) {

			this.proveedorFinder
					.setListFinder(this.filtrarListaObject(filter, this.impuestoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.impuestoTipoFinder, "listFinder");

			return;

		}


	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			/*
			 * this.ordenCompraDetalleSelected.setOrdenCompra(this.ordenCompraSelected);
			 * this.ordenCompraDetalleSelected.setCuentaRubro(this.reg.getObjectById(
			 * CuentaRubro.class.getName(), id));
			 */

			this.cuentaRubroSelected = this.reg.getObjectById(CuentaRubro.class.getName(), id);

			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}

		if (finder.compareTo(this.monedaFinder.getNameFinder()) == 0) {

			this.ordenCompraSelected.setMonedaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}

		if (finder.compareTo(this.estadoTipoFinder.getNameFinder()) == 0) {

			this.ordenCompraSelected.setEstadoTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}

		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.ordenCompraSelected.setProveedor(this.reg.getObjectById(Proveedor.class.getName(), id));

			if (this.ordenCompraSelected.getProveedor().getProveedorTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) == 0) {

				this.verDetalleDoc = true;

			} else {

				this.verDetalleDoc = false;

			}

			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			this.cursoVigenteMateriaFinder = new FinderModeloModel<CursoVigenteMateria>("CursoVigenteMateria", this.reg,
					CursoVigenteMateria.class.getName(), "proveedorid = "
							+ this.ordenCompraSelected.getProveedor().getProveedorid() + " and estadoid != 14");

			return;
		}
		
		if (finder.compareTo(this.impuestoTipoFinder.getNameFinder()) == 0) {
			
			this.impuestoSelected = this.reg.getObjectById(Tipo.class.getName(), id);
			return;
		}

	}

	// seccion finderModelo
	@Command
	@NotifyChange("*")
	public void onSelectetItemModeloFinder(@BindingParam("dato") Object dato, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.cursoVigenteMateriaFinder.getNameFinder()) == 0) {

			this.cursoVigenteMateriaSelected = (CursoVigenteMateria) dato;

			return;

		}

	}

	@Command
	@NotifyChange("*")
	public void refrescarDetallesDoc(@BindingParam("dato") OrdenCompraDetalle dato) {

		this.ordenCompraDetalleSelected = dato;

	}

	@Command
	@NotifyChange("*")
	public void agragarDocumento() {

		if (this.ordenCompraSelected.getProveedor() == null) {

			this.mensajeError("No hay proveedor Cargado");
			return;

		}

		if (this.ordenCompraDetalleSelected == null) {

			this.mensajeError("Debes seleccionar un detalle para agregar.");
			return;

		}

		if (this.cursoVigenteMateriaSelected == null) {

			this.mensajeError("Debes seleccionar un documento.");
			return;

		}

		OrdenCompraDetalleDoc ocdd = new OrdenCompraDetalleDoc();
		ocdd.setOrdenCompraDetalle(this.ordenCompraDetalleSelected);
		ocdd.setCursoVigenteMateria(this.cursoVigenteMateriaSelected);

		this.ordenCompraDetalleSelected.getDetallesDoc().add(ocdd);

		this.cursoVigenteMateriaSelected = null;

	}

	public List<Object[]> getlOrdenesCompras() {
		return lOrdenesCompras;
	}

	public void setlOrdenesCompras(List<Object[]> lOrdenesCompras) {
		this.lOrdenesCompras = lOrdenesCompras;
	}

	public OrdenCompra getOrdenCompraSelected() {
		return ordenCompraSelected;
	}

	public void setOrdenCompraSelected(OrdenCompra ordenCompraSelected) {
		this.ordenCompraSelected = ordenCompraSelected;
	}

	public boolean isOpCrearOrdenCompra() {
		return opCrearOrdenCompra;
	}

	public void setOpCrearOrdenCompra(boolean opCrearOrdenCompra) {
		this.opCrearOrdenCompra = opCrearOrdenCompra;
	}

	public boolean isOpEditarOrdenCompra() {
		return opEditarOrdenCompra;
	}

	public void setOpEditarOrdenCompra(boolean opEditarOrdenCompra) {
		this.opEditarOrdenCompra = opEditarOrdenCompra;
	}

	public boolean isOpBorrarOrdenCompra() {
		return opBorrarOrdenCompra;
	}

	public void setOpBorrarOrdenCompra(boolean opBorrarOrdenCompra) {
		this.opBorrarOrdenCompra = opBorrarOrdenCompra;
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

	public OrdenCompraDetalle getOrdenCompraDetalleSelected() {
		return ordenCompraDetalleSelected;
	}

	public void setOrdenCompraDetalleSelected(OrdenCompraDetalle ordenCompraDetalleSelected) {
		this.ordenCompraDetalleSelected = ordenCompraDetalleSelected;
	}

	public FinderModel getRubroFinder() {
		return cuentaFinder;
	}

	public void setRubroFinder(FinderModel rubroFinder) {
		this.cuentaFinder = rubroFinder;
	}

	public FinderModel getMonedaFinder() {
		return monedaFinder;
	}

	public void setMonedaFinder(FinderModel monedaFinder) {
		this.monedaFinder = monedaFinder;
	}

	public FinderModel getEstadoTipoFinder() {
		return estadoTipoFinder;
	}

	public void setEstadoTipoFinder(FinderModel estadoTipoFinder) {
		this.estadoTipoFinder = estadoTipoFinder;
	}

	public FinderModel getCuentaFinder() {
		return cuentaFinder;
	}

	public void setCuentaFinder(FinderModel cuentaFinder) {
		this.cuentaFinder = cuentaFinder;
	}

	public FinderModel getProveedorFinder() {
		return proveedorFinder;
	}

	public void setProveedorFinder(FinderModel proveedorFinder) {
		this.proveedorFinder = proveedorFinder;
	}

	public FinderModeloModel<CursoVigenteMateria> getCursoVigenteMateriaFinder() {
		return cursoVigenteMateriaFinder;
	}

	public void setCursoVigenteMateriaFinder(FinderModeloModel<CursoVigenteMateria> cursoVigenteMateriaFinder) {
		this.cursoVigenteMateriaFinder = cursoVigenteMateriaFinder;
	}

	public CursoVigenteMateria getCursoVigenteMateriaSelected() {
		return cursoVigenteMateriaSelected;
	}

	public void setCursoVigenteMateriaSelected(CursoVigenteMateria cursoVigenteMateriaSelected) {
		this.cursoVigenteMateriaSelected = cursoVigenteMateriaSelected;
	}

	public CuentaRubro getCuentaRubroSelected() {
		return cuentaRubroSelected;
	}

	public void setCuentaRubroSelected(CuentaRubro cuentaRubroSelected) {
		this.cuentaRubroSelected = cuentaRubroSelected;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public boolean isVerDetalleDoc() {
		return verDetalleDoc;
	}

	public void setVerDetalleDoc(boolean verDetalleDoc) {
		this.verDetalleDoc = verDetalleDoc;
	}

	public Tipo getImpuestoSelected() {
		return impuestoSelected;
	}

	public void setImpuestoSelected(Tipo impuestoSelected) {
		this.impuestoSelected = impuestoSelected;
	}

	public FinderModel getImpuestoTipoFinder() {
		return impuestoTipoFinder;
	}

	public void setImpuestoTipoFinder(FinderModel impuestoTipoFinder) {
		this.impuestoTipoFinder = impuestoTipoFinder;
	}

}
