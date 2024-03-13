package com.instituto.inicio;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.zkoss.zk.ui.Executions;

import com.doxacore.util.Register;
import com.instituto.modelo.SifenDocumento;

public class ListenerInicializer implements ServletContextListener {

	private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		scheduler = Executors.newScheduledThreadPool(1);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		scheduler.shutdown();
	}

	public void startScheduledTask() {


			Register reg = new Register();

			// Tarea que se ejecutará cada 5 segundos
			scheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					// Coloca aquí el código que quieres ejecutar
					

					if (Executions.getCurrent() != null) {

					System.out.println("HOLAA tarea ejecutada........");

					List<SifenDocumento> lsd = reg.getAllObjectsByCondicionOrder(SifenDocumento.class.getName(),
							"enviado = false", null);

					System.out.println("El tamaño de lsd es = " + lsd.size());
					

					} else {

						System.out.println("es null");
					}

				}
			}, 0, 5, TimeUnit.SECONDS);


	}

}
