package com.instituto.noLogin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import org.zkoss.zk.ui.util.Notification;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.ToServerCommand;
import org.zkoss.zhtml.Messagebox;

import org.zkoss.zul.ListModelArray;

import com.doxacore.TemplateNoLoginViewModel;
import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Pais;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.Empresa;
import com.instituto.modelo.InscriptoOnline;
import com.instituto.modelo.Persona;
import com.instituto.sistema.reporte.DataModelCursoVigente;
import com.instituto.util.EmailServiceModoboa;

import net.sf.jasperreports.engine.JRException;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;


@ToServerCommand("verify")
public class InscripcionOnlineVM  extends TemplateNoLoginViewModel{

	private ListModelArray<DataModelCursoVigente> cursosVigentesModel;
	private ListModelArray<DataModelCiudad> ciudadModel;
	private ListModelArray<DataModelNacionalidad> nacionalidadModel;
	
	private DataModelCursoVigente cursoVigenteSelected;
	private DataModelCiudad ciudadSelected;
	private DataModelNacionalidad nacionalidadSelected;
	

	private InscriptoOnline inscriptoOnlineSelected;

	private boolean disabledEnviar = true;
	private boolean visibleBodyForm = true;

	private Empresa empresa;
	
	@Init(superclass = true)
	public void initInscripcionONlineVM() {
		
		System.out.println("Inscripcion Online Hello...");

		this.inscriptoOnlineSelected = new InscriptoOnline();
		empresa = this.reg.getObjectById(Empresa.class.getName(), 1);
	}

	@AfterCompose(superclass = true)
	public void afterComposeInscripcionOnlineVM() {

		this.cargarLista();

	}

	private void cargarLista() {

		String cursoVigenteSQL = this.um.getSql("inscriptoOnline/buscarCursoVigenteInscripcionOnline.sql").replace("?1", "1");
		String ciudadSQL = this.um.getCoreSql("ciudades.sql");
		String paisSQL = this.um.getCoreSql("pais.sql");

		List<Object[]> lCursosVigentes = this.reg.sqlNativo(cursoVigenteSQL);
		List<Object[]> lCiudades = this.reg.sqlNativo(ciudadSQL);
		List<Object[]> lPaises = this.reg.sqlNativo(paisSQL);

		List<DataModelCursoVigente> lcv = new ArrayList<>();
		List<DataModelCiudad> lc = new ArrayList<>();
		List<DataModelNacionalidad> ln = new ArrayList<>();

		int lcvSize = lCursosVigentes.size();

		for (int i = 0; i < lcvSize; i++) {

			Object[] ocv = lCursosVigentes.get(i);

			lcv.add(new DataModelCursoVigente(Long.parseLong(ocv[0].toString()), ocv[1].toString(), ocv[2].toString(),
					ocv[3].toString(), ocv[4].toString()));

		}
		
		int lcSize = lCiudades.size();
		
		for (int i = 0; i < lcSize; i++) {
			
			Object[] oc = lCiudades.get(i);
			
			lc.add(new DataModelCiudad(Long.parseLong(oc[0].toString()), oc[1].toString(), oc[2].toString()));
			
		}
		
		int lnSize = lPaises.size();
		for(int i = 0 ; i < lnSize; i++) {
			
			Object[] on = lPaises.get(i);
			
			ln.add(new DataModelNacionalidad(Long.parseLong(on[0].toString()), on[2].toString()));
			
		}
		

		cursosVigentesModel = new ListModelArray<>(lcv);
		cursosVigentesModel.setMultiple(false);
		
		ciudadModel = new ListModelArray<>(lc);
		ciudadModel.setMultiple(false);
		
		nacionalidadModel = new ListModelArray<>(ln);
		nacionalidadModel.setMultiple(false);

	}

	/*
	 * @Command public void verificarCaptcha() { if (captchaToken == null ||
	 * captchaToken.isEmpty()) { Messagebox.show("Por favor, complete el captcha.");
	 * return; }
	 * 
	 * try { URL verifyUrl = new
	 * URL("https://www.google.com/recaptcha/api/siteverify"); HttpsURLConnection
	 * conn = (HttpsURLConnection) verifyUrl.openConnection();
	 * conn.setRequestMethod("POST"); conn.setDoOutput(true);
	 * 
	 * String postParams = "secret=" + SECRET_KEY + "&response=" + captchaToken;
	 * conn.getOutputStream().write(postParams.getBytes(StandardCharsets.UTF_8));
	 * 
	 * InputStream is = conn.getInputStream(); String jsonResponse = readStream(is);
	 * 
	 * if (jsonResponse.contains("\"success\": true")) {
	 * Messagebox.show("Captcha correcto");
	 * 
	 * } else { Messagebox.show("Captcha incorrecto"); } } catch (Exception e) {
	 * e.printStackTrace(); Messagebox.show("Error verificando el captcha."); } }
	 */

	private static final String SECRET_KEY = "6LeHmqErAAAAAKQ7h8Xlnsuluod5gv4fPP4fQzDL";

