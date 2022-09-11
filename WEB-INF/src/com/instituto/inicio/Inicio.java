package com.instituto.inicio;

import java.util.List;

import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;

import com.doxacore.login.UsuarioCredencial;
import com.doxacore.modelo.Usuario;
import com.doxacore.util.Register;
import com.doxacore.util.SystemInfo;
import com.instituto.modelo.UsuarioSede;

public class Inicio {
	
	public void beforeLogin() {
		
	}

	public void afterLogin() {
		
		Register reg = new Register();
		
		UsuarioCredencial usuarioCredencial = (UsuarioCredencial) Sessions.getCurrent().getAttribute("userCredential");

		Usuario currentUser = reg.getObjectByColumnString(Usuario.class.getName(), "account",
				usuarioCredencial.getAccount());
		
		List<UsuarioSede> result = reg.getAllObjectsByCondicionOrder(UsuarioSede.class.getName(), 
				"usuarioid = "+currentUser.getUsuarioid()+" AND activo = true", null);
		
		usuarioCredencial.setExtra( result.get(0).getSede().getSede());
		
		Sessions.getCurrent().setAttribute("userCredential", usuarioCredencial);
		
	}

}
