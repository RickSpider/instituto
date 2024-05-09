package com.instituto.sistema.abm;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Notification;

import com.instituto.modelo.Empresa;
import com.instituto.util.TemplateViewModelLocal;

public class EmpresaVM extends TemplateViewModelLocal {
	
	private Empresa empresaSelected;
	

	@Init(superclass = true)
	public void initEmpresaVM() {

		this.cargarDatos();

	}

	@AfterCompose(superclass = true)
	public void afterComposeEmpresaVM() {

	}
	
	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub
		
	}
	
	public void cargarDatos() {
		
		this.empresaSelected = this.reg.getObjectById(Empresa.class.getName(), 1);
		
		if (this.empresaSelected == null) {
			
			this.empresaSelected = new Empresa();
			
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void guardar() {
		
		this.empresaSelected = this.save(this.empresaSelected);
		Notification.show("Datos Actualizados.");
		
	}

	public Empresa getEmpresaSelected() {
		return empresaSelected;
	}

	public void setEmpresaSelected(Empresa empresaSelected) {
		this.empresaSelected = empresaSelected;
	}
	
	

}
