package com.instituto.sistema.reporte;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelArray;

import com.doxacore.modelo.Tipo;
import com.doxacore.report.ReportExcel;
import com.instituto.modelo.Empresa;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class TotalCursoReporteVM extends TemplateViewModelLocal {
	
	private boolean opCrearTotalCursoReporte;
	private Date fechaInicio;
	private Date fechaFin;
	
	private List<String> listaComprobantes = new ArrayList<String>();
	private String tipoComprobanteSelected ="Todos";
	
	private ListModelArray<DataModelCursoVigente> cursosVigentesModel;
	
	

	@Init(superclass = true)
	public void initTotalCursoReporteVM() {
		
		listaComprobantes.add("Todos");
		listaComprobantes.add("Facturas");
		listaComprobantes.add("Recibos");

		cargarLista();
		
	}
	
	private void cargarLista() {
		
		String cursoVigenteSQL = this.um.getSql("buscarCursoVigente.sql").replace("?1", "1"); 
		
		List<Object[]> lCursosVigentes = this.reg.sqlNativo(cursoVigenteSQL);
		
		List<DataModelCursoVigente> lcv = new ArrayList<>();
		
		int lcvSize = lCursosVigentes.size();
		
		for (int i = 0; i<lcvSize; i++) {
			
			Object [] ocv = lCursosVigentes.get(i);
			
			lcv.add(new DataModelCursoVigente(Long.parseLong(ocv[0].toString()), ocv[1].toString(), ocv[2].toString(), ocv[3].toString()));
			
		}

		cursosVigentesModel = new ListModelArray<>(lcv);
		
		cursosVigentesModel.setMultiple(true);
		
	}

	@AfterCompose(superclass = true)
	public void afterCompoTotalCursoReporteVM() {
		
	
	}
	
	@Override
	protected void inicializarOperaciones() {


		this.opCrearTotalCursoReporte = this.operacionHabilitada(ParamsLocal.OP_CREAR_TOTALCURSOREPORTE);
		
	}
	
	public void generarReporte() throws ParseException {
		
		if (!this.opCrearTotalCursoReporte) {
			
			this.mensajeError("No tines Permiso para generar este reporte.");
			
		}
		
		
		String cvis = cursosVigentesModel.getSelection().stream().map(dataModelCursoVigente -> String.valueOf(dataModelCursoVigente.getId())).collect(Collectors.joining(", "));
		
		if (cvis == null || cvis.isEmpty() ||cvis.length() == 0) {
			
			return;
			
		}
		
		Tipo t = null;
		
		if (this.tipoComprobanteSelected.compareTo("Facturas") == 0) {
			
			t = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_FACTURA);
			
		}else if (this.tipoComprobanteSelected.compareTo("Recibos") == 0) {
			
			t = this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_RECIBO);
			
		}
		
		
		
		ReportExcel re = new ReportExcel("TotalCursoReporte");		
		//CursoVigente cv = this.reg.getObjectById(CursoVigente.class.getName(), cursoVigenteid);
		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);
		
		List<String[]> titulos = new ArrayList<String[]>();
		
		SimpleDateFormat sdfRango = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		String[] t1 = {empresa.getNombreFantasia()};
		String[] t2 = {empresa.getExtra2()};
		String[] t3 = {"Sede:",this.getCurrentSede().getSede()};
		String[] t4 = {"REPORTE TOTAL DE CURSOS"};
		String[] t5 = null;
		String[] t6 = null;
		if (fechaInicio != null) {
			
			t5 = new String [] {"Fecha Inicio:", sdfRango.format(fechaInicio)};
			t6 = new String [] {"Fecha Fin:", sdfRango.format(fechaFin)};
			
		}
		
		String[] espacioBlanco = {""};
		
		titulos.add(t1);
		titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		
		if (fechaInicio != null) {
		
			titulos.add(t5);
			titulos.add(t6);
			
		}
		titulos.add(espacioBlanco);
		
		List<String[]> headersDatos = new ArrayList<String[]>();
		String [] hd1 =  {"Fecha","Tipo Comprobante","Factura Nro", "Nombre Apellido","Descripcion" ,"Monto","Descuento"};
		headersDatos.add(hd1);
		
		
		
		String sql1 =  this.um.getSql("reporte/totalCurso.sql").replace("?1", cvis).replace("?5", this.getCurrentSede().getSedeid()+"");
		
		if (t != null) {
			
			sql1 = sql1.replace("--2", "").replace("?2", t.getTipoid()+"");
		}
		
		if (this.fechaInicio != null && this.getFechaFin() != null) {
			
			this.fechaInicio = this.um.modificarHorasMinutosSegundos(fechaInicio, 0, 0, 0, 0);
			this.fechaFin = this.um.modificarHorasMinutosSegundos(fechaFin, 23, 59, 59, 99);
			
			SimpleDateFormat sdfConsulta = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			sql1 = sql1.replace("--3", "").replace("?3", sdfConsulta.format(fechaInicio)).replace("?4", sdfConsulta.format(fechaFin));
			
		}
		
		System.out.println(sql1);
		
		
		List<Object[]> datos = this.reg.sqlNativo(sql1);
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("es", "ES"));
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0.##",dfs);
		df.setGroupingUsed(true);
		
		Double totalMonto = 0.0 ;
		Double totalDescuento = 0.0;
		Double totalMontoGeneral = 0.0 ;
		Double totalDescuentoGeneral = 0.0 ;
		
		Long cursovigenteid = -1l;
		
		List<Object[]> reporteDet = new ArrayList<>();
		
		
		for (int i = 0 ; i<datos.size() ; i++) {
			
			Object[] o = datos.get(i);
			Object[] det = new Object[7]; 
			
			Long id =  Long.parseLong(o[3].toString());
			
			if (cursovigenteid.longValue() != id.longValue() ) {
				
				cursovigenteid = id;
				
				
				if (i!= 0) {
					
					det = new Object[7];
					det[0] = "Total";
					det[1] = "";
					det[2] = "";
					det[3] = "";
					det[4] = "";
					det[5] = df.format(totalMonto);
					det[6] = df.format(totalDescuento);
					
					reporteDet.add(det);
					reporteDet.add(espacioBlanco);
					
					totalMonto = 0.0 ;
					totalDescuento = 0.0;
					
				}
				
				det = new Object[7];
				det[0] = o[4].toString();
				det[1] = "";
				det[2] = "";
				det[3] = "";
				det[4] = "";
				det[5] = "";
				det[6] = "";
				
				reporteDet.add(det);
				
				
				
				
				det = new Object[7];
				
			}
			
			det[0] = o[1].toString();
			det[1] = o[6].toString();
			det[2] = o[7].toString();
			det[3] = o[9].toString();
			det[4] = o[10].toString();
			det[5] = df.format(Double.parseDouble(o[11].toString()));
			det[6] = df.format(Double.parseDouble(o[12].toString()));
			
			
			totalMonto += Double.parseDouble(o[11].toString());
			totalDescuento += Double.parseDouble(o[12].toString());
			
			totalMontoGeneral += Double.parseDouble(o[11].toString());
			totalDescuentoGeneral += Double.parseDouble(o[12].toString());
			
			
			
			reporteDet.add(det);
						
			
		}
		
		Object[] det = new Object[7]; 
		
		det = new Object[7];
		det[0] = "Total";
		det[1] = "";
		det[2] = "";
		det[3] = "";
		det[4] = "";
		det[5] = df.format(totalMonto);
		det[6] = df.format(totalDescuento);
		
		reporteDet.add(det);
		
		det = new Object[7];
		det[0] = "Total General";
		det[1] = "";
		det[2] = "";
		det[3] = "";
		det[4] = "";
		det[5] = df.format(totalMontoGeneral);
		det[6] = df.format(totalDescuentoGeneral) ;
		reporteDet.add(det);
		
	
		
		re.descargar(titulos, headersDatos, reporteDet);

		
	}

	public boolean isOpCrearTotalCursoReporte() {
		return opCrearTotalCursoReporte;
	}

	public void setOpCrearTotalCursoReporte(boolean opCrearTotalCursoReporte) {
		this.opCrearTotalCursoReporte = opCrearTotalCursoReporte;
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

	public List<String> getListaComprobantes() {
		return listaComprobantes;
	}

	public void setListaComprobantes(List<String> listaComprobantes) {
		this.listaComprobantes = listaComprobantes;
	}

	public ListModelArray<DataModelCursoVigente> getCursosVigentesModel() {
		return cursosVigentesModel;
	}

	public void setCursosVigentesModel(ListModelArray<DataModelCursoVigente> cursosVigentesModel) {
		this.cursosVigentesModel = cursosVigentesModel;
	}

	public String getTipoComprobanteSelected() {
		return tipoComprobanteSelected;
	}

	public void setTipoComprobanteSelected(String tipoComprobanteSelected) {
		this.tipoComprobanteSelected = tipoComprobanteSelected;
	}

}
