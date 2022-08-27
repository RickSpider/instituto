package com.instituto.sistema.administracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.doxacore.util.PasarNumerosLetras;
import com.doxacore.report.TemplateReportViewModel;
import com.instituto.modelo.Cobranza;


public class ReciboVM extends TemplateReportViewModel {
	
	@Init(superclass = true)
	public void initRecivoVM() {

		this.source += "reciboReport.jasper";

	}

	@AfterCompose(superclass = true)
	public void afterComposeRecivoVM() {

	}
	
	@Override
	protected String[] cargarColumas() {
		String[] columns = {"concepto", "periodo", "monto"};
		
		 return columns;
	}

	@Override
	protected Map<String, Object> cargarParametros() {
		
		Cobranza cobranza = this.reg.getObjectById(Cobranza.class.getName(), this.id);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("Fecha", String.valueOf(cobranza.getFecha()));
		parameters.put("RazonSocial", cobranza.getRazonSocial());
		parameters.put("Ruc", cobranza.getRuc());
		parameters.put("ComprobanteNum", cobranza.getComprobanteNum());
		parameters.put("Total", new Double(cobranza.getTotalDetalle()));
		PasarNumerosLetras pnl = new PasarNumerosLetras();
		parameters.put("TotalLetras",pnl.Convertir(String.valueOf(cobranza.getTotalDetalle()), true));
		
		return parameters;
	}

	@Override
	protected List<Object[]> cargarDatos() {
		String cobranzaDetalle = this.um.getSql("reciboReporte.sql").replace("?1", this.id+"");
		List<Object[]>datos = this.reg.sqlNativo(cobranzaDetalle);
		
		return datos;
	}

	

}
