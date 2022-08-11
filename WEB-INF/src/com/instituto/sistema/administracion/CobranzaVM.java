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
import com.instituto.modelo.EstadoCuenta;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class CobranzaVM extends TemplateViewModelLocal {

	private Cobranza cobranzaSelected;
	private boolean editar = false;
	private List<CobranzaDetalleCobro> lDetallesCobros = new ArrayList<CobranzaDetalleCobro>();
	private List<CobranzaDetalle> lDetalles = new ArrayList<CobranzaDetalle>();

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
	@NotifyChange("buscarAlumno")
	public void onSelectAlumno(@BindingParam("id") long id) {

		this.buscarSelectedAlumno = this.reg.getObjectById(Alumno.class.getName(), id);
		this.buscarAlumno = buscarSelectedAlumno.getFullNombre();
		this.cobranzaSelected.setAlumno(buscarSelectedAlumno);

		if (buscarSelectedAlumno.getPersonaFacturacion() == null) {

			this.cobranzaSelected.setPersonaFacturacion(buscarSelectedAlumno.getPersona());

		} else {

			this.cobranzaSelected.setPersonaFacturacion(buscarSelectedAlumno.getPersonaFacturacion());

		}

	}

	// fin Buscar Alumno
	
	//Seccion modal detalles
	
	private Window modal;
	private List<EstadoCuenta> lEstadosCuentas = new ArrayList<EstadoCuenta>();
	private List<EstadoCuenta> lEstadosCuentasAux = new ArrayList<EstadoCuenta>();
	
	@Command
	public void modalCobranzaDetalle() {
		
		if( this.cobranzaSelected.getAlumno() == null) {
			return;
		}
		
		
		
		String condicion2 = "";
		
		if (this.lDetalles.size() > 0) {
			
			StringBuffer ids = new StringBuffer();
			
			for (int i = 0 ; i<lDetalles.size() ; i++ ) {
				
				if (i!=0) {
					
					ids.append(", ");
					
				}
				
				ids.append(this.lDetalles.get(i).getEstadoCuenta().getEstadocuentaid());
				
			}
			
			condicion2 = "AND estadocuentaid not in ("+ids+") ";
			
		}
		
		this.lEstadosCuentas = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(), 
				"alumnoid = "+ this.cobranzaSelected.getAlumno().getAlumnoid() + "AND monto > (pago+montodescuento) "+condicion2, 
				"vencimiento asc");
		
		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cobranzaDetalleModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lDetalles")
	public void borrarDetalle(@BindingParam("dato") CobranzaDetalle	dato) {
		
		if(editar) {
			
			return;
			
		}
		
		this.lDetalles.remove(dato);
		
	}
	
	@Command
	public void onSelectEstadocuenta(@BindingParam("dato") EstadoCuenta dato){
		
		this.lEstadosCuentasAux.add(dato);
		
	}
	
	@Command
	@NotifyChange("lDetalles")
	public void agregarCobranzaDetalle() {
		
		for (EstadoCuenta x : this.lEstadosCuentasAux) {
			
			CobranzaDetalle cobranzaDetalle = new CobranzaDetalle();
			cobranzaDetalle.setEstadoCuenta(x);
			this.lDetalles.add(cobranzaDetalle);
		}
		
		this.lEstadosCuentasAux =  new ArrayList<EstadoCuenta>();
		this.modal.detach();
	}
	
	//Fin modal detalles
	
	// Seccion detalle Cobro
	
	private CobranzaDetalleCobro cobranzaDetalleCobroSelected ;
	
	@Command
	public void modalCobranzaDetalleCobro() {
		
		if( this.cobranzaSelected.getAlumno() == null) {
			return;
		}
		
		this.buscarMoneda = "";
		this.buscarFormaPago = "";
		
		cobranzaDetalleCobroSelected = new CobranzaDetalleCobro();

		modal = (Window) Executions.createComponents("/instituto/zul/administracion/cobranzaDetalleCobroModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	// fin detalle cobro
	
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

		String sqlBuscarTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1",
				sigla);

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
		this.filtroBuscarTipo="";
	}
	
	@Command
	@NotifyChange("buscarFormaPago")
	public void onSelectFormaPago(@BindingParam("id") long id) {

		Tipo tipo = this.reg.getObjectById(Tipo.class.getName(), id);
		this.cobranzaDetalleCobroSelected.setFormaPago(tipo);
		this.buscarFormaPago = tipo.getTipo();
		this.filtroBuscarTipo="";
	}
	
	@Command
	@NotifyChange("buscarComprobante")
	public void onSelectComprobante(@BindingParam("id") long id) {

		Tipo comprobante = this.reg.getObjectById(Tipo.class.getName(), id);
		this.cobranzaSelected.setComprobanteTipo(comprobante);
		this.buscarComprobante = comprobante.getTipo();
		this.filtroBuscarTipo="";
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

}
