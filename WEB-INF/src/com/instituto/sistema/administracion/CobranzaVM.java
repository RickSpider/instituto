package com.instituto.sistema.administracion;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.instituto.fe.util.MetodosCE;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.Caja;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.CobranzaDetalleCobro;
import com.instituto.modelo.Comprobante;
import com.instituto.modelo.Cotizacion;
import com.instituto.modelo.Cuenta;
import com.instituto.modelo.Entidad;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.modelo.PersonaEntidad;
import com.instituto.modelo.SifenDocumento;
import com.instituto.modelo.UsuarioSede;
import com.instituto.sistema.reporte.ReporteKudePDF;
import com.instituto.util.EmailServiceModoboa;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;
import com.instituto.util.UtilLocalMetodo;

import net.sf.jasperreports.engine.JRException;

public class CobranzaVM extends TemplateViewModelLocal {

	private Cobranza cobranzaSelected;
	private Caja cajaSelected;
	private boolean disableCobranza = false;
	private boolean disableCobranzaTipoPago = false;
	private List<CobranzaDetalleCobro> lDetallesCobros = new ArrayList<CobranzaDetalleCobro>();
	private List<CobranzaDetalle> lDetalles = new ArrayList<CobranzaDetalle>();
	private double saldoTotal = 0;
	private double saldoVencido = 0;
	private double iva10 = 0;
	private double iva5 = 0;
	private double exento = 0;
	private boolean condicionHabilitada = true;

	private boolean opCrearCobranza;
	private boolean opEditarCobranza;
	private boolean opDefinirFecha;
	private boolean opBorrarCobranza;
	private boolean opAnularCobranza;

	@Init(superclass = true)
	public void initCobranzaVM() {

		cobranzaSelected = new Cobranza();
		defaultCobranza();
		
		inicializarFinders();

	}

