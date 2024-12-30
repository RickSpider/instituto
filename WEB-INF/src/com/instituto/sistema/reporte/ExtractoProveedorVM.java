package com.instituto.sistema.reporte;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxacore.report.ReportExcel;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.Empresa;
import com.instituto.modelo.Proveedor;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class ExtractoProveedorVM extends TemplateViewModelLocal {

	private Date fechaInicio = new Date();
	private Date fechaFin = new Date();
	private Proveedor proveedorSelected;
	
	

	@Init(superclass = true)
	public void initExtractoProveedorVM() {
		
		this.fechaInicio = this.um.modificarHorasMinutosSegundos(new Date(), 0,0,0,0);
		this.fechaFin = this.um.modificarHorasMinutosSegundos(this.fechaInicio, 23, 59, 59, 999);
		
		this.inicializarFinders();
	
	}

	@AfterCompose(superclass = true)
	public void afterComposeExtractoProveedorVM() {
		
	
	}

	@Override
	protected void inicializarOperaciones() {

	

	}	
	
	private FinderModel proveedorFinder;
	
	
	@NotifyChange("*")
	public void inicializarFinders() {
		
		String sqlProveedor = this.um.getSql("proveedor/buscarProveedorSede.sql").replace("?1",
				this.getCurrentSede().getSedeid() + "");
		proveedorFinder = new FinderModel("Proveedor", sqlProveedor);
		
	}
	
	public void generarFinders(@BindingParam("finder") String finder) {
		
		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.proveedorFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.proveedorFinder, "listFinder");

			return;

		}
	}
	
	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {
		
		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {

			this.proveedorFinder
					.setListFinder(this.filtrarListaObject(filter, this.proveedorFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.proveedorFinder, "listFinder");

			return;

		}
		
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {
		
		if (finder.compareTo(this.proveedorFinder.getNameFinder()) == 0) {
			
			this.proveedorSelected = this.reg.getObjectById(Proveedor.class.getName(), id);
			return;
		}
		
	}
	
	
	
	//Reporte
	
	@Command
	public void generarReporte() throws ParseException {
		
		
		Date generado = new Date();
		
		SimpleDateFormat sdfArchivo = new SimpleDateFormat("yyyyMMdd_HHmmss");
		ReportExcel re = new ReportExcel("ExtractoProveedor"+sdfArchivo.format(generado));
		SimpleDateFormat sdfConsulta = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfRespuesta = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);
		
		List<String[]> titulos = new ArrayList<String[]>();
		
		SimpleDateFormat sdfRango = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String[] t0 = {"GENERADO", sdfRespuesta.format(generado)};
		String[] t01 = {"USUARIO", this.getCurrentUser().getAccount()};
		String[] t1 = {empresa.getNombreFantasia()};
		String[] t2 = {empresa.getExtra2()};
		String[] t3 = {"Sede:",this.getCurrentSede().getSede()};
		String[] t4 = {"REPORTE DE EXTRACTO DE PROVEEDOR"};
		String[] t5 = {"Fecha Inicio:", sdfRango.format(fechaInicio)};
		String[] t6 = {"Fecha Fin:", sdfRango.format(fechaFin)};
		
		String[] espacioBlanco = {""};
		
		titulos.add(t0);
		titulos.add(t01);
		titulos.add(espacioBlanco);
		titulos.add(t1);
		titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		titulos.add(t5);
		titulos.add(t6);
		titulos.add(espacioBlanco);
		
		List<String[]> headersDatos = new ArrayList<String[]>();
		//String [] hd1 =  {"Fecha","Comprobante","Factura Nro", "Total"};
		//headersDatos.add(hd1);
		
		
		String sql1 =  this.um.getSql("pago/extractoProveedor.sql").replace("?1", sdfConsulta.format(fechaInicio) )
				.replace("?2", sdfConsulta.format(fechaFin));
		
		if (this.proveedorSelected != null) {
			
			sql1 = sql1.replace("--#1", "").replace("?3", this.proveedorSelected.getProveedorid()+"");
			
		}
				
				
		
		//System.out.println(sql1);
		
		
		List<Object[]> datos = this.reg.sqlNativo(sql1);
		
		List<Object[]> datos2 = new ArrayList<>();

		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("es", "ES"));
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0.##",dfs);
		df.setGroupingUsed(true);
	
		
		double totalFContado = 0;
		double totalFCredito = 0;
		double totalRecibo = 0;
		double totalPFCredito = 0;
		
		double totalGFContado = 0;
		double totalGFCredito = 0;
		double totalGRecibo = 0;
		double totalGPFCredito = 0;
		
		
		
		long proveedorid = 0;
		Tipo recibo = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_RECIBO);
		Tipo condContado =  this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO);
		
		if (datos.size() > 0) {
			
			proveedorid = Long.parseLong(datos.get(0)[1].toString());
			Object[] proveedor = {"Proveedor", datos.get(0)[1].toString()+" - "+datos.get(0)[9].toString() };
			datos2.add(proveedor);
			
			String [] hd1 =  {"Fecha","Comprobante","Factura Nro", "Total"};
			datos2.add(hd1);
			
		}
		
		
				
		for (Object[] ox : datos) {
			
			
			long proveedoridActual = Long.parseLong(ox[1].toString());
			
			if (proveedoridActual != proveedorid) {
				
				
				
				datos2.add(espacioBlanco);
				Object[] tfcontado = {"Total Factura Contado", df.format(totalFContado)}; 
				Object[] tfcredito = {"Total Factura Credito", df.format(totalFCredito)}; 
				Object[] trecibo = {"Total Recibo", df.format(totalRecibo)}; 
				Object[] tPagoFCredito = {"Total Pago F. Credito ", df.format(totalPFCredito)}; 
				Object[] tSaldo = {"Saldo a Pagar", df.format(totalFCredito-totalPFCredito)}; 
				
				datos2.add(tfcontado);
				datos2.add(tfcredito);
				datos2.add(trecibo);
				datos2.add(tPagoFCredito);
				datos2.add(tSaldo);
				datos2.add(espacioBlanco);
				
				Object[] proveedor = {"Proveedor", ox[1].toString()+" - "+ox[9].toString() };
				datos2.add(proveedor);
				
				String [] hd1 =  {"Fecha","Comprobante","Factura Nro", "Total"};
				datos2.add(hd1);
				
				totalFContado = 0;
				totalFCredito = 0;
				totalRecibo = 0;
				totalPFCredito = 0;
				
				proveedorid = proveedoridActual;
				
			}
			
			long comprobanteid = Long.parseLong(ox[3].toString());
			
			if (comprobanteid == recibo.getTipoid().longValue()) {
				
				totalRecibo += Double.parseDouble(ox[7].toString());
				totalGRecibo += Double.parseDouble(ox[7].toString());
				
				if (ox[8] != null) {
					
					long idrelacionado = Long.parseLong(ox[8].toString());
					boolean existe = false;
					
					
					for (Object[] oxe : datos) {
						
						long idpago = Long.parseLong(oxe[1].toString());
						
						if (idpago == idrelacionado) {
							
							existe = true;
							break;
						}
					}
					
					if(existe) {
						
						totalPFCredito += Double.parseDouble(ox[7].toString());
						totalGPFCredito += Double.parseDouble(ox[7].toString());
						
					}
					
				}
				
				
			}else {
				
				long condVentaid = Long.parseLong(ox[4].toString());
				
				if (condVentaid == condContado.getTipoid().longValue()) {
					
					totalFContado += Double.parseDouble(ox[7].toString());
					totalGFContado += Double.parseDouble(ox[7].toString());
					
				}else {
					
					totalFCredito += Double.parseDouble(ox[7].toString());
					totalGFCredito += Double.parseDouble(ox[7].toString());
					
				}

				
			}
			
			Object[] oData = {ox[2], ox[5], ox[6], df.format((Double)ox[7])};
			
			datos2.add(oData);
			
		}	
		
		datos2.add(espacioBlanco);
		Object[] tfcontado = {"Total Factura Contado", df.format(totalFContado)}; 
		Object[] tfcredito = {"Total Factura Credito", df.format(totalFCredito)}; 
		Object[] trecibo = {"Total Recibo", df.format(totalRecibo)}; 
		Object[] tPagoFCredito = {"Total Pago F. Credito ", df.format(totalPFCredito)}; 
		Object[] tSaldo = {"Saldo a Pagar", df.format(totalGFCredito-totalPFCredito)}; 
		
		datos2.add(tfcontado);
		datos2.add(tfcredito);
		datos2.add(trecibo);
		datos2.add(tPagoFCredito);
		datos2.add(tSaldo);
	
		datos2.add(espacioBlanco);
		Object[] tgfcontado = {"Total General Factura Contado", df.format(totalGFContado)}; 
		Object[] tgfcredito = {"Total General Factura Credito", df.format(totalGFCredito)}; 
		Object[] tgrecibo = {"Total General Recibo", df.format(totalGRecibo)}; 
		Object[] tgPagoFCredito = {"Total General Pago F. Credito ", df.format(totalGPFCredito)}; 
		//Object[] tgSaldo = {"Saldo General a Pagar", df.format(totalGFCredito-totalGPFCredito)}; 
		
		datos2.add(tgfcontado);
		datos2.add(tgfcredito);
		datos2.add(tgrecibo);
		datos2.add(tgPagoFCredito);
		//datos2.add(tgSaldo);
		
		re.descargar(titulos, headersDatos, datos2);
		
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

	public Proveedor getProveedorSelected() {
		return proveedorSelected;
	}

	public void setProveedorSelected(Proveedor proveedorSelected) {
		this.proveedorSelected = proveedorSelected;
	}

	public FinderModel getProveedorFinder() {
		return proveedorFinder;
	}

	public void setProveedorFinder(FinderModel proveedorFinder) {
		this.proveedorFinder = proveedorFinder;
	}

	


}