	@Command
	@NotifyChange("disabledEnviar")
	public void verify(@BindingParam("response") String response) {

		System.out.println("Datos recibidos: " + response);

		if (response == null || response.isEmpty()) {
			Messagebox.show("Por favor, complete el captcha.");
			return;
		}

		try {
			URL verifyUrl = new URL("https://www.google.com/recaptcha/api/siteverify");
			HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			String postParams = "secret=" + SECRET_KEY + "&response=" + response;
			conn.getOutputStream().write(postParams.getBytes(StandardCharsets.UTF_8));

			InputStream is = conn.getInputStream();
			String jsonResponse = readStream(is);

			if (jsonResponse.contains("\"success\": true")) {
				// Messagebox.show("Captcha correcto");
				this.disabledEnviar = false;

			} else {
				Notification.show("Captcha incorrecto");
				this.disabledEnviar = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("Error verificando el captcha.");
		}

	}

	private String readStream(InputStream is) throws Exception {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString(StandardCharsets.UTF_8.name());
	}
	
	@Command
	public boolean verificarCampos() {
		
		if (this.inscriptoOnlineSelected.getEmail() == null || this.inscriptoOnlineSelected.getEmail().length() <5 ) {
			
			return false;
		}
		
		if (this.ciudadSelected == null) {
			
			return false;
		}
		
		if (this.nacionalidadSelected == null) {
			
			return false;
			
		}
		
		if (this.inscriptoOnlineSelected.getApellido() == null || this.inscriptoOnlineSelected.getApellido().isEmpty()  ) {
			
			return false;
			
		} 
		
		if (this.inscriptoOnlineSelected.getNombre() == null || this.inscriptoOnlineSelected.getNombre().isEmpty()) {
			
			return false;
		}
		
		if (this.inscriptoOnlineSelected.getCi() == null || this.inscriptoOnlineSelected.getCi().isEmpty()) {
			
			return false;
		}
		
		return true;
	}

	@Command
	@NotifyChange("visibleBodyForm")
	public void guardarIncripcion() {
		
		if(!verificarCampos()) {
			
			Notification.show("Todos los campos con * son obligatorios.");
			
			return;
		}

		if (this.cursoVigenteSelected == null) {

			Notification.show("No hay curso Seleccionado");

			return;

		}

		CursoVigente cv = this.reg.getObjectById(CursoVigente.class.getName(), this.cursoVigenteSelected.getId());
		//cv.setCursovigenteid(this.cursoVigenteSelected.getId());
		
		if (this.inscriptoOnlineSelected.getCiudad() == null) {
			
			Ciudad c = new Ciudad();
			c.setCiudadid(this.ciudadSelected.getId());
			this.inscriptoOnlineSelected.setCiudad(c);
			
		}
		
		if (this.inscriptoOnlineSelected.getPais() == null) {
			
			Pais p = new Pais();
			p.setPaisid(this.nacionalidadSelected.getId());
			this.inscriptoOnlineSelected.setPais(p);
			
		}

		
		this.inscriptoOnlineSelected.setCursoVigente(cv);
		
		if (existeInscrpicion() ) {
			
			Notification.show("Ya Existe una inscripcion para esta persona.");
			return;
			
		}
		
		if (existeEnCursoVigente() ) {
			
			Notification.show("Esta persona ya se encuentra dentro de curso.");
			return;
			
		}
		
		
		
		this.inscriptoOnlineSelected.setKeyTemporal(this.um.getSHA512(this.um.RandomStringGenerator()));

		this.reg.saveObject(this.inscriptoOnlineSelected, "SYS");

		this.visibleBodyForm = false;
		
		this.enviarCorreo(this.inscriptoOnlineSelected.getEmail());

	}
	
	public boolean existeInscrpicion() {
		
		 return this.reg.getObjectByCondicion(
		            InscriptoOnline.class.getName(),
		            "cursovigenteid = " + this.inscriptoOnlineSelected.getCursoVigente().getCursovigenteid() +
		            " and ci = '" + this.inscriptoOnlineSelected.getCi() + "'"
		    ) != null;
	}
	
	public boolean existeEnCursoVigente() {
		
		Persona p = this.reg.getObjectByCondicion(Persona.class.getName(), "documentoNum = '"+this.inscriptoOnlineSelected.getCi()+"'");
		
		if (p == null) {
			return false;
		}
		
		Alumno a = this.reg.getObjectByCondicion(Alumno.class.getName(), 
				"personaid = "+p.getPersonaid()+
				"and sedeid = "+this.inscriptoOnlineSelected.getCursoVigente().getSede().getSedeid());
		
		if (a == null) {
			
			return false;
		}

		return this.reg.getObjectByCondicion(
				CursoVigenteAlumno.class.getName(),
				"cursovigenteid = " + this.inscriptoOnlineSelected.getCursoVigente().getCursovigenteid()+
				"and alumnoid = "+a.getAlumnoid()) != null;
		
	}

	public void enviarCorreo(String email) {

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
				enviarEmailFE(email, host, user, pass ,body);
			} catch (Exception e) {

				System.out.println(e.getCause());
			}
		}).start();

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
	
	private boolean readOnly = false;
	
	@Command
	@NotifyChange("*")
	public void buscarCI() {
		
		Persona p = this.reg.getObjectByColumnString(Persona.class.getName(), "documentonum", this.inscriptoOnlineSelected.getCi());
		
		if (p != null) {
			
			this.inscriptoOnlineSelected.setNombre(p.getNombre());
			this.inscriptoOnlineSelected.setApellido(p.getApellido());
			this.inscriptoOnlineSelected.setEmail(p.getEmail() != null ? p.getEmail() : this.inscriptoOnlineSelected.getEmail());
			this.inscriptoOnlineSelected.setRuc(p.getRuc() != null ? p.getRuc() : this.inscriptoOnlineSelected.getRuc());
			this.inscriptoOnlineSelected.setRazonSocial(p.getRazonSocial() != null ? p.getRazonSocial() : this.inscriptoOnlineSelected.getRazonSocial());
			this.inscriptoOnlineSelected.setDireccion(p.getDireccion() != null ? p.getDireccion() : this.inscriptoOnlineSelected.getDireccion());
			this.inscriptoOnlineSelected.setTelefono(p.getTelefono() != null ? p.getTelefono() : this.inscriptoOnlineSelected.getTelefono());
			this.inscriptoOnlineSelected.setCiudad(p.getCiudad() != null ? p.getCiudad() : this.inscriptoOnlineSelected.getCiudad());
			this.inscriptoOnlineSelected.setPais(p.getNacionalidad() != null ? p.getNacionalidad() : this.inscriptoOnlineSelected.getPais());
			
			this.readOnly = true;
			
			if (this.inscriptoOnlineSelected.getCiudad() != null) {
				
				this.ciudadSelected = new DataModelCiudad(
						this.inscriptoOnlineSelected.getCiudad().getCiudadid(),
						this.inscriptoOnlineSelected.getCiudad().getCiudad(),
						this.inscriptoOnlineSelected.getCiudad().getDepartamento().getDepartamento());
				
			}
			
			if (this.inscriptoOnlineSelected.getPais() != null) {
				
				this.nacionalidadSelected = new DataModelNacionalidad(
						this.inscriptoOnlineSelected.getPais().getPaisid(), 
						this.inscriptoOnlineSelected.getPais().getGentilicio());
				
			}

		}
		
	}
	
	private Media logoFile;
	public Media getLogo() throws IOException {
		
		return this.logoFile = new AImage("logo.png",this.empresa.getLogo());
	}


	public boolean isDisabledEnviar() {
		return disabledEnviar;
	}

	public void setDisabledEnviar(boolean disabledEnviar) {
		this.disabledEnviar = disabledEnviar;
	}

	public ListModelArray<DataModelCursoVigente> getCursosVigentesModel() {
		return cursosVigentesModel;
	}

	public void setCursosVigentesModel(ListModelArray<DataModelCursoVigente> cursosVigentesModel) {
		this.cursosVigentesModel = cursosVigentesModel;
	}

	public InscriptoOnline getInscriptoOnlineSelected() {
		return inscriptoOnlineSelected;
	}

	public void setInscriptoOnlineSelected(InscriptoOnline inscriptoOnlineSelected) {
		this.inscriptoOnlineSelected = inscriptoOnlineSelected;
	}

	public DataModelCursoVigente getCursoVigenteSelected() {
		return cursoVigenteSelected;
	}

	public void setCursoVigenteSelected(DataModelCursoVigente cursoVigenteSelected) {
		this.cursoVigenteSelected = cursoVigenteSelected;
	}

	public boolean isVisibleBodyForm() {
		return visibleBodyForm;
	}

	public void setVisibleBodyForm(boolean visibleBodyForm) {
		this.visibleBodyForm = visibleBodyForm;
	}

	public ListModelArray<DataModelCiudad> getCiudadModel() {
		return ciudadModel;
	}

	public void setCiudadModel(ListModelArray<DataModelCiudad> ciudadModel) {
		this.ciudadModel = ciudadModel;
	}

	public DataModelCiudad getCiudadSelected() {
		return ciudadSelected;
	}

	public void setCiudadSelected(DataModelCiudad ciudadSelected) {
		this.ciudadSelected = ciudadSelected;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public DataModelNacionalidad getNacionalidadSelected() {
		return nacionalidadSelected;
	}

	public void setNacionalidadSelected(DataModelNacionalidad nacionalidadSelected) {
		this.nacionalidadSelected = nacionalidadSelected;
	}

	public ListModelArray<DataModelNacionalidad> getNacionalidadModel() {
		return nacionalidadModel;
	}

	public void setNacionalidadModel(ListModelArray<DataModelNacionalidad> nacionalidadModel) {
		this.nacionalidadModel = nacionalidadModel;
	}

	
	
}
