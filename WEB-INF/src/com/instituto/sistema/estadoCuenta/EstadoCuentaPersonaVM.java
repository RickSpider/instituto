package com.instituto.sistema.estadoCuenta;

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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.components.finder.FinderModel;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.Concepto;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.modelo.Persona;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class EstadoCuentaPersonaVM extends TemplateViewModelLocal {

	//private Alumno alumnoSelected;
	private Persona personaSelected;
	private CursoVigente cursoVigenteSelected;
	private List<EstadoCuenta> lEstadosCuentas;
	private List<CobranzaDetalle> lCobranzasDetalles;
	
	private EstadoCuenta estadoCuentaSelected;
	
	private boolean opCrearEstadoCuentaPersona;
	private boolean opInactivarEstadoCuentaPersona;
	private boolean opBorrarEstadoCuentaPersona;
	
	private double saldoTotal = 0;
	private double saldoVencido = 0;
	
	private Date fechaInicio = new Date();
	private Date fechaFin = new Date();
	
	private double contadoTotal = 0;
	private double creditoTotal = 0;
	private double reciboTotal = 0;

	@Init(superclass = true)
	public void initEstadoCuentaVM() {
		
		
		this.cargarRangoFecha();
		this.inicializarFinders();

	}

	@AfterCompose(superclass = true)
	public void afterComposeEstadoCuentaVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearEstadoCuentaPersona = this.operacionHabilitada(ParamsLocal.OP_CREAR_ESTADOCUENTAPERSONA);
		this.opInactivarEstadoCuentaPersona = this.operacionHabilitada(ParamsLocal.OP_INACTIVAR_ESTADOCUENTAPERSONA);
		this.opBorrarEstadoCuentaPersona = this.operacionHabilitada(ParamsLocal.OP_BORRAR_ESTADOCUENTAPERSONA);

	}
	
	private void cargarRangoFecha() {
		
		Calendar calendar = Calendar.getInstance();
        
        // Establecer el día del mes en 1 para obtener la primera fecha del mes actual
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        this.fechaInicio = calendar.getTime();
        
        // Avanzar al primer día del mes siguiente
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1); // Retroceder un día para obtener el último día del mes actual
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        this.fechaFin = calendar.getTime();
		
	}
	
	// SeccionFinder
	
	private FinderModel personaFinder;
	
	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlAlumno = "select personaid, "
				+ "case when p.ruc is null or p.ruc like '' then  p.documentonum else p.ruc end as Ruc_CI, "
				+ "case when razonsocial is not null then p.razonsocial else (p.apellido||', '||p.nombre)end as RazonSocial "
				+ "from personas p;\r\n" + "";

		personaFinder = new FinderModel("Persona", sqlAlumno);
		
		
		String sqlTipo = "Select t.tipoid as id, t.tipo as comprobante, t.descripcion as descripcion from tipos t\n"
						+ "join tipotipos tt on tt.tipotipoid = t.tipotipoid \n"
						+ "where tt.sigla like '?1' \n"
						+ "order by t.tipoid asc;";
		
		
		

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

		}

	}
	
	@Command
	@NotifyChange("*")
	public void onChangeFiltroFechas() {
		
		if (this.personaSelected == null) {
			
			return;
		}
		
		this.refrescarEstadosCuentas();
		this.calcularSaldos();
		
	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.setListFinder(this.filtrarListaObject(filter, this.personaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

		}
		
	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			Persona p = this.reg.getObjectById(Persona.class.getName(), id);
			this.personaSelected = p;

			this.lEstadosCuentas = null;
			this.lCobranzasDetalles = null;
			this.saldoVencido = 0;
			this.saldoTotal = 0;
			
			this.refrescarEstadosCuentas();
			this.calcularSaldos();
			this.calcularTotales();
		}
		
		

	}

	// buscar curso vigente

	
	public void calcularSaldos() {
		
		String sqlSaldoTotal = this.um.getSql("estadoCuentaPersona/saldoTotalPorPersona.sql").replace("?1",
				this.personaSelected.getPersonaid() + "");
		List<Object[]> resultSaldoTotal = this.reg.sqlNativo(sqlSaldoTotal);
		this.saldoTotal = 0;
		if (resultSaldoTotal.size() > 0) {
			this.saldoTotal = Double.parseDouble(resultSaldoTotal.get(0)[1].toString());
		}

		String sqlSaldoVencido = this.um.getSql("estadoCuentaPersona/saldoVencidoPorPersona.sql").replace("?1",
				this.personaSelected.getPersonaid() + "");
		List<Object[]> resultSaldoVencido = this.reg.sqlNativo(sqlSaldoVencido);
		this.saldoVencido = 0;
		if (resultSaldoVencido.size() > 0) {
			this.saldoVencido = Double.parseDouble(resultSaldoVencido.get(0)[1].toString());
		}
		
	}
	
	private void calcularTotales() {
		

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String sqlTotales = this.um.getSql("estadoCuentaPersona/totalesPorPersona.sql").replace("?1", this.personaSelected.getPersonaid()+"")
				.replace("?2", sdf.format(this.fechaInicio))
				.replace("?3", sdf.format(this.fechaFin));
		
		//System.out.println(sqlTotales);
		
		List<Object[]> resultTotal = this.reg.sqlNativo(sqlTotales);
		this.saldoTotal = 0;
		
		for (Object[] x : resultTotal) {
			
			if (x[3].toString().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA)==0) {
				
				if (x[4].toString().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO) == 0) {
					
					this.contadoTotal =  Double.parseDouble(x[1].toString());
					
				}else {
					
					this.creditoTotal =  Double.parseDouble(x[2].toString());
					
				}
				
			}else {
				
				this.reciboTotal = Double.parseDouble(x[1].toString());
				
			}
			
		}

	}
	
	@NotifyChange({"lEstadosCuentas"})
	public void refrescarEstadosCuentas() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.lEstadosCuentas = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(),
				"personaid = " + this.personaSelected.getPersonaid() 
				+"and creado between '"+sdf.format(this.fechaInicio)+"' and '"+sdf.format(this.fechaFin)+"' ",
				"estadocuentaid desc");
		
	}
	
	@Command
	@NotifyChange({ "lCobranzasDetalles" })
	public void refrescarCobranzaDetalle(@BindingParam("estadoCuenta") EstadoCuenta estadoCuenta) {
		
		this.lCobranzasDetalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(),
				"estadocuentaid = " + estadoCuenta.getEstadocuentaid(), null);
		
	}
	
	@Command
	public void verComprobante(@BindingParam("cobranza") Cobranza cobranza) {
		
		if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_RECIBO) == 0) {
			
			Executions.getCurrent().sendRedirect("/instituto/zul/administracion/reciboReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
			
		}
		
		if (cobranza.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
			
			Executions.getCurrent().sendRedirect("/instituto/zul/administracion/facturaReporte.zul?id="+cobranza.getCobranzaid(),"_blank");
			
		}

	}

	//Agregar estado cuenta
	
	private Window modal;
	
	@Command
	public void modalEstadoCuenta() {
		
		if (!this.opCrearEstadoCuentaPersona) {
			return;			
		}
		
		if (this.personaSelected == null) {
			this.mensajeInfo("Debes seleccionar una Persona Fisica o Juridica.");
			return;
			
		} 
		
		this.buscarConcepto = "";
		
		this.estadoCuentaSelected = new EstadoCuenta();
		
		this.estadoCuentaSelected.setPersona(this.personaSelected);
		this.estadoCuentaSelected.setCargaManual(true);
		

		modal = (Window) Executions.createComponents("/instituto/zul/estadoCuenta/estadoCuentaModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	

	@Command
	@NotifyChange("lEstadosCuentas")
	public void guardar() {
		
		this.save(this.estadoCuentaSelected);
		
		Notification.show("El Estado de cuenta fue actualizado.");
		
		this.modal.detach();
		
		refrescarEstadosCuentas();
		
		
	}
	
	
	@Command
	public void modalInactivarEstadoCuenta(@BindingParam("estadocuenta") EstadoCuenta estadoCuenta) {
		
		if (!this.opInactivarEstadoCuentaPersona) {
			
			this.mensajeInfo("No tienes permisos para realizar la operación.");
			return;
		}
		
		this.estadoCuentaSelected = estadoCuenta;
		
		if(this.estadoCuentaSelected.isInactivo()){
			
			this.mensajeInfo("El estado de cuenta ya se encuentra inactivo");
			return;
		}
		
		Double sum = this.estadoCuentaSelected.getPago() + this.estadoCuentaSelected.getMontoDescuento();
		
		if (sum > 0) {
			
			this.mensajeInfo("Ya posee pago, no se puede inactivar.");
			
			return;
			
		}
		
		modal = (Window) Executions.createComponents("/instituto/zul/estadoCuenta/EstadoCuentaInactivarModal.zul",
				this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
		
	}
	
	@Command()
	@NotifyChange("lEstadosCuentas")
	public void guardarInactivacion() {
		
		this.estadoCuentaSelected.setUsuarioInactivacion(this.getCurrentUser().getAccount());
		this.estadoCuentaSelected.setFechaInactivacion(new Date());
		this.estadoCuentaSelected.setInactivo(true);
		
		this.save(this.estadoCuentaSelected);
		
		this.modal.detach();
		
		refrescarEstadosCuentas();
		
	}
	
	@Command
	public void borrarEstadoCuentaConfirmacion(@BindingParam("estadocuenta") final EstadoCuenta estadoCuenta) {

		if (!this.opBorrarEstadoCuentaPersona)
			return;
		
		if (!estadoCuenta.isCargaManual()) {
			
			this.mensajeInfo("Solo puedes borrar las cargas manuales.");
			return;
		}
		
		List<CobranzaDetalle> detalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(),
				"estadocuentaid = " + estadoCuenta.getEstadocuentaid(), null);
		
		if (detalles.size() > 0) {
			
			this.mensajeInfo("No se puede eliminar ya posee transacciones, solo puede inactivar.");
			return;
		}

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarEstadoCuentaManual(estadoCuenta);

				}

			}

		};

		this.mensajeEliminar("El estado de cuenta sera eliminado. \n Continuar?", event);
	}

	private void borrarEstadoCuentaManual(EstadoCuenta estadoCuenta) {

		this.reg.deleteObject(estadoCuenta);

		this.refrescarEstadosCuentas();

		BindUtils.postNotifyChange(null, null, this, "lEstadosCuentas");

	}
	
	//Buscar concepto
	private List<Object[]> lConceptosbuscarOri;
	private List<Object[]> lConceptosBuscar;
	private Concepto buscarSelectedConcepto;
	private String buscarConcepto = "";

	@Command
	@NotifyChange("lConceptosBuscar")
	public void filtrarConceptoBuscar() {

		this.lConceptosBuscar = this.filtrarListaObject(buscarConcepto, this.lConceptosbuscarOri);

	}

	@Command
	@NotifyChange("lConceptosBuscar")
	public void generarListaBuscarConcepto() {

		String sqlBuscarConcepto = this.um.getSql("buscarConcepto.sql");

		this.lConceptosBuscar = this.reg.sqlNativo(sqlBuscarConcepto);
		this.lConceptosbuscarOri = this.lConceptosBuscar;
	}

	@Command
	@NotifyChange("buscarConcepto")
	public void onSelectConcepto(@BindingParam("id") long id) {

		this.buscarSelectedConcepto = this.reg.getObjectById(Concepto.class.getName(), id);
		this.buscarConcepto = buscarSelectedConcepto.getConcepto();
		this.estadoCuentaSelected.setConcepto(buscarSelectedConcepto);

	}

	//Fin agregar estado cuenta


	public CursoVigente getCursoVigenteSelected() {
		return cursoVigenteSelected;
	}

	public void setCursoVigenteSelected(CursoVigente cursoVigenteSelected) {
		this.cursoVigenteSelected = cursoVigenteSelected;
	}

	public List<EstadoCuenta> getlEstadosCuentas() {
		return lEstadosCuentas;
	}

	public void setlEstadosCuentas(List<EstadoCuenta> lEstadosCuentas) {
		this.lEstadosCuentas = lEstadosCuentas;
	}

	public List<CobranzaDetalle> getlCobranzasDetalles() {
		return lCobranzasDetalles;
	}

	public void setlCobranzasDetalles(List<CobranzaDetalle> lCobranzasDetalles) {
		this.lCobranzasDetalles = lCobranzasDetalles;
	}

	public EstadoCuenta getEstadoCuentaSelected() {
		return estadoCuentaSelected;
	}

	public void setEstadoCuentaSelected(EstadoCuenta estadoCuentaSelected) {
		this.estadoCuentaSelected = estadoCuentaSelected;
	}

	public List<Object[]> getlConceptosBuscar() {
		return lConceptosBuscar;
	}

	public void setlConceptosBuscar(List<Object[]> lConceptosBuscar) {
		this.lConceptosBuscar = lConceptosBuscar;
	}

	public String getBuscarConcepto() {
		return buscarConcepto;
	}

	public void setBuscarConcepto(String buscarConcepto) {
		this.buscarConcepto = buscarConcepto;
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

	public FinderModel getPersonaFinder() {
		return personaFinder;
	}

	public void setPersonaFinder(FinderModel personaFinder) {
		this.personaFinder = personaFinder;
	}

	public Persona getPersonaSelected() {
		return personaSelected;
	}

	public void setPersonaSelected(Persona personaSelected) {
		this.personaSelected = personaSelected;
	}

	public boolean isOpCrearEstadoCuentaPersona() {
		return opCrearEstadoCuentaPersona;
	}

	public void setOpCrearEstadoCuentaPersona(boolean opCrearEstadoCuentaPersona) {
		this.opCrearEstadoCuentaPersona = opCrearEstadoCuentaPersona;
	}

	public boolean isOpInactivarEstadoCuentaPersona() {
		return opInactivarEstadoCuentaPersona;
	}

	public void setOpInactivarEstadoCuentaPersona(boolean opInactivarEstadoCuentaPersona) {
		this.opInactivarEstadoCuentaPersona = opInactivarEstadoCuentaPersona;
	}

	public boolean isOpBorrarEstadoCuentaPersona() {
		return opBorrarEstadoCuentaPersona;
	}

	public void setOpBorrarEstadoCuentaPersona(boolean opBorrarEstadoCuentaPersona) {
		this.opBorrarEstadoCuentaPersona = opBorrarEstadoCuentaPersona;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public double getContadoTotal() {
		return contadoTotal;
	}

	public void setContadoTotal(double contadoTotal) {
		this.contadoTotal = contadoTotal;
	}

	public double getCreditoTotal() {
		return creditoTotal;
	}

	public void setCreditoTotal(double creditoTotal) {
		this.creditoTotal = creditoTotal;
	}

	public double getReciboTotal() {
		return reciboTotal;
	}

	public void setReciboTotal(double reciboTotal) {
		this.reciboTotal = reciboTotal;
	}
	
	
}
