package com.instituto.sistema.reporte;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.doxacore.modelo.Tipo;
import com.doxacore.report.ReportExcel;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.CursoVigente;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class FacturacionReporteVM extends TemplateViewModelLocal {

	private boolean opCrearFacturacionReporte;
	private Date fechaInicio = new Date();
	private Date fechaFin = new Date();
	private List<String> listaRegistros = new ArrayList<String>();
	private List<String> listaComprobantes = new ArrayList<String>();
	private String tipoRegistroSelected ="Todos";
	private String tipoComprobanteSelected ="Todos";

	@Init(superclass = true)
	public void initFacturacionReporteVM() {
		
		listaRegistros.add("Todos");
		listaRegistros.add("Activos");
		listaRegistros.add("Anulados");
		
		listaComprobantes.add("Todos");
		listaComprobantes.add("Facturas");
		listaComprobantes.add("Recibos");

	}

	@AfterCompose(superclass = true)
	public void afterComposeFacturacionReporteVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearFacturacionReporte = this.operacionHabilitada(ParamsLocal.OP_CREAR_FACTURACIONREPORTE);

	}	
	
	//Reporte
	
	@Command
	public void generarReporte() throws ParseException {
		
		if (!this.opCrearFacturacionReporte) {
			
			this.mensajeError("No tines Permiso para generar este reporte.");
			
		}
		
		ReportExcel re = new ReportExcel("Facturacion");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		//CursoVigente cv = this.reg.getObjectById(CursoVigente.class.getName(), cursoVigenteid);
		
		List<String[]> titulos = new ArrayList<String[]>();
		
		String[] t1 = {"INSTITUTO SANTO TOMAS"};
		String[] t2 = {"Resolucion M.E.C. Nº 841/98"};
		String[] t3 = {"Sede:",this.getCurrentSede().getSede()};
		String[] t4 = {"REPORTE DE FACTURACION"};
		String[] t5 = {"Fecha Inicio:", sdf.format(fechaInicio)};
		String[] t6 = {"Fecha Fin:", sdf.format(fechaFin)};
		String[] espacioBlanco = {""};
		
		titulos.add(t1);
		titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		titulos.add(t5);
		titulos.add(t6);
		titulos.add(espacioBlanco);
		
		List<String[]> headersDatos = new ArrayList<String[]>();
		String [] hd1 =  {"Fecha","Tipo Comprobante","Factura Nro", "Nombre Apellido", "Importe","Totales"};
		headersDatos.add(hd1);
		
		
		String sql1 =  this.um.getSql("facturacionReporte/facturasReporte.sql").replace("?1", sdf.format(fechaInicio) ).replace("?2", sdf.format(fechaFin));
		
		if (this.tipoRegistroSelected.compareTo("Activos") == 0) {
			
			sql1 = sql1.replace("--1", "").replace("?3", "false");
			
		}
		
		if (this.tipoRegistroSelected.compareTo("Anulados") == 0) {
			
			sql1 = sql1.replace("--1", "").replace("?3", "true");
			
		}
		
		if (this.tipoComprobanteSelected.compareTo("Facturas") == 0) {
			
			Tipo t = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_FACTURA);
			sql1 = sql1.replace("--2", "").replace("?4", t.getTipoid()+"");
			
		}
		
		if (this.tipoComprobanteSelected.compareTo("Recibos") == 0) {
			
			Tipo t = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_RECIBO);
			sql1 = sql1.replace("--2", "").replace("?4", t.getTipoid()+"");
			
		}	
		
		System.out.println(sql1);
		
		List<Object[]> datos = this.reg.sqlNativo(sql1);
		
		List<Object[]> datos2 = new ArrayList<>();
		String mes ="";
		Double totalMes = 0.0 ;
		Double totalGeneral = 0.0 ;
		SimpleDateFormat sdfMes = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("es", "ES"));
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0.##",dfs);
		df.setGroupingUsed(true);
	
		
		for (int i = 0 ; i<datos.size() ; i++) {
			
			Object[] o = datos.get(i);
			
			Date d = sdf.parse(o[1].toString());
			
			if (mes.compareTo(sdfMes.format(d))!=0) {
				
				if (datos2.size()>0) {
					Object[] tMes = new Object[6];
					tMes[0]="";
					tMes[1]="";
					tMes[2]="Total Mes";
					tMes[3]="";
					tMes[4]="";
					tMes[5]=df.format(totalMes);
					totalMes = 0.0;
					datos2.add(tMes);
				}
				
				Object[] oMes = new Object[1];
				mes = sdfMes.format(d);
				oMes[0] = mes.toUpperCase();
				datos2.add(oMes);
			}
			 
			
			Object[] det = new Object[6]; 
			
			det[0]= o[1];
			det[1]= o[2];
			det[2]= o[3];
			if (Boolean.parseBoolean(o[6].toString())) {
				
				det[3]="ANULADO";
				det[4]= "";
				
			}else {
				
				det[3]= o[5];
				det[4]= df.format((double) o[4]);
				totalMes += Double.valueOf(o[4].toString());
				totalGeneral += Double.valueOf(o[4].toString());
			}
			det[5]="";
			
			
			
			
			datos2.add(det);
			
		}
		
		Object[] tMes = new Object[6];
		tMes[0]="";
		tMes[1]="";
		tMes[2]="Total Mes";
		tMes[3]="";
		tMes[4]="";
		tMes[5]=df.format(totalMes);
		totalMes = 0.0;
		
		datos2.add(tMes);
		
		Object[] tGeneral = new Object[6];
		tGeneral[0]="TOTAL GENERAL";
		tGeneral[1]="";
		tGeneral[2]="";
		tGeneral[3]="";
		tGeneral[4]="";
		tGeneral[5]=df.format(totalGeneral);
		
		datos2.add(tGeneral);
	
		
		re.descargar(titulos, headersDatos, datos2);
		
	}
	

	public boolean isOpCrearFacturacionReporte() {
		return opCrearFacturacionReporte;
	}

	public void setOpCrearFacturacionReporte(boolean opCrearFacturacionReporte) {
		this.opCrearFacturacionReporte = opCrearFacturacionReporte;
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

	public List<String> getListaRegistros() {
		return listaRegistros;
	}

	public void setListaRegistros(List<String> listaRegistros) {
		this.listaRegistros = listaRegistros;
	}

	public String getTipoRegistroSelected() {
		return tipoRegistroSelected;
	}

	public void setTipoRegistroSelected(String tipoRegistroSelected) {
		this.tipoRegistroSelected = tipoRegistroSelected;
	}

	public List<String> getListaComprobantes() {
		return listaComprobantes;
	}

	public void setListaComprobantes(List<String> listaComprobantes) {
		this.listaComprobantes = listaComprobantes;
	}

	public String getTipoComprobanteSelected() {
		return tipoComprobanteSelected;
	}

	public void setTipoComprobanteSelected(String tipoComprobanteSelected) {
		this.tipoComprobanteSelected = tipoComprobanteSelected;
	}


}
