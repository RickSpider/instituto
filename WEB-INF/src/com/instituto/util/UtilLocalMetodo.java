package com.instituto.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.doxacore.util.Register;
import com.instituto.modelo.Cobranza;

public class UtilLocalMetodo {
	
	private Register reg;
	
	public UtilLocalMetodo() {
		
		this.reg = new Register();
		
	}

	public String getEmailCobranza(Long id) {
		
		Cobranza c = this.reg.getObjectById(Cobranza.class.getName(), id);
		String email = null;
		
		if (c.getAlumno() != null) {
			
			if (c.getAlumno().getPersonaFacturacion()!=null) {
				
				if (c.getAlumno().getPersonaFacturacion().getEmail() != null) {
					
					email = c.getAlumno().getPersonaFacturacion().getEmail();
					
				}
				
			}else {
				
				if (c.getAlumno().getPersona().getEmail() != null) {
					
					email = c.getAlumno().getPersona().getEmail();
					
				}
				
			}
			
		}else {
			
			if (c.getPersona().getEmail() != null) {
				
				email = c.getPersona().getEmail(); 
				
			}
			
		}
		
		if (email == null) {
			
			return null;
		}
		
		
		String EMAIL_REGEX ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		
		if ( !matcher.matches()) {
			//this.mensajeInfo("Correo electrtronico no compatible.");
			return null;
			
		}

		
		return email;
		
	}
	
}
