package com.instituto.noLogin;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

import com.doxacore.TemplateNoLoginViewModel;
import com.doxacore.modelo.Tipo;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.CursoVigenteConcepto;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.modelo.InscriptoOnline;
import com.instituto.modelo.Persona;
import com.instituto.sistema.administracion.GenerarEstadoCuenta;
import com.instituto.util.ParamsLocal;

public class InscripcionConfirmarVM extends TemplateNoLoginViewModel {
	
	private String mensaje = "...";

	@Init(superclass = true)
	public void initInscripcionConfirmarVM() {
		
		

		System.out.println("Inscripcion Confirmar Hello...");
		
	
	}

	@NotifyChange("*")
	@AfterCompose(superclass = true)
	public void afterComposeInscripcionConfirmarVM() {

		String parametroValor = Executions.getCurrent().getParameter("keyTemporal");
		
		this.verficar(parametroValor);

		
	}
	
	@NotifyChange("*")
	public void verficar(String keyTemporal) {
		
		InscriptoOnline io = this.reg.getObjectByColumnString(InscriptoOnline.class.getName(), "keyTemporal", keyTemporal);
		
		if (io == null) {
			
			this.mensaje = "El link no es valido.";
			return;
		}

		this.mensaje = "Validación Exitosa<br>"
		        + "Se ha inscripto al curso " + io.getCursoVigente().getCurso().getCurso()
		        + ", días " + convertirDias(io.getCursoVigente().getDias()) + "<br>"
		        + "Para finalizar la inscripción le facilitamos cuenta de banco para transferencia<br>"
		        + "<br>"
		        + "BANCO ITAU: 25263612<br>"
		        + "BANCO UENO: 19168758<br>"
		        + "DENOMINACIÓN: INNOVAPY S.R.L.<br>"
		        + "RUC: 80127310-2<br>"
		        + "<br>"
		        + "Luego de realizar el pago, favor enviar el comprobante al e-mail: "
		        + "inscripciones@imai.edu.py o vía WhatsApp al +595981700877<br>"
		        + "¡Muchas gracias!<br>";
		
		io.setKeyTemporal("");
		io.setEmailVerificado(true);
		io = this.cargarAlumno(io);
		
		io = this.reg.saveObject(io, "SYS");
		
		inscribirAlumanoCurso(io);

	}
	
	private String convertirDias(String dias) {
		
		String[] DIAS = {"Domingo","Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};
		
		 String[] valores = dias.split(";");
	        StringBuilder resultado = new StringBuilder();

	        for (int i = 0; i < valores.length && i < DIAS.length; i++) {
	            if ("true".equalsIgnoreCase(valores[i].trim())) {
	                if (resultado.length() > 0) {
	                    resultado.append(" - ");
	                }
	                resultado.append(DIAS[i]);
	            }
	        }

	        return resultado.toString();
		
	}
	
	public InscriptoOnline cargarAlumno(InscriptoOnline io) {
		
		Persona p = this.reg.getObjectByColumnString(Persona.class.getName(), "documentonum", io.getCi());
		
		if (p == null) {
			
			p = new Persona();
			p.setNombre(io.getNombre().trim());
			p.setApellido(io.getApellido().trim());
			p.setDocumentoTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_DOCUMENTO_CI));
			p.setPersonaTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_PERSONA_FISICA));
			p.setDocumentoNum(io.getCi().trim());
			p.setNacionalidad(io.getPais());

			//p = this.reg.saveObject(p, "sys");
			
		}
		
		p.setCiudad(io.getCiudad());
		p.setDireccion(io.getDireccion().trim());
		p.setEmail(io.getEmail().trim());
		p.setTelefono(io.getTelefono().trim());
		
		Persona pFacturacion = null;
		
		if (io.getFacturaTercero()) {
			
			pFacturacion = this.reg.getObjectByColumnString(Persona.class.getName(), "ruc", io.getRuc().trim());
			
			if (pFacturacion == null) {
				
				pFacturacion = new Persona();
				
				pFacturacion.setNombre(io.getRazonSocial().trim());
				pFacturacion.setRuc(io.getRuc().trim());
				pFacturacion.setRazonSocial(io.getRazonSocial().trim());
				pFacturacion.setTelefono(io.getTelefono().trim());
				pFacturacion.setEmail(io.getEmail().trim());
				pFacturacion.setDireccion(io.getDireccion().trim());
				
				String [] ruc = io.getRuc().split("-");
				int rucNum = Integer.valueOf(ruc[0]);
				
				if (rucNum >= 80000000) {
					
					pFacturacion.setPersonaTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_PERSONA_JURIDICA));	
					
				}else {
					
					pFacturacion.setPersonaTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_PERSONA_FISICA));	
					
				}

				pFacturacion = this.reg.saveObject(pFacturacion, "sys");

			}
			
			
		}else {
			
			if (io.getRazonSocial() != null && !io.getRazonSocial().isEmpty()) {
				
				p.setRazonSocial(io.getRazonSocial().trim());
			}
			
			if (io.getRuc() != null && !io.getRuc().isEmpty()) {
				
				p.setRuc(io.getRuc().trim());
			}
			
			
		}
		
		
		
		
		
		p = this.reg.saveObject(p, "sys");
		
		Alumno a = this.reg.getObjectByCondicion(Alumno.class.getName(), "sedeid = "+io.getCursoVigente().getSede().getSedeid()+"and personaid = "+p.getPersonaid());
		
		if (a == null) {
			
			a = new Alumno();
			a.setPersona(p);
			a.setSede(io.getCursoVigente().getSede());
			
			
		}
		
		a.setActivo(true);

		if(pFacturacion != null) {
			
			a.setPersonaFacturacion(pFacturacion);
			
		}
		
		a = this.reg.saveObject(a, "sys");
		
		io.setAlumno(a);
		
		return io;
		
	}
	
	public void inscribirAlumanoCurso(InscriptoOnline io) {
		
		List<CursoVigenteConcepto> lConceptosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteConcepto.class.getName(),
				"cursoVigenteid = " + io.getCursoVigente().getCursovigenteid(), "conceptoid asc");
		
		CursoVigenteAlumno cursoVigenteAlumno = new CursoVigenteAlumno();
		
		cursoVigenteAlumno.setAlumno(io.getAlumno());

		cursoVigenteAlumno.setCursoVigente(io.getCursoVigente());
		
		cursoVigenteAlumno = this.reg.saveObject(cursoVigenteAlumno, "sys");
		
		GenerarEstadoCuenta gec = new GenerarEstadoCuenta();
		List<EstadoCuenta> lec = gec.generarMovimientoAlumno(cursoVigenteAlumno, lConceptosCursosVigentes);
		
		for (EstadoCuenta x : lec) {
			
			this.reg.saveObject(x, "sys");
			
		}
		
	}
	

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	

}
