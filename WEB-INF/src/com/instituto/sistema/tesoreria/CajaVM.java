package com.instituto.sistema.tesoreria;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

import com.doxacore.components.finder.FinderInterface;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Tipotipo;
import com.doxacore.modelo.Usuario;
import com.doxacore.report.ReportExcel;
import com.instituto.modelo.Caja;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.Entidad;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CajaVM extends TemplateViewModelLocal implements FinderInterface {

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

		String sqlCajas = this.um.getSql("caja/listaCaja.sql").replace("?1", this.getCurrentSede().getSedeid() + "");

		this.lCajas = this.reg.sqlNativo(sqlCajas);
		this.lCajasOri = this.lCajas;

	}

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[6]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lCajas")
	public void filtrarCajas() {

		this.lCajas = filtrarListaObject(this.filtroColumns, this.lCajasOri);

	}

	private Window modal;
	private boolean editar = false;

	@Command
	public void modalCajaAgregar() {

		if (!this.opCrearCaja)
			return;

		this.editar = false;
		modalCaja(-1);

	}

	@Command
	public void modalCaja(@BindingParam("cajaid") long cajaid) {

		this.inicializarFinders();

		if (cajaid != -1) {

			if (!this.opEditarCaja)
				return;

			this.cajaSelected = this.reg.getObjectById(Caja.class.getName(), cajaid);
			this.editar = true;
			
			if (this.cajaSelected.getCierre() != null) {
				
				
				this.mensajeInfo("La caja ya esta cerrada, no se puede editar.");
				return;
			}
			
			
			List<Cobranza> lCobranzas = this.reg.getAllObjectsByCondicionOrder(Cobranza.class.getName(), "cajaid = "+this.cajaSelected.getCajaid(), null);
			
			if (lCobranzas != null && lCobranzas.size() > 0) {
			
				this.mensajeInfo("La caja ya posee cobranzas, no se puede editar.");
				return;
			}

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

		if (this.cajaSelected.getApertura() == null) {

			this.cajaSelected.setApertura(new Date());

		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		/*
		 * String sqlCaja = "select * from cajas \n" +
		 * "WHERE sedeid = "+this.getCurrentSede().getSedeid()+
		 * " and DATE(apertura) = '"+sdf.format(this.cajaSelected.getApertura())+
		 * "' and usuariocajaid = "+this.cajaSelected.getUsuarioCaja().getUsuarioid()
		 * +" ;";
		 * 
		 * List<Object[]> result = this.reg.sqlNativo(sqlCaja);
		 */

		/*
		 * List<Caja> cajas1 =
		 * this.reg.getAllObjectsByCondicionOrder(Caja.class.getName(),
		 * "sedeid = "+this.getCurrentSede().getSedeid()+
		 * " and DATE(apertura) = '"+sdf.format(this.cajaSelected.getApertura())+
		 * "' and usuariocajaid = "+this.cajaSelected.getUsuarioCaja().getUsuarioid(),
		 * null);
		 * 
		 * 
		 * if (cajas1.size() > 0) {
		 * 
		 * this.
		 * mensajeError("Ya existe una caja con este usuario para esta fecha en esta sede."
		 * ); return; }
		 */

		if (!editar) {
			
			List<Caja> cajas2 = this.reg.getAllObjectsByCondicionOrder(
					Caja.class.getName(), "sedeid = " + this.getCurrentSede().getSedeid() + "\n"
							+ "and usuariocajaid = " + this.cajaSelected.getUsuarioCaja().getUsuarioid() + "\n",
					"cajaid desc");

			if (cajas2.size() > 0) {

				for (Caja x : cajas2) {

					if (x.getCierre() == null) {

						this.mensajeError("Este Usuario tiene Cajas no cerradas.");
						return;

					}

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
		} else {

			Notification.show("La Caja fue agregada.");
		}

	}

	@Command
	public void modalCerrar(@BindingParam("cajaid") long cajaid) {

		this.cajaSelected = this.reg.getObjectById(Caja.class.getName(), cajaid);

		if (this.cajaSelected.getCierre() != null) {

			this.mensajeInfo("La caja ya esta cerrada.");
			return;

		}

		modal = (Window) Executions.createComponents("/instituto/zul/tesoreria/cajaCerrarModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}

	@Command
	@NotifyChange("*")
	public void cerrarCaja() {

		if (!this.opCrearCaja) {

			this.mensajeInfo("No tienes privilegios para cerrar la caja");
			return;
		}

		if (this.cajaSelected.getCierre() != null) {

			this.mensajeInfo("La caja ya esta cerrada.");
			return;

		}

		this.cajaSelected.setCierre(new Date());
		this.cajaSelected.setUsuarioCierre(this.getCurrentUser());

		this.save(this.cajaSelected);

		this.cargarCajas();

		this.modal.detach();

	}

	// Seccion Finder

	private FinderModel usuarioFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlUsuario = "Select usuarioid, account as usuario from usuarios\n" + "order by usuarioid asc";

		// String sqlContribuyente =
		// this.um.getSql("contribuyente/listaContribuyentes.sql");

		/*
		 * if (!this.isUserRolMaster()) {
		 * 
		 * sqlContribuyente = sqlContribuyente.replace("--", "").replace("?1",
		 * this.getCurrentUser().getUsuarioid() + "");
		 * 
		 * }
		 */

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

			this.usuarioFinder.setListFinder(this.filtrarListaObject(filter, this.usuarioFinder.getListFinderOri()));
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

	@Command
	@NotifyChange("*")
	public void generarReporteCaja(@BindingParam("cajaid") long id) {

		DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("es", "ES"));
		DecimalFormat df = new DecimalFormat("#,##0.##", dfs);

		Caja caja = this.reg.getObjectById(Caja.class.getName(), id);

		/*
		 * if(caja.getCierre() == null) {
		 * 
		 * this.
		 * mensajeError("El reporte de caja solo puede ser generado cuando la caja esta cerrada."
		 * ); return;
		 * 
		 * }
		 */

		ReportExcel re = new ReportExcel("Caja_" + caja.getCajaid());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		List<String[]> titulos = new ArrayList<String[]>();

		String[] t1 = { "INSTITUTO SANTO TOMAS" };
		String[] t2 = { "Resolucion M.E.C. Nº 841/98" };
		String[] t3 = { "SEDE:", this.getCurrentSede().getSede() };

		String[] t4 = { "REPORTE DE CAJA" };

		String[] t5 = { "USUARIO CAJA: ", caja.getUsuarioCaja().getAccount() };
		String[] t6 = { "CAJA ID:", caja.getCajaid() + "" };
		String[] t7 = { "APERTURA:", sdf.format(caja.getApertura()) };
		String[] t8 = { "APERTURA MONTO:", df.format(caja.getMontoApertura()) };

		String[] espacioBlanco = { "" };

		titulos.add(t1);
		titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		titulos.add(t5);
		titulos.add(t6);
		titulos.add(t7);
		titulos.add(t8);

		if (caja.getCierre() != null) {

			String[] t9 = { "CIERRE:", sdf.format(caja.getCierre()) };
			String[] t10 = { "CIERRE MONTO:", df.format(caja.getMontoCierre()) };
			titulos.add(t9);
			titulos.add(t10);

		}

		titulos.add(espacioBlanco);

		List<String[]> headersDatos = new ArrayList<String[]>();

		String[] hd1 = { "Tipo Comprobante", "Tipo Pago", "Comprobante #", "Fecha", "Razon social", "Ruc", "Alumno",
				"Importe", "Estado" };

		headersDatos.add(hd1);

		List<Object[]> datos = new ArrayList<>();

		String sql = this.um.getSql("caja/reporteCaja.sql").replace("?1", caja.getCajaid() + "");

		List<Object[]> result = this.reg.sqlNativo(sql);

		if (result.size() == 0) {

			this.mensajeInfo("La caja no tiene ningun movimiento");
			return;

		}

		long tipoComprobante = Long.parseLong(result.get(0)[2].toString());
		long formaPago = Long.parseLong(result.get(0)[9].toString());

		String tipoComprobanteName = result.get(0)[7].toString();
		String[] tipoComprobanteNameV = { result.get(0)[7].toString() };
		datos.add(tipoComprobanteNameV);

		String formaPagoName = result.get(0)[10].toString();

		String[] formaPagoV = { "", result.get(0)[10].toString() };
		datos.add(formaPagoV);

		double totalTP = 0;
		double totalC = 0;
		double totalGral = 0;

		for (Object[] x : result) {

			if (tipoComprobante != (long) x[2]) {

				Object[] totalTPDat = { "", "TOTAL " + formaPagoName, "", "", "", "", "", df.format(totalTP) };
				datos.add(totalTPDat);

				datos.add(espacioBlanco);

				Object[] totalCDat = { "TOTAL " + tipoComprobanteName, "", "", "", "", "", "", df.format(totalC) };
				datos.add(totalCDat);

				totalTP = 0;
				totalC = 0;

				tipoComprobante = Long.parseLong(x[2].toString());

				datos.add(espacioBlanco);

				formaPago = Long.parseLong(result.get(0)[9].toString());

				tipoComprobanteName = x[7].toString();

				String[] tituloComprobanteName = { x[7].toString() };
				datos.add(tituloComprobanteName);

				formaPagoName = x[10].toString();

				String[] tituloFormaPagoName = { "", x[10].toString() };
				datos.add(tituloFormaPagoName);

			}

			if (formaPago != (long) x[9]) {

				Object[] totalTPDat = { "", "TOTAL " + formaPagoName, "", "", "", "", "", df.format(totalTP) };
				datos.add(totalTPDat);
				totalTP = 0;

				formaPago = Long.parseLong(x[9].toString());
				datos.add(espacioBlanco);

				formaPagoName = x[10].toString();
				String[] tituloFormaPagoName = { "", x[10].toString() };
				datos.add(tituloFormaPagoName);

			}

			Object[] dat = new Object[9];

			dat[0] = "";
			dat[1] = "";

			dat[2] = x[3];
			dat[3] = x[1];
			dat[4] = x[4];
			dat[5] = x[5];
			dat[6] = x[6];
			dat[7] = df.format(x[8]);

			if (Boolean.parseBoolean(x[11].toString()) == true) {

				dat[8] = "Anulado";

			} else {

				dat[8] = "";

			}

			totalTP += Double.parseDouble(x[8].toString());
			totalC += Double.parseDouble(x[8].toString());
			totalGral += Double.parseDouble(x[8].toString());
			datos.add(dat);
		}

		Object[] totalTPDat = { "", "TOTAL " + formaPagoName, "", "", "", "", "", df.format(totalTP) };
		datos.add(totalTPDat);

		datos.add(espacioBlanco);

		Object[] totalCDat = { "TOTAL " + tipoComprobanteName, "", "", "", "", "", "", df.format(totalC) };
		datos.add(totalCDat);

		titulos.add(espacioBlanco);

		Object[] totalGraldat = { "TOTAL GENERAL ", "", "", "", "", "", "", df.format(totalGral) };
		datos.add(totalGraldat);
		
	
		//seccion credito
		
		String sqlCredito = this.um.getSql("caja/reporteCajaCredito.sql").replace("?1", caja.getCajaid() + "");
		
		System.out.println(sqlCredito);
		List<Object[]> resultCredito = this.reg.sqlNativo(sqlCredito);
		
		if (resultCredito != null && resultCredito.size() > 0) {
			
			datos.add(espacioBlanco);
			datos.add(espacioBlanco);
			
			double totalCredito = 0;
			
			Object[] headerCredito =  {"Tipo Comprobante", "Comprobante #", "Fecha", "Razon social", "Ruc", "Importe", "Estado" };
			datos.add(headerCredito);
			Object[] head2 = {"Factura Credito"};
			datos.add(head2);
			
			for (Object[] x : resultCredito) {
				
				
				Object[] dat = new Object[7];

				dat[0] = "";
				dat[1] = x[3];

				dat[2] = x[2];
				dat[3] = x[4];
				dat[4] = x[5];
				dat[5] = df.format(x[8]);
				dat[6] = x[9];
				
				datos.add(dat);
				
				totalCredito += Double.parseDouble(x[8].toString());
			}
			
			Object[] totalGraldatCredito = { "TOTAL CREDITO ", "", "", "", "",df.format(totalCredito) };
			
			datos.add(totalGraldatCredito);
			
			
		}		

		re.descargar(titulos, headersDatos, datos);

	}
	
	@Command
	public void borrarCajaConfirmacion(@BindingParam("cajaid") long cajaid ) {
		
		if (!this.opBorrarCaja)
			return;
		
		Caja cajaBorrar = reg.getObjectById(Caja.class.getName(), cajaid);
		
		if (cajaBorrar.getCierre() != null) {
			
			
			this.mensajeInfo("La caja ya esta cerrada, no se puede borrar.");
			return;
		}
		
		List<Cobranza> lCobranzas = this.reg.getAllObjectsByCondicionOrder(Cobranza.class.getName(), "cajaid = "+cajaBorrar.getCajaid(), null);
		
		if (lCobranzas != null && lCobranzas.size() > 0) {
		
			this.mensajeInfo("La caja ya posee cobranzas, no se puede borrar.");
			return;
		}
		
		EventListener event = new EventListener () {

			@Override
			public void onEvent(Event evt) throws Exception {
				
				if (evt.getName().equals(Messagebox.ON_YES)) {
					
					
					borrarCaja(cajaBorrar);
					
				}
				
			}

		};
		
		this.mensajeEliminar("La Entidad sera eliminada. \n Continuar?", event);
	}
	
	private void borrarCaja (Caja caja) {
		
		this.reg.deleteObject(caja);
		
		this.cargarCajas();
		
		BindUtils.postNotifyChange(null,null,this,"lCajas");
		
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
