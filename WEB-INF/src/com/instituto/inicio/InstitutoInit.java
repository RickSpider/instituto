package com.instituto.inicio;

import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

import com.doxacore.util.HibernateUtil;
import com.doxacore.util.Register;
import com.doxacore.util.SystemInfo;
import com.instituto.modelo.SifenDocumento;

public class InstitutoInit implements WebAppInit{

	@Override
	public void init(WebApp arg0) throws Exception {
		
		System.out.println("Insituto Web App inito wey!!!!.....");
		
		//System.out.println(SystemInfo.SISTEMA_PATH_NOMBRE);
		/*
		Register reg = new Register();
		
		if (HibernateUtil.getSessionFactory() != null) {
			
			System.out.println("no es nulo el hibernate");
			
		
			 
			List<SifenDocumento> lsd = reg.getAllObjectsByCondicionOrder(SifenDocumento.class.getName(), "enviado = false", null);
	     	
	     	System.out.println("El tamaño de lsd es = "+lsd.size());
			
		}else {
			
			System.out.println("Hubernate Nulo...");
			
		}
		
		
		*/
		
		new ListenerInicializer().startScheduledTask();
		
	}

	
	
}
