package com.instituto.inicio;

import java.util.List;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

import com.doxacore.util.HibernateUtil;
import com.doxacore.util.Register;
import com.doxacore.util.SystemInfo;
import com.instituto.modelo.SifenDocumento;


public class InstitutoInit implements WebAppInit{

	
	
	
	@Override
	public void init(WebApp webApp) throws Exception {
		
		System.out.println("Insituto Web App inito wey!!!!.....");	
		
		System.out.println("SyetemInfo = "+SystemInfo.SISTEMA_PATH_ABSOLUTO);
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		System.out.println(HibernateUtil.getSessionFactory().getCurrentSession().getProperties().toString());
		
		//Register reg = new Register();
		
		System.out.println("antes del getAll");
		
	/*	List<SifenDocumento> lsd = reg.getAllObjectsByCondicionOrder(SifenDocumento.class.getName(),
				"enviado = false", null);*/
		
		List<SifenDocumento> lsd = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from " + SifenDocumento.class.getName() ).list();

		System.out.println("El tamaño de lsd es = " + lsd.size());
		
	
		
		//new ListenerInicializer().startScheduledTask();
		
	}

	
	
}
