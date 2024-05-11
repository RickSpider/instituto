package com.instituto.sistema.reporte;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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



import com.doxacore.report.ReportExcel;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.CursoVigente;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class EstadoCuentaReporteVM extends TemplateViewModelLocal {

	private boolean opCrearEstadoCuentaReporte;
	private CursoVigente cursoVigenteSelected;
	private Alumno alumnoSelected;
	private Date fechaInicio = new Date();
	private Date fechaFin = new Date();
	private List<String> listaRegistros = new ArrayList<String>();
	private String tipoRegistroSelected ="Todos";

	@Init(superclass = true)
	public void initEstadoCuentaReporteVM() {
		
		listaRegistros.add("Todos");
		listaRegistros.add("Pagados");
		listaRegistros.add("Vencidos");

	}

	@AfterCompose(superclass = true)
	public void afterComposeEstadoCuentaReporteVM() {

	}

	@Override
	protected void inicializarOperaciones() {

		this.opCrearEstadoCuentaReporte = this.operacionHabilitada(ParamsLocal.OP_CREAR_ESTADOCUENTAREPORTE);

	}

	// Seccion Buscar alumno

	// seccion buscador

	private List<Object[]> lAlumnosbuscarOri;
	private List<Object[]> lAlumnosBuscar;
	private String buscarAlumno = "";

	@Command
	@NotifyChange("lAlumnosBuscar")
	public void filtrarAlumnoBuscar() {

		this.lAlumnosBuscar = this.filtrarListaObject(buscarAlumno, this.lAlumnosbuscarOri);

	}

	@Command
	@NotifyChange("*")
	public void generarListaBuscarAlumno() {

		this.buscarAlumno = "";
		this.buscarCursoVigente = "";

		String sqlBuscarAlumno = this.um.getSql("buscarAlumno.sql").replace("?1",
				this.getCurrentSede().getSedeid() + "");

		this.lAlumnosBuscar = this.reg.sqlNativo(sqlBuscarAlumno);
		this.lAlumnosbuscarOri = this.lAlumnosBuscar;
	}

	@Command
	@NotifyChange("*")
	public void onSelectAlumno(@BindingParam("id") long id) {

		this.alumnoSelected = this.reg.getObjectById(Alumno.class.getName(), id);
		this.buscarAlumno = alumnoSelected.getFullNombre();
		this.cursoVigenteSelected = null;
		this.buscarCursoVigente = "";

	}

	// fin alumno buscador

	// buscar curso vigente

	private List<Object[]> lCursosVigentesbuscarOri;
	private List<Object[]> lCursosVigentesBuscar;
	private String buscarCursoVigente = "";

	@Command
	@NotifyChange("lCursosVigentesBuscar")
	public void filtrarCursoVigenteBuscar() {

		this.lCursosVigentesBuscar = this.filtrarListaObject(buscarCursoVigente, this.lCursosVigentesbuscarOri);

	}

	@Command
	@NotifyChange({ "lCursosVigentesBuscar", "buscarCursoVigente" })
	public void generarListaBuscarCursoVigente() {

		this.buscarCursoVigente = "";

		String sqlBuscarCursoVigente = "";

		if (this.alumnoSelected == null) {

			sqlBuscarCursoVigente = this.um.getSql("buscarCursoVigente.sql").replace("?1", this.getCurrentSede().getSedeid()+"");

		} else {

			sqlBuscarCursoVigente = this.um.getSql("buscarCursoVigentePorAlumno.sql").replace("?1",
					this.alumnoSelected.getAlumnoid() + "");

		}

		this.lCursosVigentesBuscar = this.reg.sqlNativo(sqlBuscarCursoVigente);
		this.lCursosVigentesbuscarOri = this.lCursosVigentesBuscar;
	}

	@Command
	@NotifyChange({ "buscarCursoVigente", "lEstadosCuentas", "cursoVigenteSelected" })
	public void onSelectCursoVigente(@BindingParam("id") long id) {

		this.cursoVigenteSelected = this.reg.getObjectById(CursoVigente.class.getName(), id);
		this.buscarCursoVigente = cursoVigenteSelected.getCurso().getCurso();

	}
	
	
	//Reporte
	
	public void generarReporte() {
		
		/*if (!this.opCrearEstadoCuentaReporte) {
			
			this.mensajeError("No tines Permiso para generar este reporte.");
			
		}
		
		if (this.cursoVigenteSelected == null && this.alumnoSelected == null) {
			
			this.mensajeInfo("Debes de selecionar un alumno, un curso, o ambos.");
			
			return;
			
		}*/
		
		ReportExcel re = new ReportExcel("EstadoDeCuenta");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		//CursoVigente cv = this.reg.getObjectById(CursoVigente.class.getName(), cursoVigenteid);
		
		List<String[]> titulos = new ArrayList<String[]>();
		
		String[] t1 = {"INSTITUTO SANTO TOMAS"};
		String[] t2 = {"Resolucion M.E.C. Nº 841/98"};
		String[] t3 = {"Sede:",this.getCurrentSede().getSede()};
		String[] t4 = {"REPORTE DE ESTADO DE CUENTA"};
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
		String [] hd1 =  {"Curso","Alumno","Fecha Vencimiento", "Fecha Pago", "Descripcion", "Importe Debito", "Credito", "Saldo"};
		headersDatos.add(hd1);
		
		
		String sql1 =  this.um.getSql("estadoCuentaReporte/estadoCuentaByCursoVigenteReporte.sql").replace("?2", sdf.format(fechaInicio) ).replace("?3", sdf.format(fechaFin));
		
		
		
		
		String sql2 = this.um.getSql("estadoCuentaReporte/cobranzaDetalleByCursoVigenteReporte.sql").replace("?2", sdf.format(fechaInicio) ).replace("?3", sdf.format(fechaFin));

		if (this.cursoVigenteSelected != null) {
			
			sql1 = sql1.replace("?1", this.cursoVigenteSelected.getCursovigenteid()+"").replace("--1", "");
			sql2 = sql2.replace("?1", this.cursoVigenteSelected.getCursovigenteid()+"").replace("--1", "");
			
			
			
		}
		
		if (this.alumnoSelected != null) {
			
			sql1 = sql1.replace("?4", this.alumnoSelected.getAlumnoid()+"").replace("--2", "");
			sql2 = sql2.replace("?4", this.alumnoSelected.getAlumnoid()+"").replace("--2", "");
			
		}
		
		if (this.tipoRegistroSelected.compareTo("Pagados") == 0) {
			
			sql1 = sql1.replace("--3", "");
			sql2 = sql2.replace("--3", "");
			
		}
		
		if (this.tipoRegistroSelected.compareTo("Vencidos") == 0) {
			
			sql1 = sql1.replace("--4", "");
			sql2 = sql2.replace("--4", "");
		}
		
		
		List<Object[]> datos = this.reg.sqlNativo(sql1);
		List<Object[]> datos2 = this.reg.sqlNativo(sql2);
		
		List<Object[]> datos3 = new ArrayList<>();
		
		String alumno = "";
		String curso = "";
		
		double totalCredito = 0;
		double totalDebito = 0;
		double totalGralCredito = 0;
		double totalGralDebito = 0;
		
		//String doubleFormato = "%,.2f";
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("es", "ES"));
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0.##",dfs);
		
		for (Object[] x : datos) {

			
			if (curso.compareTo(x[6].toString()) != 0) {
				
				curso = x[6].toString();
				
				Object[] c = {curso};
				
				if (totalDebito > 0) {
					Object[] total = {"Total:","","","","",df.format(totalDebito),df.format(totalCredito),df.format((totalDebito - totalCredito))};			
					datos3.add(total);
					datos3.add(espacioBlanco);
				}
				
				
				
				datos3.add(c);
				
				totalDebito =0; 
				totalCredito = 0;
				
				
			}
			
			if (alumno.compareTo(x[5].toString()) != 0) {
				
				alumno = x[5].toString();
				
				Object[] a = {"",alumno};
				
				if (totalDebito > 0) {
					Object[] total = {"Total:","","","","",df.format(totalDebito),df.format(totalCredito),df.format((totalDebito - totalCredito))};			
					datos3.add(total);
					datos3.add(espacioBlanco);
				}
				
				datos3.add(a);
		
				totalDebito = 0;
				totalCredito = 0;

				
			}
			

			totalGralDebito += Double.parseDouble(x[2].toString()); 
			totalDebito += Double.parseDouble(x[2].toString()); 
			double saldo =  Double.parseDouble(x[2].toString()); 
			
			Object[] o = new Object[8];
			
			o[0] = "";
			o[1] = "";
			o[2] = x[0];
			o[3] = "";
			o[4] = x[1];
			o[5] = df.format(Double.parseDouble(x[2].toString()));
			o[6] = "";
			o[7] = "";
			
			datos3.add(o);
			
			
			
			for (Object[] x2 : datos2) {

				
				if (Long.parseLong(x[3].toString()) == Long.parseLong(x2[3].toString()) ) {
					
					
					Object[] o2 = new Object[8];
					
					o2[0] = "";
					o2[1] = "";
					o2[2] = "";
					o2[3] = x2[0];
					o2[4] = x2[1];
					o2[5] = "";
					
					saldo -= Double.parseDouble(x2[2].toString());
					
					o2[6] = df.format(Double.parseDouble(x2[2].toString()));
					o2[7] = df.format(saldo);
					
					datos3.add(o2);
					
					
					
					totalGralCredito += Double.parseDouble(x2[2].toString()); 
					totalCredito += Double.parseDouble(x2[2].toString());
				}
				
				
			}
			
			
		
			
			
			
		}
		
		Object[] total = {"Total:","","","","",df.format(totalDebito),df.format(totalCredito),df.format((totalDebito - totalCredito))};			
		datos3.add(total);
		datos3.add(espacioBlanco);
		
		Object[] totales = {"Total General:","","","","",df.format(totalGralDebito),df.format(totalGralCredito),df.format((totalGralDebito - totalGralCredito))};
				
		datos3.add(totales);
		
		re.descargar(titulos, headersDatos, datos3);
		
	}

	public boolean isOpCrearEstadoCuentaReporte() {
		return opCrearEstadoCuentaReporte;
	}

	public void setOpCrearEstadoCuentaReporte(boolean opCrearEstadoCuentaReporte) {
		this.opCrearEstadoCuentaReporte = opCrearEstadoCuentaReporte;
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

	public List<Object[]> getlCursosVigentesBuscar() {
		return lCursosVigentesBuscar;
	}

	public void setlCursosVigentesBuscar(List<Object[]> lCursosVigentesBuscar) {
		this.lCursosVigentesBuscar = lCursosVigentesBuscar;
	}

	public String getBuscarCursoVigente() {
		return buscarCursoVigente;
	}

	public void setBuscarCursoVigente(String buscarCursoVigente) {
		this.buscarCursoVigente = buscarCursoVigente;
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


}
