package com.instituto.util;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Listbox;

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
	
	protected Sede getCurrentSede() {
		
		List<UsuarioSede> result = this.reg.getAllObjectsByCondicionOrder(UsuarioSede.class.getName(), 
				"usuarioid = "+this.getCurrentUser().getUsuarioid()+" AND activo = true", null);
		
		return result.get(0).getSede();
	}
	

}
