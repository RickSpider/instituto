package com.instituto.sistema.administracion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import com.doxacore.modelo.Tipo;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.CobranzaDetalleCobro;
import com.instituto.modelo.CursoVigenteConvenio;
import com.instituto.modelo.Entidad;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CobranzaVM extends TemplateViewModelLocal {

	private Cobranza cobranzaSelected;
	private boolean editar = false;
	private List<CobranzaDetalleCobro> lDetallesCobros = new ArrayList<CobranzaDetalleCobro>();
	private List<CobranzaDetalle> lDetalles = new ArrayList<CobranzaDetalle>();
	private double saldoTotal;
	private double saldoVencido;

	@Init(superclass = true)
	public void initConvenioVM() {

		cobranzaSelected = new Cobranza();

	}

	@AfterCompose(superclass = true)
	public void afterComposeConvenioVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub

	}

	// Seccion Buscar Alumno
	private List<Object[]> lAlumnosbuscarOri;
	private List<Object[]> lAlumnosBuscar;
	private Alumno buscarSelectedAlumno;
	private String buscarAlumno = "";

	@Command
	@NotifyChange("lAlumnosBuscar")
	public void filtrarAlumnoBuscar() {

		this.lAlumnosBuscar = this.filtrarListaObject(buscarAlumno, this.lAlumnosbuscarOri);

	}

	@Command
	@NotifyChange("lAlumnosBuscar")
	public void generarListaBuscarAlumno() {

		String sqlBuscarAlumno = this.um.getSql("buscarAlumnoNotSede.sql");

		this.lAlumnosBuscar = this.reg.sqlNativo(sqlBuscarAlumno);
		this.lAlumnosbuscarOri = this.lAlumnosBuscar;
	}

	@Command
	@NotifyChange({"buscarAlumno","saldoTotal","saldoVencido"})
	public void onSelectAlumno(@BindingParam("id") long id) {

		this.buscarSelectedAlumno = this.reg.getObjectById(Alumno.class.getName(), id);
		this.buscarAlumno = buscarSelectedAlumno.getFullNombre();
		this.cobranzaSelected.setAlumno(buscarSelectedAlumno);

		if (buscarSelectedAlumno.getPersonaFacturacion() == null) {

			this.cobranzaSelected.setPersonaFacturacion(buscarSelectedAlumno.getPersona());

		} else {

			this.cobranzaSelected.setPersonaFacturacion(buscarSelectedAlumno.getPersonaFacturacion());

		}
		
		String sqlSaldoTotal = this.um.getSql("saldoTotalPorAlumno.sql").replace("?1", this.buscarSelectedAlumno.getAlumnoid()+"");
		List<Object[]> resultSaldoTotal = this.reg.sqlNativo(sqlSaldoTotal);
		this.saldoTotal = Double.parseDouble(resultSaldoTotal.get(0)[1].toString());
		
		String sqlSaldoVencido = this.um.getSql("saldoVencidoPorAlumno.sql").replace("?1", this.buscarSelectedAlumno.getAlumnoid()+"");
		List<Object[]> resultSaldoVencido = this.reg.sqlNativo(sqlSaldoVencido);
		this.saldoVencido = Double.parseDouble(resultSaldoVencido.get(0)[1].toString());
		

	}

	// fin Buscar Alumno

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

		this.lEstadosCuentas = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(), "alumnoid = "
				+ this.cobranzaSelected.getAlumno().getAlumnoid() + "AND monto > (pago+montodescuento) " + condicion2,
				"vencimiento asc");

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cobranzaDetalleModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}

	@Command
	@NotifyChange("lDetalles")
	public void borrarDetalle(@BindingParam("dato") CobranzaDetalle dato) {

		if (editar) {

			return;

		}

		this.lDetalles.remove(dato);

	}

	@Command
	public void onSelectEstadocuenta(@BindingParam("dato") EstadoCuenta dato) {

		this.lEstadosCuentasAux.add(dato);

	}

	@Command
	@NotifyChange("lDetalles")
	public void agregarCobranzaDetalle() {

		for (EstadoCuenta x : this.lEstadosCuentasAux) {

			CobranzaDetalle cobranzaDetalle = new CobranzaDetalle();
			cobranzaDetalle.setEstadoCuenta(x);
			cobranzaDetalle = buscarDescuento(cobranzaDetalle);
			cobranzaDetalle.setMonto(cobranzaDetalle.getEstadoCuenta().getMonto() - cobranzaDetalle.getMontoDescuento());
			this.lDetalles.add(cobranzaDetalle);
			
		}

		this.lEstadosCuentasAux = new ArrayList<EstadoCuenta>();
		this.modal.detach();
	}

	private CobranzaDetalle buscarDescuento(CobranzaDetalle cobranzaDetalle) {

		String sql = this.um.getSql("bucarDescuentoConvenio.sql")
				.replace("?1", cobranzaDetalle.getEstadoCuenta().getCursoVigente().getCursovigenteid() + "")
				.replace("?2", cobranzaDetalle.getEstadoCuenta().getConcepto().getConceptoid() + "")
				.replace("?3", cobranzaDetalle.getEstadoCuenta().getAlumno().getAlumnoid() + "");

		List<Object[]> result = this.reg.sqlNativo(sql);

		if (result.size() > 0) {

			boolean porcentaje = Boolean.parseBoolean(result.get(0)[5].toString());
			double importe = Double.parseDouble(result.get(0)[4].toString());
			String[] periodos = result.get(0)[6].toString().split(",");

			for (int i = 0; i < periodos.length - 1; i++) {

				int periodo = Integer.parseInt(periodos[i]);

				if (periodo == -1 || cobranzaDetalle.getEstadoCuenta().getPeriodo() == periodo) {

					if (porcentaje) {

						double porcentajeCalculo = importe / 100;

						double descuento = cobranzaDetalle.getEstadoCuenta().getMonto() / porcentajeCalculo;

						cobranzaDetalle.setMontoDescuento(descuento);

					} else {

						cobranzaDetalle.setMontoDescuento(importe);

					}
				}

			}

		}

		return cobranzaDetalle;

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

		this.buscarEntidad = "";

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

	@Command
	@NotifyChange("lDetallesCobros")
	public void agregarCobranzaDetalleCobro() {

		this.lDetallesCobros.add(cobranzaDetalleCobroSelected);

		this.modal.detach();

	}

	@Command
	@NotifyChange("lDetallesCobros")
	public void borrarCobranzaDetalleCobro(@BindingParam("dato") CobranzaDetalleCobro dato) {

		if (editar) {

			return;

		}

		this.lDetallesCobros.remove(dato);

	}

	private boolean[] camposCobroModal = new boolean[7];

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

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_CHEQUE) == 0) {

			camposCobroModal[0] = false;
			camposCobroModal[3] = false;
			camposCobroModal[4] = false;
			camposCobroModal[5] = false;
			camposCobroModal[6] = false;

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_GIRO) == 0) {

			camposCobroModal[0] = false;
			camposCobroModal[1] = false;
			camposCobroModal[2] = false;
			camposCobroModal[4] = false;

		}

		if (this.cobranzaDetalleCobroSelected.getFormaPago().getSigla()
				.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TRANSFERENCIA) == 0) {

			camposCobroModal[0] = false;
			camposCobroModal[1] = false;
			camposCobroModal[4] = false;

		}

	}

	// fin detalle cobro

	// Seccion Buscar Entidad
	private List<Object[]> lEntidadesbuscarOri;
	private List<Object[]> lEntidadesBuscar;
	private Entidad buscarSelectedEntidad;
	private String buscarEntidad = "";

	@Command
	@NotifyChange("lEntidadesBuscar")
	public void filtrarEntidadBuscar() {

		this.lEntidadesBuscar = this.filtrarListaObject(buscarEntidad, this.lEntidadesbuscarOri);

	}

	@Command
	@NotifyChange("lEntidadesBuscar")
	public void generarListaBuscarEntidad() {

		String sqlBuscarEntidad = this.um.getSql("buscarEntidad.sql");

		this.lEntidadesBuscar = this.reg.sqlNativo(sqlBuscarEntidad);
		this.lEntidadesbuscarOri = this.lEntidadesBuscar;
	}

	@Command
	@NotifyChange("buscarEntidad")
	public void onSelectEntidad(@BindingParam("id") long id) {

		this.buscarSelectedEntidad = this.reg.getObjectById(Entidad.class.getName(), id);
		this.buscarEntidad = buscarSelectedEntidad.getEntidad();
		this.cobranzaDetalleCobroSelected.setEntidad(buscarSelectedEntidad);

	}

	// fin Buscar Entidad

	// buscarTipo

	private List<Object[]> lTiposbuscarOri;
	private List<Object[]> lTiposBuscar;

	private String filtroBuscarTipo;

	private String buscarMoneda = "";
	private String buscarFormaPago = "";
	private String buscarComprobante = "";

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

	@Command
	@NotifyChange("lTiposBuscar")
	public void generarListaBuscarComprobante() {

		generarListaBuscarTipo(ParamsLocal.SIGLA_COMPROBANTE);

	}

	@Command
	@NotifyChange("buscarMoneda")
	public void onSelectMoneda(@BindingParam("id") long id) {

		Tipo tipo = this.reg.getObjectById(Tipo.class.getName(), id);
		this.cobranzaDetalleCobroSelected.setMonedaTipo(tipo);
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

	@Command
	@NotifyChange("buscarComprobante")
	public void onSelectComprobante(@BindingParam("id") long id) {

		Tipo comprobante = this.reg.getObjectById(Tipo.class.getName(), id);
		this.cobranzaSelected.setComprobanteTipo(comprobante);
		this.buscarComprobante = comprobante.getTipo();
		this.filtroBuscarTipo = "";
	}

	// fin buscarTipo

	public Cobranza getCobranzaSelected() {
		return cobranzaSelected;
	}

	public void setCobranzaSelected(Cobranza cobranzaSelected) {
		this.cobranzaSelected = cobranzaSelected;
	}

	public List<Object[]> getlAlumnosBuscar() {
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
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

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

	public String getBuscarComprobante() {
		return buscarComprobante;
	}

	public void setBuscarComprobante(String buscarComprobante) {
		this.buscarComprobante = buscarComprobante;
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

	public List<Object[]> getlEntidadesBuscar() {
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
	}

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

}
