package com.instituto.sistema.abm;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

import com.doxacore.TemplateViewModel;
import com.doxacore.report.ReportBigExcel;
import com.instituto.modelo.InscriptoOnline;
import com.instituto.util.EmailServiceModoboa;
import com.instituto.util.ParamsLocal;

import net.sf.jasperreports.engine.JRException;

public class InscriptoOnlineVM extends TemplateViewModel {

	private List<Object[]> lInscriptosOnline;
	private List<Object[]> lInscriptosOnlineOri;
	private InscriptoOnline inscriptoOnlineSelected;
	
	private boolean opCrearInscriptoOnline;
	private boolean opEditarInscriptoOnline;
	private boolean opBorrarInscriptoOnline;

	private Date desde;
	private Date hasta;

	@Init(superclass = true)
	public void initInscriptoOnlineVM() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);

		cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		desde = cal.getTime();

		cal.set(cal.get(Calendar.YEAR), 11, 31, 23, 59, 59);
		hasta = cal.getTime();
		
		cargarInscriptosOnline();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeInscriptoOnlineVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearInscriptoOnline = this.operacionHabilitada(ParamsLocal.OP_CREAR_INSCRIPTOONLINE);
		this.opEditarInscriptoOnline = this.operacionHabilitada(ParamsLocal.OP_EDITAR_INSCRIPTOONLINE);
		this.opBorrarInscriptoOnline = this.operacionHabilitada(ParamsLocal.OP_BORRAR_INSCRIPTOONLINE);

	}

	private void cargarInscriptosOnline() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		

		String sqlInscriptoOnline = this.um.getSql("inscriptoOnline/listaInscriptoOnline.sql")
				.replace("?1", sdf.format(desde)).replace("?2", sdf.format(hasta));
		
		this.lInscriptosOnline = this.reg.sqlNativo(sqlInscriptoOnline);
		this.lInscriptosOnlineOri = this.lInscriptosOnline;
	}
	
	@Command
	@NotifyChange("lInscriptosOnline")
	public void onChangeFiltroFechas() {

		this.cargarInscriptosOnline();

	}

	// seccion filtro

	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[14]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lInscriptosOnline")
	public void filtrarInscriptoOnline() {

		this.lInscriptosOnline = this.filtrarListaObject(this.filtroColumns, this.lInscriptosOnlineOri);

	}
	
	private Window modal;

	
	@Command
	public void modalInscriptoOnline(@BindingParam("inscriptoOnlineid") long inscriptoOnlineid) {
		
		this.inscriptoOnlineSelected = this.reg.getObjectById(InscriptoOnline.class.getName(), inscriptoOnlineid);

		modal = (Window) Executions.createComponents("/instituto/zul/abm/inscriptoOnlineModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lInscriptosOnline")
	public void guardar() {
		
		this.save(inscriptoOnlineSelected);

		this.inscriptoOnlineSelected = null;

		this.cargarInscriptosOnline();

		this.modal.detach();
		
		Notification.show("La Inscripcion fue Actualizada.");
	
	}
	
	@Command
	public void borrarInscriptoOnlineConfirmacion(@BindingParam("inscriptoOnlineid") final long inscriptoonlineid) {

		if (!this.opBorrarInscriptoOnline)
			return;

		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					borrarInscriptoOnline(reg.getObjectById(InscriptoOnline.class.getName(), inscriptoonlineid));

				}

			}

		};

		this.mensajeEliminar("Se eliminara la inscripcon. \n Continuar?", event);
	}

	private void borrarInscriptoOnline(InscriptoOnline inscriptoOnline) {

		this.reg.deleteObject(inscriptoOnline);

		this.cargarInscriptosOnline();

		BindUtils.postNotifyChange(null, null, this, "lInscriptosOnline");

	}
	
	
	@Command
	public void exportarExcel() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	
		List<String[]> titulos = new ArrayList<String[]>();
		
		
		String[] t1 = {"Inscriptos Online"};
		String[] t2 = {"Reporte De Inscriptos"};
		String[] t3 = {"Fecha Desde:", sdf.format(this.desde)};
		String[] t4 = {"Fecha Hasta:", sdf.format(this.hasta)};
		String[] espacioBlanco = {""};

		titulos.add(t1);
		titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		titulos.add(espacioBlanco);
		
		List<String[]> headersDatos = new ArrayList<String[]>();
		String [] hd1 =  {"ID","CURSO","DIA","FECHA INSCRIPCION", "NOMBRE COMPLETO", "DOCUMENTO #", "RUC", "RAZON SOCIAL" ,"FAC. OTRO", "TELEFONO", "EMAIL", "TITULO" ,"VERIFICADO","PAGO MATRICULA", "ENCUESTA"};
		headersDatos.add(hd1);
		
		/*String sql = this.um.getSql("comprobanteElectronico/listaComprobantesElectronicos.sql")
				.replace("?1", sdf.format(desde)).replace("?2", sdf.format(hasta))
				.replace("?3", this.contribuyenteSelected.getContribuyenteid() + "");
		
		List<Object[]> datos = this.reg.sqlNativo(sql);*/
		
		List<Object[]> detalles = new ArrayList<>();
		
		for (Object[] ox : this.lInscriptosOnline) {
			
			Object[] o = new Object[15];

			o[0] = ox[0];
			o[1] = ox[1].toString();
			o[2] = ox[2].toString();
			o[3] = ox[3].toString();
			o[4] = ox[4].toString();
			o[5] = ox[5].toString();
			o[6] = "";
			
			if (ox[6] != null) {
				
				o[6] = ox[6].toString();
				
			}
			
			o[7] = "";
			if (ox[7] != null) {
				
				o[7] = ox[7].toString();
				
			}
			
			
			
			o[8] = ox[8].toString();
			
			o[9] = ox[9].toString();
			o[10] = ox[10].toString();
			
			if (ox[11] != null) {
				
				o[11] = ox[11].toString();
				
			}
			
			
			
			o[12] = ox[12].toString();
			o[13] = ox[13].toString();
			
			o[14] = "";
			if (ox[14] != null) {
				o[14] = ox[14].toString();
			}
			detalles.add(o);
		}
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
		ReportBigExcel re = new ReportBigExcel("InscriptosOnline_"+sdf2.format(new Date()));
		re.descargar(titulos, headersDatos, detalles);
	}
	
	@Command
	public void enviarCorreo() {
		
		if (this.inscriptoOnlineSelected.isEmailVerificado()) {
			
			this.mensajeInfo("La inscripcion ya fue verificada.");
			
			return;
			
		}
		
		String host = getSistemaPropiedad("EMAIL_HOST").getValor();
		String user = getSistemaPropiedad("EMAIL_USER").getValor();
		String pass = getSistemaPropiedad("EMAIL_PASS").getValor();
		String body = "Sigue el link para confirmar tu instcripcion: \n"
				+ "https://sistema.imai.edu.py/instituto/noLogin/zul/inscripcionConfirmar.zul?keyTemporal="+this.inscriptoOnlineSelected.getKeyTemporal();

		System.out.println("Inicio de envio de correo");
		new Thread(() -> {
			// Coloca aquí el código de tu tarea asíncrona
			try {
				System.out.println("dentro de la tarea y tray asincrono");
				this.enviarEmailFE(this.inscriptoOnlineSelected.getEmail(), host, user, pass ,body);
			} catch (Exception e) {

				System.out.println(e.getCause());
			}
		}).start();
		
		this.mensajeInfo("Se envio un email de validacion.");

	}
	
	public void enviarEmailFE(String email, String host, String user, String pass, String body) throws KeyManagementException, NoSuchAlgorithmException, IOException, JRException {
		
		System.out.println("==================DENTRO DEL METODOTO DE ENVIO================");
				
		//email="rrgi89@hotmail.com";
		System.out.println("ENVIANDO CORREO A: "+email);

		EmailServiceModoboa esm = new EmailServiceModoboa(host,user,pass);
		
		System.out.println("enviando mensaje");
		
		esm.send(email, "Confirmación de Inscripción", body);
		//esm.test();
		
		System.out.println("mensaje enviado");
		
	}
	
	

	public List<Object[]> getlInscriptosOnline() {
		return lInscriptosOnline;
	}

	public void setlInscriptosOnline(List<Object[]> lInscriptosOnline) {
		this.lInscriptosOnline = lInscriptosOnline;
	}

	public InscriptoOnline getInscriptoOnlineSelected() {
		return inscriptoOnlineSelected;
	}

	public void setInscriptoOnlineSelected(InscriptoOnline inscriptoOnlineSelected) {
		this.inscriptoOnlineSelected = inscriptoOnlineSelected;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public boolean isOpCrearInscriptoOnline() {
		return opCrearInscriptoOnline;
	}

	public void setOpCrearInscriptoOnline(boolean opCrearInscriptoOnline) {
		this.opCrearInscriptoOnline = opCrearInscriptoOnline;
	}

	public boolean isOpEditarInscriptoOnline() {
		return opEditarInscriptoOnline;
	}

	public void setOpEditarInscriptoOnline(boolean opEditarInscriptoOnline) {
		this.opEditarInscriptoOnline = opEditarInscriptoOnline;
	}

	public boolean isOpBorrarInscriptoOnline() {
		return opBorrarInscriptoOnline;
	}

	public void setOpBorrarInscriptoOnline(boolean opBorrarInscriptoOnline) {
		this.opBorrarInscriptoOnline = opBorrarInscriptoOnline;
	}

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	
	
	
}