	@AfterCompose(superclass = true)
	public void afterComposeCobranzaVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearCobranza = this.operacionHabilitada(ParamsLocal.OP_CREAR_COBRANZA);
		this.opEditarCobranza = this.operacionHabilitada(ParamsLocal.OP_EDITAR_COBRANZA);
		this.opBorrarCobranza = this.operacionHabilitada(ParamsLocal.OP_BORRAR_COBRANZA);
		this.opAnularCobranza = this.operacionHabilitada(ParamsLocal.OP_ANULAR_COBRANZA);
		this.opDefinirFecha = this.operacionHabilitada(ParamsLocal.OP_DEFINIR_FECHA_COBRANZA);

	}

	private void verificarCaja() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		cajaSelected = this.reg.getObjectByCondicion(Caja.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid() + "\n" + "and usuariocajaid = "
						+ this.getCurrentUser().getUsuarioid() + "\n" + "and Date(apertura) = '"
						+ sdf.format(new Date()) + "'\n" + "and cierre is null");

	}

	@Command
	@NotifyChange("*")
	public void verificarFechaCaja() {

		if (!this.opDefinirFecha) {

			this.mensajeError("No tienes privilegios para hacer el cambio de fecha.");

			return;

		}
		
		if (this.cobranzaSelected.getFecha() == null) {
			
			return;
			
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		cajaSelected = this.reg.getObjectByCondicion(Caja.class.getName(),
				"sedeid = " + this.getCurrentSede().getSedeid() + "\n" + "and usuariocajaid = "
						+ this.getCurrentUser().getUsuarioid() + "\n" + "and Date(apertura) = '"
						+ sdf.format(this.cobranzaSelected.getFecha()) + "'\n" + "and cierre is null");

		if (this.cajaSelected == null) {

			this.mensajeInfo("No hay caja habilitada para la fecha");

		} else {

			this.disableCobranza = false;
			this.disableCobranzaTipoPago = false;

		}

	}

	@Command
	@NotifyChange("*")
	public void limpiar() {

		cobranzaSelected = new Cobranza();

		this.lDetallesCobros = new ArrayList<CobranzaDetalleCobro>();
		this.lDetalles = new ArrayList<CobranzaDetalle>();
		lTiposBuscar = new ArrayList<Object[]>();
		saldoTotal = 0;
		saldoVencido = 0;
		condicionHabilitada = true;
	/*	this.buscarAlumno = "";
		this.buscarComprobante = "";
		this.buscarCondicionVenta = "";*/

		defaultCobranza();

		double totalDetalle = 0;
		this.iva10 = 0;
		this.iva5 = 0;
		this.exento = 0;

	}

	private void defaultCobranza() {

		if (!this.opCrearCobranza) {

			this.disableCobranza = true;
			this.disableCobranzaTipoPago = true;

		} else {

			this.disableCobranza = false;
			this.disableCobranzaTipoPago = false;
		}

		this.verificarCaja();

		if (this.cajaSelected == null) {

			this.disableCobranza = true;
			this.disableCobranzaTipoPago = true;

		}

		Tipo comprobanteTipo = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_FACTURA);
		this.cobranzaSelected.setComprobanteTipo(comprobanteTipo);
		//this.buscarComprobante = comprobanteTipo.getTipo();

		Tipo condicionVenta = this.reg.getObjectBySigla(Tipo.class.getName(),
				ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO);
		this.cobranzaSelected.setCondicionVentaTipo(condicionVenta);
		//this.buscarCondicionVenta = condicionVenta.getTipo();

	}	

	

	// Seccion modal detalles

	private Window modal;
	private List<EstadoCuenta> lEstadosCuentas = new ArrayList<EstadoCuenta>();
	private List<EstadoCuenta> lEstadosCuentasAux = new ArrayList<EstadoCuenta>();

	@Command
	public void modalCobranzaDetalle() {

		if (this.cobranzaSelected.getAlumno() == null) {
			return;
		}

		String condicion2 = "";

		if (this.lDetalles.size() > 0) {

			StringBuffer ids = new StringBuffer();

			for (int i = 0; i < lDetalles.size(); i++) {

				if (i != 0) {

					ids.append(", ");

				}

				ids.append(this.lDetalles.get(i).getEstadoCuenta().getEstadocuentaid());

			}

			condicion2 = "AND estadocuentaid not in (" + ids + ") ";

		}

		this.lEstadosCuentas = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(),
				"alumnoid = " + this.cobranzaSelected.getAlumno().getAlumnoid()
						+ "AND monto > (pago+montodescuento) AND inactivo = FALSE " + condicion2,
				"cursovigenteid asc, vencimiento asc, conceptoid asc ");

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cobranzaDetalleModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	@Command
	@NotifyChange({ "totalDetalle", "iva10", "iva5", "exento", "lDetalles", "totalesDiferencia" })
	public void borrarDetalle(@BindingParam("dato") CobranzaDetalle dato) {

		if (this.disableCobranza) {

			return;

		}

		this.lDetalles.remove(dato);

		this.getTotalDetalle();

	}

	@Command
	public void onSelectEstadocuenta(@BindingParam("dato") EstadoCuenta dato) {

		this.lEstadosCuentasAux.add(dato);

	}

	@Command
	@NotifyChange({ "lDetalles", "totalDetalle", "totalesDiferencia", "iva10", "iva5", "exento", "cssDiferencia" })
	public void agregarCobranzaDetalle() {

		for (EstadoCuenta x : this.lEstadosCuentasAux) {

			CobranzaDetalle cobranzaDetalle = new CobranzaDetalle();
			cobranzaDetalle.setEstadoCuenta(x);
			cobranzaDetalle = buscarDescuento(cobranzaDetalle);
			cobranzaDetalle.setMonto(cobranzaDetalle.getSaldo() - cobranzaDetalle.getMontoDescuento());
			cobranzaDetalle.setDescripcion(x.getConcepto().getConcepto()+" "+x.getPeriodo()+" - "+x.getCursoVigente().getCurso().getDescripcion());

			/*
			 * if (x.getConcepto().getImpuestoTipo().getSigla().compareTo(ParamsLocal.
			 * SIGLA_IMPUESTO_IVA_EXENTO)==0) {
			 * 
			 * cobranzaDetalle.setExento(cobranzaDetalle.getSaldo()-cobranzaDetalle.
			 * getMontoDescuento());
			 * 
			 * }
			 * 
			 * if (x.getConcepto().getImpuestoTipo().getSigla().compareTo(ParamsLocal.
			 * SIGLA_IMPUESTO_IVA_10)==0) {
			 * 
			 * cobranzaDetalle.setIva10((cobranzaDetalle.getSaldo()-cobranzaDetalle.
			 * getMontoDescuento())/11);
			 * 
			 * }
			 * 
			 * if (x.getConcepto().getImpuestoTipo().getSigla().compareTo(ParamsLocal.
			 * SIGLA_IMPUESTO_IVA_5)==0) {
			 * 
			 * cobranzaDetalle.setIva5((cobranzaDetalle.getSaldo()-cobranzaDetalle.
			 * getMontoDescuento())/21);
			 * 
			 * }
			 */

			this.lDetalles.add(cobranzaDetalle);

		}

		this.lEstadosCuentasAux = new ArrayList<EstadoCuenta>();
		this.modal.detach();
	}

	private CobranzaDetalle buscarDescuento(CobranzaDetalle cobranzaDetalle) {

		if (cobranzaDetalle.getEstadoCuenta().getPago() == 0) {

			String sql = this.um.getSql("bucarDescuentoConvenio.sql")
					.replace("?1", cobranzaDetalle.getEstadoCuenta().getCursoVigente().getCursovigenteid() + "")
					.replace("?2", cobranzaDetalle.getEstadoCuenta().getConcepto().getConceptoid() + "")
					.replace("?3", cobranzaDetalle.getEstadoCuenta().getAlumno().getAlumnoid() + "");

			// System.out.println("SQL DESCUENTO ===================\n"+sql);

			List<Object[]> result = this.reg.sqlNativo(sql);

			if (result.size() > 0) {

				boolean porcentaje = Boolean.parseBoolean(result.get(0)[5].toString());
				double importe = Double.parseDouble(result.get(0)[4].toString());
				String[] periodos = result.get(0)[6].toString().split(",");

				for (int i = 0; i < periodos.length; i++) {

					int periodo = Integer.parseInt(periodos[i]);

					if (periodo == -1 || cobranzaDetalle.getEstadoCuenta().getPeriodo() == periodo) {

						if (porcentaje) {

							double porcentajeCalculo = importe / 100;

							double descuento = cobranzaDetalle.getEstadoCuenta().getMonto() * porcentajeCalculo;

							cobranzaDetalle.setMontoDescuento(descuento);

						} else {

							cobranzaDetalle.setMontoDescuento(importe);

						}
					}

				}

			}

		}

		return cobranzaDetalle;

	}
	
	@Command
	@NotifyChange("*")
	public void onChangePago(@BindingParam("detalle") CobranzaDetalle detalle ) {
		
		if (detalle.getSaldo() < detalle.getMonto()) {
			
			this.mensajeInfo("El Pago no puede ser mayor al saldo");
			detalle.setMonto(detalle.getSaldo());
			return;
		}
		
		this.getTotalDetalle();
		
	} 

	// Fin modal detalles

	// Seccion detalle Cobro

	private CobranzaDetalleCobro cobranzaDetalleCobroSelected;

	@Command
	public void modalCobranzaDetalleCobro() {

		if (this.cobranzaSelected.getAlumno() == null) {
			return;
		}

		if (this.lDetalles.size() == 0) {

			return;

		}

		//this.buscarEntidad = "";

		cobranzaDetalleCobroSelected = new CobranzaDetalleCobro();

		this.defaultCamposCobro();
		this.desabilitarCampos();

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cobranzaDetalleCobroModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	private void defaultCamposCobro() {

		Tipo efectivo = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_FORMA_PAGO_EFECTIVO);
		Tipo guarani = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_MONEDA_GUARANI);
		this.cobranzaDetalleCobroSelected.setFormaPago(efectivo);
		this.cobranzaDetalleCobroSelected.setMonedaTipo(guarani);

		this.buscarFormaPago = this.cobranzaDetalleCobroSelected.getFormaPago().getTipo();
		this.buscarMoneda = this.cobranzaDetalleCobroSelected.getMonedaTipo().getTipo();

	};

	private boolean verficarNumeroComprobante() {

		if (this.cobranzaDetalleCobroSelected.getComprobanteNum() != null) {

			List<CobranzaDetalleCobro> lcdc = this.reg.getAllObjectsByCondicionOrder(
					CobranzaDetalleCobro.class.getName(),
					"comprobanteNum = '" + this.cobranzaDetalleCobroSelected.getComprobanteNum() + "' and entidadid = "
							+ this.cobranzaDetalleCobroSelected.getEntidad().getEntidadid() + " and formapagoid = "
							+ this.cobranzaDetalleCobroSelected.getFormaPago().getTipoid(),
					null);

			if (lcdc != null && lcdc.size() > 0) {

				return true;

			}

		}

		return false;

	}

	private boolean verificarCampos() {
		
		
		if (this.cobranzaDetalleCobroSelected.getCuentaNumCR() == null) {
			
			this.mensajeInfo("Falta CuentaCR");
			return false;
			
		}
		

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TARJETA_CREDITO) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TARJETA_DEBITO) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_QR) == 0) {

			if (this.cobranzaDetalleCobroSelected.getEntidad() == null) {

				this.mensajeInfo("Falta Entidad");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getTransaccionNum() == null) {

				this.mensajeInfo("Falta Transaccion #");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getAutorizacionNum() == null) {

				this.mensajeInfo("Falta Autorizacion #");
				return false;
			}

			if (this.cobranzaDetalleCobroSelected.getTitular() == null) {

				this.mensajeInfo("Falta Titutular");
				return false;
			}

			if (this.cobranzaDetalleCobroSelected.getEmision() == null) {

				this.mensajeInfo("Falta Fecha Emision");
				return false;
			}

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_CHEQUE) == 0) {
			
			if (this.cobranzaDetalleCobroSelected.getEntidad() == null) {

				this.mensajeInfo("Falta Entidad");
				return false;

			}


			if (this.cobranzaDetalleCobroSelected.getChequeNum() == null) {

				this.mensajeInfo("Falta Cheque #");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getTitular() == null) {

				this.mensajeInfo("Falta Titutular");
				return false;
			}

			if (this.cobranzaDetalleCobroSelected.getEmision() == null) {

				this.mensajeInfo("Falta Fecha Emision");
				return false;
			}

			if (this.cobranzaDetalleCobroSelected.getVencimiento() == null) {

				this.mensajeInfo("Falta Fecha Vencimiento");
				return false;
			}

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_GIRO) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_BILLETERA) == 0) {

			if (this.cobranzaDetalleCobroSelected.getEntidad() == null) {

				this.mensajeInfo("Falta Entidad");
				return false;


			}

			if (this.cobranzaDetalleCobroSelected.getComprobanteNum() == null) {

				this.mensajeInfo("Falta falta Comprobante #");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getCuentaNum() == null) {

				this.mensajeInfo("Falta Cuenta DB #");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getTransaccionNum() == null) {

				this.mensajeInfo("Falta Transaccion #");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getEmision() == null) {

				this.mensajeInfo("Falta Fecha Emision");
				return false;

			}

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TRANSFERENCIA) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_BOCA_COBRANZA) == 0
				||this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
					.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DIFERIDO) == 0) {

			if (this.cobranzaDetalleCobroSelected.getEntidad() == null) {

				this.mensajeInfo("Falta Entidad");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getComprobanteNum() == null) {

				this.mensajeInfo("Falta falta Comprobante #");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getCuentaNum() == null) {

				this.mensajeInfo("Falta Cuenta DB #");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getTransaccionNum() == null) {

				this.mensajeInfo("Falta Transaccion #");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getTitular() == null) {

				this.mensajeInfo("Falta Titular");
				return false;

			}

			if (this.cobranzaDetalleCobroSelected.getEmision() == null) {

				this.mensajeInfo("Falta Fecha Emision");
				return false;

			}

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_BANCARIO) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_ATM) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_EXTERIOR) == 0) {

			if (this.cobranzaDetalleCobroSelected.getEntidad() == null) {

				this.mensajeInfo("Falta Entidad");
				return false;


			}

			if (this.cobranzaDetalleCobroSelected.getComprobanteNum() == null) {

				this.mensajeInfo("Falta falta Comprobante #");
				return false;


			}

			if (this.cobranzaDetalleCobroSelected.getTransaccionNum() == null) {

				this.mensajeInfo("Falta Transaccion #");
				return false;


			}

		
			if (this.cobranzaDetalleCobroSelected.getEmision() == null) {

				this.mensajeInfo("Falta Fecha Emision");
				return false;

			}

		}
		
		return true;

	}

	@Command
	@NotifyChange({ "lDetallesCobros", "totalDetalleCobro", "totalesDiferencia", "cssDiferencia" })
	public void agregarCobranzaDetalleCobro() {
		
		if (!verificarCampos()) {
			
			return;
			
		}
		

		if (this.verficarNumeroComprobante()) {

			this.mensajeInfo("El comprobante ya fue utilizado para pagar anteriormente.");

			return;

		}

		for (CobranzaDetalleCobro x : this.lDetallesCobros) {

			if (x.getFormaPago().getSigla().compareTo(cobranzaDetalleCobroSelected.getFormaPago().getSigla()) == 0) {

				this.mensajeInfo("Ya existe una Forma de Pago " + x.getFormaPago().getTipo());

				return;
			}

		}

		if (cobranzaDetalleCobroSelected.getMonto() <= 0) {

			this.mensajeInfo("El monto no puede ser 0 (cero).");
			return;
		}

		this.lDetallesCobros.add(cobranzaDetalleCobroSelected);

		this.modal.detach();

	}

	@Command
	@NotifyChange({ "lDetallesCobros", "lDetalles", "totalDetalle", "totalesDiferencia", "iva10", "iva5", "exento",
			"cssDiferencia" })
	public void borrarCobranzaDetalleCobro(@BindingParam("dato") CobranzaDetalleCobro dato) {

		if (this.disableCobranza) {

			return;

		}

		this.lDetallesCobros.remove(dato);
		this.getTotalDetalle();

	}

	private boolean[] camposCobroModal = new boolean[10];

	private void desabilitarCampos() {

		for (int i = 0; i < camposCobroModal.length; i++) {

			camposCobroModal[i] = true;

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_EFECTIVO) == 0) {

			for (int i = 0; i < camposCobroModal.length; i++) {

				camposCobroModal[i] = true;

			}

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TARJETA_CREDITO) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TARJETA_DEBITO) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_QR) == 0) {

			camposCobroModal[0] = false;
			camposCobroModal[2] = false;
			camposCobroModal[4] = false;
			camposCobroModal[5] = false;
			camposCobroModal[7] = false;
			camposCobroModal[9] = false;

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_CHEQUE) == 0) {

			camposCobroModal[0] = false;
			camposCobroModal[3] = false;
			camposCobroModal[4] = false;
			camposCobroModal[5] = false;
			camposCobroModal[6] = false;
			camposCobroModal[9] = false;

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_GIRO) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_BILLETERA) == 0) {

			camposCobroModal[0] = false;
			camposCobroModal[1] = false;
			camposCobroModal[2] = false;
			// camposCobroModal[4] = false;
			camposCobroModal[5] = false;
			camposCobroModal[8] = false;
			camposCobroModal[9] = false;

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TRANSFERENCIA) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_BOCA_COBRANZA) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DIFERIDO) == 0) {

			camposCobroModal[0] = false;
			camposCobroModal[1] = false;
			camposCobroModal[2] = false;
			camposCobroModal[4] = false;
			camposCobroModal[5] = false;
			camposCobroModal[8] = false;
			camposCobroModal[9] = false;

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_BANCARIO) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_ATM) == 0
				|| this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_EXTERIOR) == 0) {

			camposCobroModal[0] = false;
			camposCobroModal[2] = false;
			camposCobroModal[5] = false;
			camposCobroModal[8] = false;
			

		}

	}

	//seccion finder
	private FinderModel comprobanteFinder;
	private FinderModel condicionFinder;
	private FinderModel alumnoFinder;
	private FinderModel cuentaFinder;
	private FinderModel cuentaCRfinder;
	private FinderModel entidadFinder;
	
	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlBuscarAlumno = this.um.getSql("buscarAlumnoNotSede.sql");
		this.alumnoFinder = new FinderModel("Alumno",sqlBuscarAlumno);
		
		String sqlTipo = "Select t.tipoid as id, t.tipo as comprobante, t.descripcion as descripcion from tipos t\n"
						+ "join tipotipos tt on tt.tipotipoid = t.tipotipoid \n"
						+ "where tt.sigla like '?1' \n"
						+ "order by t.tipoid asc;";
		
		comprobanteFinder = new FinderModel("Comprobante", sqlTipo.replace("?1", ParamsLocal.SIGLA_COMPROBANTE));
		condicionFinder = new FinderModel("Condicion", sqlTipo.replace("?1", ParamsLocal.SIGLA_CONDICION_VENTA));
		
		String sqlCuentaCR = this.um.getSql("buscarCuenta.sql").replace("?1", this.getCurrentSede().getSedeid()+"");
		
		cuentaCRfinder = new FinderModel("CuentaCR", sqlCuentaCR);
		
		String sqlEntidad = this.um.getSql("buscarEntidad.sql");
		
		entidadFinder = new FinderModel("entidad",sqlEntidad);
		

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.alumnoFinder.getNameFinder())==0) {
			
			this.alumnoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.alumnoFinder, "listFinder");
			return;
		}
				
		if (finder.compareTo(this.comprobanteFinder.getNameFinder()) == 0) {

			this.comprobanteFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.comprobanteFinder, "listFinder");
			return;
		}
		
		if (finder.compareTo(this.condicionFinder.getNameFinder()) == 0) {

			this.condicionFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.condicionFinder, "listFinder");
			return;
		}
		
		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");
			return;
		}
		
		if (finder.compareTo(this.cuentaCRfinder.getNameFinder()) == 0) {

			this.cuentaCRfinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.cuentaCRfinder, "listFinder");
			return;
		}
		
		if (finder.compareTo(this.entidadFinder.getNameFinder()) == 0) {

			this.entidadFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.entidadFinder, "listFinder");
			return;
		}
		
		

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		
		if (finder.compareTo(this.alumnoFinder.getNameFinder()) == 0) {

			this.alumnoFinder.setListFinder(this.filtrarListaObject(filter, this.alumnoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.alumnoFinder, "listFinder");
			return;
		}
		
		if (finder.compareTo(this.comprobanteFinder.getNameFinder()) == 0) {

			this.comprobanteFinder.setListFinder(this.filtrarListaObject(filter, this.comprobanteFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.comprobanteFinder, "listFinder");
			return;
		}
		
		if (finder.compareTo(this.condicionFinder.getNameFinder()) == 0) {

			this.condicionFinder.setListFinder(this.filtrarListaObject(filter, this.condicionFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.condicionFinder, "listFinder");
			return;
		}
		

		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {

			this.cuentaFinder.setListFinder(this.filtrarListaObject(filter, this.cuentaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.cuentaFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.cuentaCRfinder.getNameFinder()) == 0) {

			this.cuentaCRfinder.setListFinder(this.filtrarListaObject(filter, this.cuentaCRfinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.cuentaCRfinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.entidadFinder.getNameFinder()) == 0) {

			this.entidadFinder.setListFinder(this.filtrarListaObject(filter, this.entidadFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.entidadFinder, "listFinder");

			return;
		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.alumnoFinder.getNameFinder()) == 0) {
			
			Alumno buscarSelectedAlumno = this.reg.getObjectById(Alumno.class.getName(), id);
		//	this.buscarAlumno = buscarSelectedAlumno.getFullNombre();
			this.cobranzaSelected.setAlumno(buscarSelectedAlumno);

			if (buscarSelectedAlumno.getPersonaFacturacion() == null) {

				if (buscarSelectedAlumno.getPersona().getRazonSocial() != null
						&& buscarSelectedAlumno.getPersona().getRazonSocial().length() > 0) {

					this.cobranzaSelected.setRazonSocial(buscarSelectedAlumno.getPersona().getRazonSocial());

				} else {

					this.cobranzaSelected.setRazonSocial(buscarSelectedAlumno.getPersona().getNombreCompleto());

				}

				if (buscarSelectedAlumno.getPersona().getRuc() != null
						&& buscarSelectedAlumno.getPersona().getRuc().length() > 0) {

					this.cobranzaSelected.setRuc(buscarSelectedAlumno.getPersona().getRuc());

				} else {

					this.cobranzaSelected.setRuc(buscarSelectedAlumno.getPersona().getDocumentoNum());
				}

				this.cobranzaSelected.setDireccion(buscarSelectedAlumno.getPersona().getDireccion());
				this.cobranzaSelected.setTelefono(buscarSelectedAlumno.getPersona().getTelefono());

			} else {

				if (buscarSelectedAlumno.getPersonaFacturacion().getRazonSocial() != null
						&& buscarSelectedAlumno.getPersonaFacturacion().getRazonSocial().length() > 0) {

					this.cobranzaSelected.setRazonSocial(buscarSelectedAlumno.getPersonaFacturacion().getRazonSocial());

				} else {

					this.cobranzaSelected.setRazonSocial(buscarSelectedAlumno.getPersonaFacturacion().getNombreCompleto());

				}

				if (buscarSelectedAlumno.getPersona().getRuc() != null
						&& buscarSelectedAlumno.getPersona().getRuc().length() > 0) {

					this.cobranzaSelected.setRuc(buscarSelectedAlumno.getPersonaFacturacion().getRuc());

				} else {

					this.cobranzaSelected.setRuc(buscarSelectedAlumno.getPersonaFacturacion().getDocumentoNum());
				}

				this.cobranzaSelected.setDireccion(buscarSelectedAlumno.getPersonaFacturacion().getDireccion());
				this.cobranzaSelected.setTelefono(buscarSelectedAlumno.getPersonaFacturacion().getTelefono());

			}

			String sqlSaldoTotal = this.um.getSql("saldoTotalPorAlumno.sql").replace("?1",
					buscarSelectedAlumno.getAlumnoid() + "");
			List<Object[]> resultSaldoTotal = this.reg.sqlNativo(sqlSaldoTotal);
			this.saldoTotal = 0;
			if (resultSaldoTotal.size() > 0) {
				this.saldoTotal = Double.parseDouble(resultSaldoTotal.get(0)[1].toString());
			}

			String sqlSaldoVencido = this.um.getSql("saldoVencidoPorAlumno.sql").replace("?1",
					buscarSelectedAlumno.getAlumnoid() + "");
			List<Object[]> resultSaldoVencido = this.reg.sqlNativo(sqlSaldoVencido);
			this.saldoVencido = 0;
			if (resultSaldoVencido.size() > 0) {
				this.saldoVencido = Double.parseDouble(resultSaldoVencido.get(0)[1].toString());
			}
			
			String cuentaDBSQL = this.um.getSql("buscarPersonaEntidad.sql").replace("?1", this.cobranzaSelected.getAlumno().getPersona().getPersonaid()+"");
			System.out.println(cuentaDBSQL);
			this.cuentaFinder = new FinderModel("CuentaDB", cuentaDBSQL);
			
			return;
		
		}
		
		if (finder.compareTo(this.comprobanteFinder.getNameFinder()) == 0) {

			this.cobranzaSelected.setComprobanteTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
			if (this.cobranzaSelected.getComprobanteTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {

				this.condicionHabilitada = true;
				this.cobranzaSelected.setCondicionVentaTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO));
				
			} else if (this.cobranzaSelected.getComprobanteTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) == 0) {

				this.condicionHabilitada = false;
				this.cobranzaSelected.setCondicionVentaTipo(null);
				
			}
			
			return;
		}
		
		if (finder.compareTo(this.condicionFinder.getNameFinder()) == 0) {

			this.cobranzaSelected.setCondicionVentaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
			if (this.cobranzaSelected.getCondicionVentaTipo().getSigla().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO) == 0) {
				
				this.disableCobranzaTipoPago = false;
				
			}else {
				
				this.disableCobranzaTipoPago = true;
				
			}
			
			
			
			return;
		}
		
		if (finder.compareTo(this.cuentaFinder.getNameFinder()) == 0) {
			
			PersonaEntidad personaEntidad = this.reg.getObjectByCondicion(PersonaEntidad.class.getName(),
					"personaid = " + this.cobranzaSelected.getAlumno().getPersona().getPersonaid()+
					" And entidadid = "+id);
			
		//	this.buscarEntidad = personaEntidad.getEntidad().getEntidad();
			this.cobranzaDetalleCobroSelected.setEntidad(personaEntidad.getEntidad());
			this.cobranzaDetalleCobroSelected.setCuentaNum(personaEntidad.getCuenta());
			this.cobranzaDetalleCobroSelected.setTitular(this.cobranzaSelected.getAlumno().getPersona().getNombreCompleto());			
			return;
		}
		
		if (finder.compareTo(this.cuentaCRfinder.getNameFinder()) == 0) {
			
			Cuenta cuenta = this.reg.getObjectById(Cuenta.class.getName(), id);
			this.cobranzaDetalleCobroSelected.setCuentaNumCR(cuenta.getNumero());
			
			return;
		}
		
		if (finder.compareTo(this.entidadFinder.getNameFinder()) == 0) {
			
			this.cobranzaDetalleCobroSelected.setEntidad(this.reg.getObjectById(Entidad.class.getName(), id));
			
			return;
		}

	}

	// fin seccion finder

	// buscarTipo

	private List<Object[]> lTiposbuscarOri;
	private List<Object[]> lTiposBuscar;

	private String filtroBuscarTipo = "";

	private String buscarMoneda = "";
	private String buscarFormaPago = "";
//	private String buscarComprobante = "";
//	private String buscarCondicionVenta = "";

	@Command
	@NotifyChange("lTiposBuscar")
	public void filtrarTipoBuscar() {

		this.lTiposBuscar = this.filtrarListaObject(filtroBuscarTipo, this.lTiposbuscarOri);

	}

	@Command
	@NotifyChange("lTiposBuscar")
	public void generarListaBuscarTipo(String sigla) {

		String sqlBuscarTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", sigla);

		this.lTiposBuscar = this.reg.sqlNativo(sqlBuscarTipo);
		this.lTiposbuscarOri = this.lTiposBuscar;
	}

	@Command
	@NotifyChange("lTiposBuscar")
	public void generarListaBuscarFormaPago() {

		generarListaBuscarTipo(ParamsLocal.SIGLA_FORMA_PAGO);

	}

	@Command
	@NotifyChange("lTiposBuscar")
	public void generarListaBuscarMoneda() {

		generarListaBuscarTipo(ParamsLocal.SIGLA_MONEDA);

	}

/*	@Command
	@NotifyChange("lTiposBuscar")
	public void generarListaBuscarComprobante() {

		generarListaBuscarTipo(ParamsLocal.SIGLA_COMPROBANTE);

	}

	@Command
	@NotifyChange("lTiposBuscar")
	public void generarListaBuscarCondicionVenta() {

		if (this.cobranzaSelected.getComprobanteTipo() == null) {

			this.mensajeInfo("Seleccione un Comprobante Primero");

			return;

		}

		generarListaBuscarTipo(ParamsLocal.SIGLA_CONDICION_VENTA);

	}*/

	@Command
	@NotifyChange("buscarMoneda")
	public void onSelectMoneda(@BindingParam("id") long id) {

		Tipo tipo = this.reg.getObjectById(Tipo.class.getName(), id);

		Cotizacion cotizacion = this.reg.getObjectByCondicion(Cotizacion.class.getName(),
				" monedaTipoid =  " + tipo.getTipoid() + " " + "AND fecha = current_date ");

		if (tipo.getSigla().compareTo(ParamsLocal.SIGLA_MONEDA_GUARANI) != 0 && cotizacion == null) {

			this.mensajeInfo("No existe cotizacion de la moneda para la fecha de hoy.");

			return;

		}

		this.cobranzaDetalleCobroSelected.setMonedaTipo(tipo);

		if (tipo.getSigla().compareTo(ParamsLocal.SIGLA_MONEDA_GUARANI) == 0) {

			this.cobranzaDetalleCobroSelected.setMonedaCambio(1);

		} else {

			this.cobranzaDetalleCobroSelected.setMonedaCambio(cotizacion.getCompra());

		}

		this.buscarMoneda = tipo.getTipo();
		this.filtroBuscarTipo = "";

	}

	@Command
	@NotifyChange({ "buscarFormaPago", "camposCobroModal" })
	public void onSelectFormaPago(@BindingParam("id") long id) {

		Tipo tipo = this.reg.getObjectById(Tipo.class.getName(), id);
		this.cobranzaDetalleCobroSelected.setFormaPago(tipo);
		this.buscarFormaPago = tipo.getTipo();
		this.filtroBuscarTipo = "";

		desabilitarCampos();
	}

	/*@Command
	@NotifyChange({ "buscarComprobante", "condicionHabilitada", "buscarCondicionVenta" })
	public void onSelectComprobante(@BindingParam("id") long id) {

		Tipo comprobante = this.reg.getObjectById(Tipo.class.getName(), id);
		this.cobranzaSelected.setComprobanteTipo(comprobante);
		this.buscarComprobante = comprobante.getTipo();
		this.filtroBuscarTipo = "";

		if (this.cobranzaSelected.getComprobanteTipo().getSigla()
				.compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {

			this.condicionHabilitada = true;
			Tipo condicion = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO);
			this.cobranzaSelected.setCondicionVentaTipo(condicion);
			this.buscarCondicionVenta = this.cobranzaSelected.getCondicionVentaTipo().getTipo();
		}

		if (this.cobranzaSelected.getComprobanteTipo().getSigla()
				.compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) == 0) {

			this.condicionHabilitada = false;
			this.cobranzaSelected.setCondicionVentaTipo(null);
			this.buscarCondicionVenta = "";

		}

	}

	@Command
	@NotifyChange("buscarCondicionVenta")
	public void onSelectCondicionVenta(@BindingParam("id") long id) {

		Tipo condicionVenta = this.reg.getObjectById(Tipo.class.getName(), id);
		this.cobranzaSelected.setCondicionVentaTipo(condicionVenta);
		this.buscarCondicionVenta = condicionVenta.getTipo();
		this.filtroBuscarTipo = "";

	}*/

	// fin buscarTipo

	@Command
	public void guardarCobranza() {

		if (this.disableCobranza) {

			return;

		}

		if (!this.opCrearCobranza) {

			this.mensajeInfo("No tienes permisos para gestionar una cobranza.");
			return;
		}

		if (this.cobranzaSelected.getComprobanteTipo() == null) {

			this.mensajeInfo("No hay Comprobante seleccionado.");
			return;

		}

		if (this.lDetalles.size() == 0) {

			this.mensajeInfo("No hay Detalles agregados.");
			return;

		}

		if (this.lDetallesCobros.size() == 0) {

			this.mensajeInfo("No hay Tipos de Pagos agregados.");
			return;

		}

		if (!this.existeComprobante()) {

			this.mensajeInfo("No hay comprobante vigente para esta operacion.");
			return;

		}

		if (this.getTotalDetalle() > this.getTotalDetalleCobro()) {

			this.mensajeInfo("El tipo de Pago total no se ajusta al monto total de los detalles.");
			return;

		}

		if (this.cobranzaSelected.getFecha() != null && this.opDefinirFecha == false) {
			this.mensajeInfo("No tienes permisos para agregar una fecha.");
			return;
		}

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					procesarCobranza();

				}

			}

		};

		String mensajeInfo = "";

		if (this.getTotalesDiferencia() > 0) {

			mensajeInfo = "Hay una diferencia de vuelto con monto: "
					+ new DecimalFormat("#,###").format(this.getTotalesDiferencia()) + "\n";

		}

		this.mensajeSiNo(mensajeInfo + "Se empezara a procesar el cobro. \n Continuar?", "Cobranza", event);

	}

	@Command
	@NotifyChange({ "totalDetalle", "iva10", "iva5", "exento" })
	public double getTotalDetalle() {

		double totalDetalle = 0;
		this.iva10 = 0;
		this.iva5 = 0;
		this.exento = 0;

		for (int i = 0; i < lDetalles.size(); i++) {

			totalDetalle += lDetalles.get(i).getMonto();

			if (lDetalles.get(i).getEstadoCuenta().getConcepto().getImpuestoTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_IMPUESTO_IVA_EXENTO) == 0) {

				lDetalles.get(i).setExento(lDetalles.get(i).getMonto());
				this.exento += lDetalles.get(i).getExento();

			}

			if (lDetalles.get(i).getEstadoCuenta().getConcepto().getImpuestoTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_IMPUESTO_IVA_10) == 0) {

				lDetalles.get(i).setIva10(lDetalles.get(i).getMonto());
				//this.iva10 = +lDetalles.get(i).getIva10();
				this.iva10 += lDetalles.get(i).getMonto();
			}

			if (lDetalles.get(i).getEstadoCuenta().getConcepto().getImpuestoTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_IMPUESTO_IVA_5) == 0) {

				lDetalles.get(i).setIva5(lDetalles.get(i).getMonto());
				//this.iva5 += lDetalles.get(i).getIva5();
				this.iva5 += lDetalles.get(i).getMonto();

			}

		}

		return totalDetalle;
	}

	public double getTotalDetalleCobro() {

		double totalDetalleCobro = 0;

		for (CobranzaDetalleCobro x : lDetallesCobros) {

			totalDetalleCobro += x.getMonto() * x.getMonedaCambio();

		}

		return totalDetalleCobro;

	}

	public double getTotalesDiferencia() {

		return getTotalDetalleCobro() - getTotalDetalle();

	}

	public String getCssDiferencia() {

		if (getTotalesDiferencia() > 0) {

			return "font-weight: bold; text-align:right; background-color:yellow; ";

		}

		if (getTotalesDiferencia() < 0) {

			return "font-weight: bold; text-align:right; background-color:red; ";

		}

		return "font-weight: bold; text-align:right; ";
	}

	private void procesarCobranza() throws KeyManagementException, NoSuchAlgorithmException, IOException, JRException {

		if (this.cajaSelected == null) {

			this.mensajeError("No existe caja habilitada para el usurio");

			return;
		}

		this.cobranzaSelected.setCaja(this.cajaSelected);

		Object[] comprobante = this.getNumeroComprobante();

		if (this.cobranzaSelected.getComprobanteTipo().getSigla()
				.compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {

			this.cobranzaSelected.setTimbrado((Long) comprobante[0]);

		}

		this.cobranzaSelected.setComprobanteEmision((Date) comprobante[1]);
		this.cobranzaSelected.setComprobanteVencimiento((Date) comprobante[2]);

		String comprobanteNum = comprobante[3].toString();
		this.cobranzaSelected.setComprobanteNum(comprobanteNum);

		this.cobranzaSelected.setTotalDetalle(this.getTotalDetalle());
		this.cobranzaSelected.setTotalDetalleCobro(this.getTotalDetalleCobro());

		this.cobranzaSelected.setExento(this.exento);
		this.cobranzaSelected.setIva10(this.iva10);
		this.cobranzaSelected.setIva5(this.iva5);

		if (this.cobranzaSelected.getFecha() == null) {

			this.cobranzaSelected.setFecha(new Date());

		}
		
		boolean comprobanteElectronico = (boolean) comprobante[4];
		
		if (comprobanteElectronico) {
			
			this.cobranzaSelected.setComprobanteElectronico(true);
			
		}

		this.cobranzaSelected = this.save(this.cobranzaSelected);

		for (CobranzaDetalle x : lDetalles) {

			x.setCobranza(this.cobranzaSelected);			
			
			this.save(x);
			
			if (this.cobranzaSelected.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) ==0 
					|| this.cobranzaSelected.getCondicionVentaTipo().getSigla().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO) == 0) {

				x.getEstadoCuenta().setPago(x.getEstadoCuenta().getPago() + x.getMonto());
				x.getEstadoCuenta().setMontoDescuento(x.getMontoDescuento());

			}

			this.save(x.getEstadoCuenta());

		}

		for (CobranzaDetalleCobro x : lDetallesCobros) {

			x.setCobranza(this.cobranzaSelected);
			this.save(x);

		}

		this.disableCobranza = true;
		this.disableCobranzaTipoPago = true;

		//BindUtils.postNotifyChange(null, null, this, "*");

		if (this.cobranzaSelected.isComprobanteElectronico()) {
			
			if (this.cobranzaSelected.getComprobanteTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
	
				//this.generarComprobanteElectronico();
	
				MetodosCE ctce = new MetodosCE();
	
				SifenDocumento sd = ctce.convertAndSend(this.cobranzaSelected.getCobranzaid(), this.getCurrentSede().getSedeid());
				
				if (sd != null) {
					
					this.generarKude();
					
				}else {
					
					this.mensajeError("Hubo un error al envio del documento al servidor, intente luego");
					
				}
				
			}
			
		}else {
		
			if (this.cobranzaSelected.getComprobanteTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) == 0) {
	
				this.generarRecibo();
	
			}
	
			if (this.cobranzaSelected.getComprobanteTipo().getSigla()
					.compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
	
				this.generarFactura();
	
			}
		}
		this.limpiar();

		BindUtils.postNotifyChange(null, null, this, "*");

	}

	private boolean existeComprobante() {

		UsuarioSede us = this.getCurrentUsuarioSede();

		Comprobante comprobante = this.reg.getObjectByCondicion(Comprobante.class.getName(),
				"activo = true " + "AND sedeid = " + us.getSede().getSedeid() + " " + "AND comprobantetipoid = "
						+ this.cobranzaSelected.getComprobanteTipo().getTipoid() + " " + "AND puntoExpdicion = '"
						+ us.getPuntoExpedicion() + "' " + "AND emision <= current_date "
						+ "AND vencimiento >= current_date " + "AND siguiente <= fin ");

		if (comprobante == null) {

			return false;
		}

		return true;

	}

	private synchronized Object[] getNumeroComprobante() {

		UsuarioSede us = this.getCurrentUsuarioSede();

		StringBuffer numero = new StringBuffer();
		Object[] out = new Object[5];

		/*
		 * Comprobante comprobante =
		 * this.reg.getObjectByCondicion(Comprobante.class.getName(), "activo = true " +
		 * "AND sedeid = " + this.getCurrentSede().getSedeid() + " " +
		 * "AND comprobantetipoid = " +
		 * this.cobranzaSelected.getComprobanteTipo().getTipoid() + " " +
		 * "AND emision <= current_date " + "AND vencimiento >= current_date " +
		 * "AND siguiente <= fin ");
		 */

		Comprobante comprobante = this.reg.getObjectByCondicion(Comprobante.class.getName(),
				"activo = true " + "AND sedeid = " + us.getSede().getSedeid() + " " + "AND comprobantetipoid = "
						+ this.cobranzaSelected.getComprobanteTipo().getTipoid() + " " + "AND puntoExpdicion = '"
						+ us.getPuntoExpedicion() + "' " + "AND emision <= current_date "
						+ "AND vencimiento >= current_date " + "AND siguiente <= fin ");

		numero.append(this.getCurrentSede().getEstablecimiento() + "-" + comprobante.getPuntoExpdicion() + "-");

		for (int i = 0; i < 7 - comprobante.getSiguiente().toString().length(); i++) {

			numero.append("0");

		}

		numero.append(comprobante.getSiguiente());

		if (comprobante.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
			out[0] = comprobante.getTimbrado();
		}

		out[1] = comprobante.getEmision();
		out[2] = comprobante.getVencimiento();
		out[3] = numero;
		
		//envia campo si es electronico
		out[4] = comprobante.isElectronico();

		comprobante.setSiguiente(comprobante.getSiguiente() + 1);

		this.save(comprobante);

		return out;
	}

	// ========================Seccion Reportes ============================

	public void generarRecibo() {

		Executions.getCurrent().sendRedirect(
				"/instituto/zul/administracion/reciboReporte.zul?id=" + this.cobranzaSelected.getCobranzaid(),
				"_blank");
		
	

	}

	public void generarFactura() {

		Executions.getCurrent().sendRedirect(
				"/instituto/zul/administracion/facturaReporte.zul?id=" + this.cobranzaSelected.getCobranzaid(),
				"_blank");

	}
	
	public void generarKude() throws KeyManagementException, NoSuchAlgorithmException, IOException, JRException {

		Executions.getCurrent().sendRedirect(
				"/instituto/zul/administracion/kudeReporte.zul?id=" + this.cobranzaSelected.getCobranzaid(),
				"_blank");
		
		System.out.println("Inicio de envio de correo");
		
		UtilLocalMetodo ulm = new UtilLocalMetodo();
		
		String email = ulm.getEmailCobranza(this.cobranzaSelected.getCobranzaid());
				
		
		if (email != null) {
			
			String host = getSistemaPropiedad("EMAIL_HOST").getValor();
			String user = getSistemaPropiedad("EMAIL_USER").getValor();
			String pass = getSistemaPropiedad("EMAIL_PASS").getValor();
			
			ReporteKudePDF rk = new ReporteKudePDF(this.cobranzaSelected.getCobranzaid(), "kude"+this.cobranzaSelected.getCobranzaid());
			
			System.out.println("Inicio de envio de correo");
			new Thread(() -> {
		        // Coloca aquí el código de tu tarea asíncrona
		        try {
		        	System.out.println("dentro de la tarea y tray asincrono");
		        	enviarEmailFE(email, host, user, pass, rk.getPDF());
		    				} catch (Exception e) {
					
					System.out.println(e.getCause());
				}
		    }).start();		
		}	

	}
	
	public void enviarEmailFE(String email, String host, String user, String pass, File pdf) throws KeyManagementException, NoSuchAlgorithmException, IOException, JRException {
		
		System.out.println("==================DENTRO DEL MEDOTO DE ENVIO================");
				
		//email="rrgi89@hotmail.com";
		System.out.println("ENVIANDO CORREO A: "+email);
		
		System.out.println("host: "+host);

		EmailServiceModoboa esm = new EmailServiceModoboa(host,user,pass);
		
		System.out.println("enviando mensaje");
		
		esm.sent(email, "Facturacion Electronica", "El archivo adjunto es una representacion grafica del Documento Electronico.", pdf);
		
		System.out.println("mensaje enviado");
		
	}

	// =====================================================================
	

	public Cobranza getCobranzaSelected() {
		return cobranzaSelected;
	}

	public void setCobranzaSelected(Cobranza cobranzaSelected) {
		this.cobranzaSelected = cobranzaSelected;
	}

	/*public List<Object[]> getlAlumnosBuscar() {
		return lAlumnosBuscar;
	}

	public void setlAlumnosBuscar(List<Object[]> lAlumnosBuscar) {
		this.lAlumnosBuscar = lAlumnosBuscar;
	}

	public String getBuscarAlumno() {
		return buscarAlumno;
	}

	public void setBuscarAlumno(String buscarAlumno) {
		this.buscarAlumno = buscarAlumno;
	}*/

	public List<CobranzaDetalleCobro> getlDetallesCobros() {
		return lDetallesCobros;
	}

	public void setlDetallesCobros(List<CobranzaDetalleCobro> lDetallesCobros) {
		this.lDetallesCobros = lDetallesCobros;
	}

	public List<CobranzaDetalle> getlDetalles() {
		return lDetalles;
	}

	public void setlDetalles(List<CobranzaDetalle> lDetalles) {
		this.lDetalles = lDetalles;
	}

	public List<EstadoCuenta> getlEstadosCuentas() {
		return lEstadosCuentas;
	}

	public void setlEstadosCuentas(List<EstadoCuenta> lEstadosCuentas) {
		this.lEstadosCuentas = lEstadosCuentas;
	}

	public List<EstadoCuenta> getlEstadosCuentasAux() {
		return lEstadosCuentasAux;
	}

	public void setlEstadosCuentasAux(List<EstadoCuenta> lEstadosCuentasAux) {
		this.lEstadosCuentasAux = lEstadosCuentasAux;
	}

	public CobranzaDetalleCobro getCobranzaDetalleCobroSelected() {
		return cobranzaDetalleCobroSelected;
	}

	public void setCobranzaDetalleCobroSelected(CobranzaDetalleCobro cobranzaDetalleCobroSelected) {
		this.cobranzaDetalleCobroSelected = cobranzaDetalleCobroSelected;
	}

	public List<Object[]> getlTiposBuscar() {
		return lTiposBuscar;
	}

	public void setlTiposBuscar(List<Object[]> lTiposBuscar) {
		this.lTiposBuscar = lTiposBuscar;
	}

	public String getFiltroBuscarTipo() {
		return filtroBuscarTipo;
	}

	public void setFiltroBuscarTipo(String filtroBuscarTipo) {
		this.filtroBuscarTipo = filtroBuscarTipo;
	}

	public String getBuscarMoneda() {
		return buscarMoneda;
	}

	public void setBuscarMoneda(String buscarMoneda) {
		this.buscarMoneda = buscarMoneda;
	}

	public String getBuscarFormaPago() {
		return buscarFormaPago;
	}

	public void setBuscarFormaPago(String buscarFormaPago) {
		this.buscarFormaPago = buscarFormaPago;
	}

