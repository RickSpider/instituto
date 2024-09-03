package com.instituto.sistema.compra;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.instituto.modelo.Pago;
import com.instituto.modelo.PagoDetalle;
import com.instituto.modelo.PagoDetalleDoc;
import com.instituto.modelo.Proveedor;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;
import com.instituto.util.component.FinderModeloModel;

public class PagoVM extends TemplateViewModelLocal {

	private List<Object[]> lPagos;
	private List<Object[]> lPagosOri;
	private Pago pagoSelected;
	private PagoDetalle pagoDetalleSelected;

	private boolean opCrearPago;
	private boolean opEditarPago;
	private boolean opBorrarPago;

	private CuentaRubro cuentaRubroSelected;
	private double monto;
	private CursoVigenteMateria cursoVigenteMateriaSelected;
	private Tipo impuestoSelected;

	private boolean verDetalleDoc = false;
	
	
	private boolean[] camposCabecera = {false, false, false, false, false, false,false,false,false,false,false,false};
	private boolean camposDetalle =false;
	
	private Date fechaDesde;
	private Date fechaHasta;
	private Tipo comprobanteTipoFiltro;
	
	private double totalGral;

	@Init(superclass = true)
	public void initPagoVM() {

		cargarFiltroFecha();
		cargarPagos();
		inicializarFiltros();
		inicializarFinders();


	}
	
	
	@AfterCompose(superclass = true)
	public void afterComposePagoVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		this.opCrearPago = this.operacionHabilitada(ParamsLocal.OP_CREAR_PAGO);
		this.opEditarPago = this.operacionHabilitada(ParamsLocal.OP_EDITAR_PAGO);
		this.opBorrarPago = this.operacionHabilitada(ParamsLocal.OP_BORRAR_PAGO);

	}

	@Command
	@NotifyChange("*")
	public void filtrarPorFechas() {
		
		if (this.fechaDesde == null) {
			
			this.mensajeError("Debes poner Fecha Desde.");
			return;
		}
		
		if (this.fechaHasta == null) {
			
			this.mensajeError("Debes poner Fecha Hasta.");
			return;
		}
		
		
		cargarPagos();
		
	}
	
	private void cargarFiltroFecha() {
		
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());		
		
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        this.fechaDesde = calendar.getTime();
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        this.fechaHasta = calendar.getTime();
        
	}
	
	private void cargarPagos() {

		String sqlPago = this.um.getSql("pago/listaPago.sql");
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.fechaDesde = this.um.modificarHorasMinutosSegundos(this.fechaDesde, 00, 00, 00, 000);
		this.fechaHasta = this.um.modificarHorasMinutosSegundos(this.fechaHasta, 23, 59, 59, 999);
			
		sqlPago = sqlPago.replace("?1", sdf.format(fechaDesde)).replace("?2", sdf.format(fechaHasta));
		
		if (this.comprobanteTipoFiltro != null) {
			
			sqlPago = sqlPago.replace("--1", "").replace("?3", this.comprobanteTipoFiltro.getTipoid()+"");
			
		}
		
		//System.out.println(sqlPago);

		this.lPagos = this.reg.sqlNativo(sqlPago);
		this.lPagosOri = this.lPagos;
		
		this.totalGral = 0;
		
		for (Object[] x: this.lPagosOri) {
			
			this.totalGral += (double) x[6];
			
		}
		
	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[6]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lPagos")
	public void filtrarPago() {

		this.lPagos = this.filtrarListaObject(this.filtroColumns, this.lPagosOri);

	}

	// fin seccion

	// seccion modal

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalPagoAgregar() {

		if (!this.opCrearPago)
			return;

		this.editar = false;
		modalPago(-1);

	}

	@Command
	public void modalPago(@BindingParam("pagoid") long pagoid) {

		this.monto = 0;
		this.cuentaRubroSelected = null;
		this.cursoVigenteMateriaSelected = null;
		this.pagoDetalleSelected = null;
		this.impuestoSelected = null;
		this.verDetalleDoc = false;
		
		for (int i = 0 ; i<this.camposCabecera.length; i++) {
			
			this.camposCabecera[i] = false;
			
		}
		this.camposDetalle = false;
		
		
			
		inicializarFinders();

		if (pagoid != -1) {
			
			this.editar = true;

			if (!this.opEditarPago)
				return;
			
			this.camposCabecera[0] = true;
			this.camposCabecera[1] = true;
			this.camposCabecera[2] = true;
			this.camposCabecera[6] = true;
			

			this.pagoSelected = this.reg.getObjectById(Pago.class.getName(), pagoid);
						
			
			if (this.pagoSelected.getPagoRelacionado() != null) {
				
				
				
				this.camposCabecera[3] = true;
				this.camposCabecera[4] = true;
				
				
				
			}
			
			if (this.pagoSelected.getOrdenCompra() != null) {
				
				
				
				this.camposCabecera[3] = true;
				this.camposCabecera[4] = true;
				
				this.camposDetalle = true;
				
			}
			
			List<Pago> lPagos = this.reg.getAllObjectsByCondicionOrder(Pago.class.getName(), "pagorelacionadoid = "+this.pagoSelected.getPagoid(), "pagoid desc" );
			
			if (lPagos.size() > 0) {
				
				for (int i = 0 ; i<this.camposCabecera.length; i++) {
					
					this.camposCabecera[i] = true;
					
				}
				this.camposDetalle = true;
				
				
			}

			if (this.pagoSelected.getProveedor().getProveedorTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) == 0) {

				this.verDetalleDoc = true;

			} else {

				this.verDetalleDoc = false;

			}

		} else {

			pagoSelected = new Pago();
			pagoSelected.setMonedaTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_MONEDA_GUARANI));
			pagoSelected.setComprobanteTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_FACTURA));
			pagoSelected.setCondicionVentaTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO));
			/*pagoSelected.setEstadoTipo(
					this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_ORDEN_COMPRA_ACTIVO));*/

		}

		modal = (Window) Executions.createComponents("/instituto/zul/compra/pagoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private void verificarCampos() {
		
		if (this.pagoSelected.getProveedor() == null) {

			this.mensajeError("No hay proveedor Cargado");
			return;

		}

		
		if(this.pagoSelected.getComprobanteTipo() == null) {
			
			this.mensajeError("Debes seleccionar un tipo de comprobante");
			return;
			
		}else if (this.pagoSelected.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0
				&& this.pagoSelected.getTimbrado() == null) {
			
			this.mensajeError("Debes agregar el timbrado del comprobante comprobante");
			return;
			
		}
		
		if(this.pagoSelected.getComprobanteNum() == null) {
			
			this.mensajeError("Debes agregar el numero de comprobante");
			return;
			
		}

	}

	@Command
	@NotifyChange("*")
	public void agregarDetalle() {

		this.verificarCampos();

		
		if (this.cuentaRubroSelected == null) {

			this.mensajeError("No hay Cuenta agregada");
			return;

		}
		

		if (this.monto == 0) {

			this.mensajeError("El monto debe ser mayor a 0");
			return;

		}
		
		if (this.impuestoSelected == null) {
			
			this.mensajeInfo("Debes agregar el impuesto.");
			
		}
		
		
		this.pagoDetalleSelected = new PagoDetalle();
		this.pagoDetalleSelected.setCuentaRubro(this.cuentaRubroSelected);
		this.pagoDetalleSelected.setMonto(this.monto);
		this.pagoDetalleSelected.setPago(this.pagoSelected);
		this.pagoDetalleSelected.setImpuestoTipo(this.impuestoSelected);

		this.pagoSelected.getDetalles().add(this.pagoDetalleSelected);
		this.pagoDetalleSelected = null;
		this.impuestoSelected = null;

		this.cuentaRubroSelected = null;
		this.monto = 0;
		
		this.calcularTotalesImpuesto();

	}
	
	private void calcularTotalesImpuesto() {
		
		double total10 = 0;
		double total5 = 0;
		double total0 = 0;
		
		for (PagoDetalle x : this.pagoSelected.getDetalles()) {
			
			if (x.getImpuestoTipo().getSigla().compareTo(ParamsLocal.SIGLA_IMPUESTO_IVA_10) == 0) {
				
				total10 += x.getMonto();
				
			}
			
			if (x.getImpuestoTipo().getSigla().compareTo(ParamsLocal.SIGLA_IMPUESTO_IVA_5) == 0) {
				
				total5 += x.getMonto();
				
			}
			
			if (x.getImpuestoTipo().getSigla().compareTo(ParamsLocal.SIGLA_IMPUESTO_IVA_EXENTO) == 0) {
				
				total0 += x.getMonto();
				
			}
			
		}
		
		this.pagoSelected.setTotal10(total10);
		this.pagoSelected.setTotal5(total5);
		this.pagoSelected.setTotalExento(total0);
		
	}
	
	@Command
	@NotifyChange("*")
	public void borrarDetalle(@BindingParam("dato") PagoDetalle dato) {
		
		this.pagoSelected.getDetalles().remove(dato);
		
	}


	@Command
	@NotifyChange("lPagos")
	public void guardar() {
		
		verificarCampos();
		
		if (this.pagoSelected.getDetalles().size() == 0) {
			
			this.mensajeInfo("Debes agregar detalles para guardar.");
			return;
			
		}
		
		this.save(pagoSelected);

		if (editar) {

			Notification.show("El Pago fue Actualizado.");
			this.editar = false;
		} else {

			procesarOrdenCompra();
			procesarCursosVigentes();
			Notification.show("El Pago fue agregado.");
		}
		
		this.pagoSelected = null;

		this.cargarPagos();

		this.modal.detach();

	}
	
	public void procesarCursosVigentes() {
		
		for(PagoDetalle pd : this.pagoSelected.getDetalles()) {
			
			if (pd.getDetallesDoc().size() > 0) {
				
				for (PagoDetalleDoc pdd : pd.getDetallesDoc()) {
					
					CursoVigenteMateria cvm = pdd.getCursoVigenteMateria();
					
					cvm.setEstado(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_ESTAOD_CV_MATERIA_FINALIZADO));
					
					this.save(cvm);
					
				}
				
			}
		}
		
	}
	
	public void procesarOrdenCompra() {
		
		if (this.pagoSelected.getOrdenCompra() != null) {
			
			this.pagoSelected.getOrdenCompra().setEstadoTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_ORDEN_COMPRA_PAGADO));
			
			this.save(this.pagoSelected.getOrdenCompra());
			
		}
		
	}

	// fin modal

	@Command
	public void borrarPagoConfirmacion(@BindingParam("pagoid") final long pagoid) {

		if (!this.opBorrarPago)
			return;

		Pago s = this.reg.getObjectById(Pago.class.getName(), pagoid);

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarPago(s);

				}

			}

		};

		this.mensajeEliminar("El Pago sera eliminada. \n Continuar?", event);
	}

	private void borrarPago(Pago pago) {

		this.reg.deleteObject(pago);

		this.cargarPagos();

		BindUtils.postNotifyChange(null, null, this, "lPagos");

	}

	private FinderModel cuentaFinder;
	private FinderModel monedaFinder;
	//private FinderModel estadoTipoFinder;
	private FinderModel proveedorFinder;
	private FinderModel comprobanteTipoFinder;
	private FinderModel condicionTipoFinder;
	
	private FinderModel pagoRelacionadoFinder;
	private FinderModel ordenCompraFinder;
	
	private FinderModel impuestoTipoFinder;
	
	private FinderModel comprobanteTipoFiltroFinder;

	private FinderModeloModel<CursoVigenteMateria> cursoVigenteMateriaFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		String sqlComprobanteTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_COMPROBANTE);
		comprobanteTipoFinder = new FinderModel("Comprobante", sqlComprobanteTipo);
		comprobanteTipoFiltroFinder = new FinderModel("ComprobanteFiltro", sqlComprobanteTipo);

		String sqlCuenta = this.um.getSql("cuentaRubro/buscarCuentaRubro.sql");
		cuentaFinder = new FinderModel("Cuenta", sqlCuenta);

		String sqlMoneda = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_MONEDA);
		monedaFinder = new FinderModel("Moneda", sqlMoneda);

	/*	String sqlEstado = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_ORDEN_COMPRA);
		estadoTipoFinder = new FinderModel("PagoEstado", sqlEstado);*/

		String sqlProveedor = this.um.getSql("proveedor/buscarProveedorSede.sql").replace("?1",
				this.getCurrentSede().getSedeid() + "");
		proveedorFinder = new FinderModel("Proveedor", sqlProveedor);
		
		String sqlCondicionTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_CONDICION_VENTA);
		condicionTipoFinder = new FinderModel("Condicion", sqlCondicionTipo);
		
		String sqlPagoRelacionado = this.um.getSql("pago/buscarPagosCredito.sql");
		pagoRelacionadoFinder = new FinderModel("PagoRelacionado", sqlPagoRelacionado);
		
		Tipo ordeCompraActivo = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_ORDEN_COMPRA_ACTIVO);
		
		String sqlImpuestoTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				ParamsLocal.SIGLA_IMPUESTO);
		impuestoTipoFinder = new FinderModel("Impuesto", sqlImpuestoTipo);
		
		String sqlOrdenCompra = this.um.getSql("ordenCompra/buscarOrdenCompraEstado.sql").replace("?1", ordeCompraActivo.getTipoid()+"" );
		ordenCompraFinder = new FinderModel("OrdenCompra", sqlOrdenCompra);

	}

	public void generarFinders(@BindingParam("finder") String finder) {
		
		if (finder.compareTo(this.comprobanteTipoFiltroFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFiltroFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFiltroFinder, "listFinder");

			return;

		}
		

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

		/*if (finder.compareTo(this.estadoTipoFinder.getNameFinder()) == 0) {

			this.estadoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.estadoTipoFinder, "listFinder");

			return;

		}*/

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.condicionTipoFinder.getNameFinder()) == 0) {

			this.condicionTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.condicionTipoFinder, "listFinder");

			return;

		}
		

		if (finder.compareTo(this.pagoRelacionadoFinder.getNameFinder()) == 0) {

			this.pagoRelacionadoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.pagoRelacionadoFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.ordenCompraFinder.getNameFinder()) == 0) {

			this.ordenCompraFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.ordenCompraFinder, "listFinder");

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
		
		if (finder.compareTo(this.comprobanteTipoFiltroFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFiltroFinder
					.setListFinder(this.filtrarListaObject(filter, this.comprobanteTipoFiltroFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFiltroFinder, "listFinder");

			return;

		}

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

		/*if (finder.compareTo(this.estadoTipoFinder.getNameFinder()) == 0) {

			this.estadoTipoFinder
					.setListFinder(this.filtrarListaObject(filter, this.estadoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.estadoTipoFinder, "listFinder");

			return;

		}*/

		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.proveedorFinder
					.setListFinder(this.filtrarListaObject(filter, this.proveedorFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.proveedorFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder
					.setListFinder(this.filtrarListaObject(filter, this.comprobanteTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;

		}
		
		

		if (finder.compareTo(this.condicionTipoFinder.getNameFinder()) == 0) {

			this.condicionTipoFinder
					.setListFinder(this.filtrarListaObject(filter, this.condicionTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.condicionTipoFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.pagoRelacionadoFinder.getNameFinder()) == 0) {

			this.pagoRelacionadoFinder
					.setListFinder(this.filtrarListaObject(filter, this.pagoRelacionadoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.pagoRelacionadoFinder, "listFinder");

			return;

		}
		
		if (finder.compareTo(this.ordenCompraFinder.getNameFinder()) == 0) {

			this.ordenCompraFinder
					.setListFinder(this.filtrarListaObject(filter, this.ordenCompraFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.ordenCompraFinder, "listFinder");

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
		
		if (finder.compareTo(this.comprobanteTipoFiltroFinder.getNameFinder()) == 0) {
			
			this.comprobanteTipoFiltro = this.reg.getObjectById(Tipo.class.getName(), id);
			

			return;
		}
		

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			/*
			 * this.pagoDetalleSelected.setPago(this.pagoSelected);
			 * this.pagoDetalleSelected.setCuentaRubro(this.reg.getObjectById(
			 * CuentaRubro.class.getName(), id));
			 */

			this.cuentaRubroSelected = this.reg.getObjectById(CuentaRubro.class.getName(), id);

			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}

		if (finder.compareTo(this.monedaFinder.getNameFinder()) == 0) {

			this.pagoSelected.setMonedaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}

		/*if (finder.compareTo(this.estadoTipoFinder.getNameFinder()) == 0) {

			this.pagoSelected.setEstadoTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}*/

		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.pagoSelected.setProveedor(this.reg.getObjectById(Proveedor.class.getName(), id));

			if (this.pagoSelected.getProveedor().getProveedorTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE) == 0) {

				this.verDetalleDoc = true;

			} else {

				this.verDetalleDoc = false;

			}

			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			this.cursoVigenteMateriaFinder = new FinderModeloModel<CursoVigenteMateria>("CursoVigenteMateria", this.reg,
					CursoVigenteMateria.class.getName(), "proveedorid = "
							+ this.pagoSelected.getProveedor().getProveedorid() + " and estadoid != 14");

			return;
		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {
	
			this.pagoSelected.setComprobanteTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");
			
			if (this.pagoSelected.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
				
				pagoSelected.setCondicionVentaTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO));
				this.camposCabecera[4]=false;
				
			}else if(this.pagoSelected.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) == 0) {
				
				pagoSelected.setCondicionVentaTipo(null);
				this.camposCabecera[4]=true;
				
			}

			return;
		}
		
		if (finder.compareTo(this.condicionTipoFinder.getNameFinder()) == 0) {
			
			this.pagoSelected.setCondicionVentaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");

			return;
		}
		
		if (finder.compareTo(this.pagoRelacionadoFinder.getNameFinder()) == 0) {
			
			this.pagoSelected.setPagoRelacionado(this.reg.getObjectById(Pago.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");
			this.camposCabecera[1] = true;
			this.camposCabecera[2] = true;
			this.camposCabecera[3] = true;
			this.camposCabecera[4] = true;
			this.pagoSelected.setComprobanteTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_RECIBO));
			this.pagoSelected.setProveedor(this.pagoSelected.getPagoRelacionado().getProveedor());
			return;
		}
		
		if (finder.compareTo(this.ordenCompraFinder.getNameFinder()) == 0) {
			
			this.pagoSelected.setOrdenCompra(this.reg.getObjectById(OrdenCompra.class.getName(), id));
			// BindUtils.postNotifyChange(null, null, this, "rubroFinder");
			
			this.cargarDatosOrdenCompra();
			
			this.camposCabecera[0] = true;
			this.camposCabecera[2] = true;
			
			this.camposDetalle = true;
			
			return;
		}
		
		if (finder.compareTo(this.impuestoTipoFinder.getNameFinder()) == 0) {
			
			this.impuestoSelected = this.reg.getObjectById(Tipo.class.getName(), id);
			return;
		}

	}
	
	private void cargarDatosOrdenCompra() {
		
		OrdenCompra oc = this.pagoSelected.getOrdenCompra();
		
		this.pagoSelected.setProveedor(oc.getProveedor());
		this.pagoSelected.setMonedaTipo(oc.getMonedaTipo());
		this.pagoSelected.setDetalles(new ArrayList<PagoDetalle>());
		
		if(oc.getDescripcion() != null) {
			
			this.pagoSelected.setDescripcion(oc.getDescripcion());
			
		}
		
		for (OrdenCompraDetalle ocd : oc.getDetalles()) {
			
			PagoDetalle pd = new PagoDetalle();
			pd.setPago(this.pagoSelected);
			pd.setCuentaRubro(ocd.getCuentaRubro());
			pd.setMonto(ocd.getMonto());
			pd.setImpuestoTipo(ocd.getImpuestoTipo());
			
			if (ocd.getDetallesDoc().size() > 0) {
				
				for (OrdenCompraDetalleDoc ocdd : ocd.getDetallesDoc()) {
					
					PagoDetalleDoc pdd = new PagoDetalleDoc();
					pdd.setPagoDetalle(pd);
					pdd.setCursoVigenteMateria(ocdd.getCursoVigenteMateria());
					
					pd.getDetallesDoc().add(pdd);
					
				}
				
			}
			
			this.pagoSelected.getDetalles().add(pd);
			
		}
		
		if (this.pagoSelected.getProveedor().getProveedorTipo().getSigla().compareTo(ParamsLocal.SIGLA_PROVEEDOR_DOCENTE)==0) {
			
			this.verDetalleDoc = true;
			
		}
		
		this.calcularTotalesImpuesto();

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
	public void refrescarDetallesDoc(@BindingParam("dato") PagoDetalle dato) {

		this.pagoDetalleSelected = dato;

	}

	@Command
	@NotifyChange("*")
	public void agragarDocumento() {

		if (this.pagoSelected.getProveedor() == null) {

			this.mensajeError("No hay proveedor Cargado");
			return;

		}

		if (this.pagoDetalleSelected == null) {

			this.mensajeError("Debes seleccionar un detalle para agregar.");
			return;

		}

		if (this.cursoVigenteMateriaSelected == null) {

			this.mensajeError("Debes seleccionar un documento.");
			return;

		}

		PagoDetalleDoc ocdd = new PagoDetalleDoc();
		ocdd.setPagoDetalle(this.pagoDetalleSelected);
		ocdd.setCursoVigenteMateria(this.cursoVigenteMateriaSelected);

		this.pagoDetalleSelected.getDetallesDoc().add(ocdd);

		this.cursoVigenteMateriaSelected = null;

	}

	public List<Object[]> getlPagos() {
		return lPagos;
	}

	public void setlPagos(List<Object[]> lPagos) {
		this.lPagos = lPagos;
	}

	public Pago getPagoSelected() {
		return pagoSelected;
	}

	public void setPagoSelected(Pago pagoSelected) {
		this.pagoSelected = pagoSelected;
	}

	public boolean isOpCrearPago() {
		return opCrearPago;
	}

	public void setOpCrearPago(boolean opCrearPago) {
		this.opCrearPago = opCrearPago;
	}

	public boolean isOpEditarPago() {
		return opEditarPago;
	}

	public void setOpEditarPago(boolean opEditarPago) {
		this.opEditarPago = opEditarPago;
	}

	public boolean isOpBorrarPago() {
		return opBorrarPago;
	}

	public void setOpBorrarPago(boolean opBorrarPago) {
		this.opBorrarPago = opBorrarPago;
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

	public PagoDetalle getPagoDetalleSelected() {
		return pagoDetalleSelected;
	}

	public void setPagoDetalleSelected(PagoDetalle pagoDetalleSelected) {
		this.pagoDetalleSelected = pagoDetalleSelected;
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

	public FinderModel getComprobanteTipoFinder() {
		return comprobanteTipoFinder;
	}

	public void setComprobanteTipoFinder(FinderModel comprobanteTipoFinder) {
		this.comprobanteTipoFinder = comprobanteTipoFinder;
	}

	public FinderModel getCondicionTipoFinder() {
		return condicionTipoFinder;
	}

	public void setCondicionTipoFinder(FinderModel condicionTipoFinder) {
		this.condicionTipoFinder = condicionTipoFinder;
	}

	public FinderModel getPagoRelacionadoFinder() {
		return pagoRelacionadoFinder;
	}

	public void setPagoRelacionadoFinder(FinderModel pagoRelacionadoFinder) {
		this.pagoRelacionadoFinder = pagoRelacionadoFinder;
	}

	public FinderModel getOrdenCompraFinder() {
		return ordenCompraFinder;
	}

	public void setOrdenCompraFinder(FinderModel ordenCompraFinder) {
		this.ordenCompraFinder = ordenCompraFinder;
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

	public boolean[] getCamposCabecera() {
		return camposCabecera;
	}

	public void setCamposCabecera(boolean[] camposCabecera) {
		this.camposCabecera = camposCabecera;
	}

	public boolean isCamposDetalle() {
		return camposDetalle;
	}

	public void setCamposDetalle(boolean camposDetalle) {
		this.camposDetalle = camposDetalle;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}


	public double getTotalGral() {
		return totalGral;
	}


	public void setTotalGral(double totalGral) {
		this.totalGral = totalGral;
	}


	public FinderModel getComprobanteTipoFiltroFinder() {
		return comprobanteTipoFiltroFinder;
	}


	public void setComprobanteTipoFiltroFinder(FinderModel comprobanteTipoFiltroFinder) {
		this.comprobanteTipoFiltroFinder = comprobanteTipoFiltroFinder;
	}


	public Tipo getComprobanteTipoFiltro() {
		return comprobanteTipoFiltro;
	}


	public void setComprobanteTipoFiltro(Tipo comprobanteTipoFiltro) {
		this.comprobanteTipoFiltro = comprobanteTipoFiltro;
	}

	

}
