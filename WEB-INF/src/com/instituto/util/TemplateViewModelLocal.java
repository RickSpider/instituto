package com.instituto.util;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import com.doxacore.TemplateViewModel;
import com.instituto.modelo.Sede;
import com.instituto.modelo.UsuarioSede;

public abstract class TemplateViewModelLocal extends TemplateViewModel{
	
	@Init(superclass = true)
	public void initTemplateViewModelLocal() {
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeTemplateViewModelLocal() {
		
	}
	
	protected UsuarioSede getCurrentUsuarioSede() {
		
		return this.reg.getObjectByCondicion(UsuarioSede.class.getName(), 
				"usuarioid = "+this.getCurrentUser().getUsuarioid()+" AND activo = true");
		
		
	}
	
	protected Sede getCurrentSede() {
		
		/*
		List<UsuarioSede> result = this.reg.getAllObjectsByCondicionOrder(UsuarioSede.class.getName(), 
				"usuarioid = "+this.getCurrentUser().getUsuarioid()+" AND activo = true", null);
		
		return result.get(0).getSede();
		*/
		return getCurrentUsuarioSede().getSede();
	}
	

}