/*	public List<Object[]> getlEntidadesBuscar() {
		return lEntidadesBuscar;
	}

	public void setlEntidadesBuscar(List<Object[]> lEntidadesBuscar) {
		this.lEntidadesBuscar = lEntidadesBuscar;
	}

	public String getBuscarEntidad() {
		return buscarEntidad;
	}

	public void setBuscarEntidad(String buscarEntidad) {
		this.buscarEntidad = buscarEntidad;
	}*/

	public boolean[] getCamposCobroModal() {
		return camposCobroModal;
	}

	public void setCamposCobroModal(boolean[] camposCobroModal) {
		this.camposCobroModal = camposCobroModal;
	}

	public double getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(double saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public double getSaldoVencido() {
		return saldoVencido;
	}

	public void setSaldoVencido(double saldoVencido) {
		this.saldoVencido = saldoVencido;
	}

	public boolean isCondicionHabilitada() {
		return condicionHabilitada;
	}

	public void setCondicionHabilitada(boolean condicionHabilitada) {
		this.condicionHabilitada = condicionHabilitada;
	}

	public boolean isDisableCobranza() {
		return this.disableCobranza;
	}

	public void setDisableCobranza(boolean disableCobranza) {
		this.disableCobranza = disableCobranza;
	}

	public double getIva10() {
		return iva10;
	}

	public void setIva10(double iva10) {
		this.iva10 = iva10;
	}

	public double getIva5() {
		return iva5;
	}

	public void setIva5(double iva5) {
		this.iva5 = iva5;
	}

	public boolean isOpCrearCobranza() {
		return opCrearCobranza;
	}

	public void setOpCrearCobranza(boolean opCrearCobranza) {
		this.opCrearCobranza = opCrearCobranza;
	}

	public boolean isOpEditarCobranza() {
		return opEditarCobranza;
	}

	public void setOpEditarCobranza(boolean opEditarCobranza) {
		this.opEditarCobranza = opEditarCobranza;
	}

	public boolean isOpBorrarCobranza() {
		return opBorrarCobranza;
	}

	public void setOpBorrarCobranza(boolean opBorrarCobranza) {
		this.opBorrarCobranza = opBorrarCobranza;
	}

	public boolean isOpAnularCobranza() {
		return opAnularCobranza;
	}

	public void setOpAnularCobranza(boolean opAnularCobranza) {
		this.opAnularCobranza = opAnularCobranza;
	}

	public double getExento() {
		return exento;
	}

	public void setExento(double exento) {
		this.exento = exento;
	}

	public boolean isOpDefinirFecha() {
		return opDefinirFecha;
	}

	public void setOpDefinirFecha(boolean opDefinirFecha) {
		this.opDefinirFecha = opDefinirFecha;
	}

	public FinderModel getComprobanteFinder() {
		return comprobanteFinder;
	}

	public void setComprobanteFinder(FinderModel comprobanteFinder) {
		this.comprobanteFinder = comprobanteFinder;
	}

	public FinderModel getCondicionFinder() {
		return condicionFinder;
	}

	public void setCondicionFinder(FinderModel condicionFinder) {
		this.condicionFinder = condicionFinder;
	}

	public FinderModel getAlumnoFinder() {
		return alumnoFinder;
	}

	public void setAlumnoFinder(FinderModel alumnoFinder) {
		this.alumnoFinder = alumnoFinder;
	}

	public FinderModel getCuentaFinder() {
		return cuentaFinder;
	}

	public void setCuentaFinder(FinderModel cuentaFinder) {
		this.cuentaFinder = cuentaFinder;
	}

	public FinderModel getCuentaCRfinder() {
		return cuentaCRfinder;
	}

	public void setCuentaCRfinder(FinderModel cuentaCRfinder) {
		this.cuentaCRfinder = cuentaCRfinder;
	}

	public FinderModel getEntidadFinder() {
		return entidadFinder;
	}

	public void setEntidadFinder(FinderModel entidadFinder) {
		this.entidadFinder = entidadFinder;
	}

	public boolean isDisableCobranzaTipoPago() {
		return disableCobranzaTipoPago;
	}

	public void setDisableCobranzaTipoPago(boolean disableCobranzaTipoPago) {
		this.disableCobranzaTipoPago = disableCobranzaTipoPago;
	}

}
