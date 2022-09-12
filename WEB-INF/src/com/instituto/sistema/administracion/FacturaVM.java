package com.instituto.sistema.administracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.doxacore.util.PasarNumerosLetras;
import com.doxacore.report.TemplateReportViewModel;
import com.instituto.modelo.Cobranza;
import com.instituto.util.ParamsLocal;


public class FacturaVM extends TemplateReportViewModel {
	
	@Init(superclass = true)
	public void initFacturaVM() {

		this.source += "facturaReport.jasper";

	}

	@AfterCompose(superclass = true)
	public void afterComposeFacturaVM() {

	}
	
	@Override
	protected String[] cargarColumas() {
		String[] columns = {"concepto", "periodo", "monto", "exento", "iva5", "iva10"};
		
		 return columns;
	}

	@Override
	protected Map<String, Object> cargarParametros() {
		
		Cobranza cobranza = this.reg.getObjectById(Cobranza.class.getName(), this.id);

		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("Fecha", cobranza.getFecha());
		parameters.put("RazonSocial", cobranza.getRazonSocial());
		parameters.put("Ruc", cobranza.getRuc());
		parameters.put("ComprobanteNum", cobranza.getComprobanteNum());
		parameters.put("Total", new Double(cobranza.getTotalDetalle()));
		PasarNumerosLetras pnl = new PasarNumerosLetras();
		parameters.put("TotalLetras",pnl.Convertir(String.valueOf(cobranza.getTotalDetalle()), true));
		
		parameters.put("Timbrado",cobranza.getTimbrado());
		parameters.put("FechaInicio", cobranza.getComprobanteEmision());
		parameters.put("FechaFin", cobranza.getComprobanteVencimiento());
		
		if (cobranza.getCondicionVentaTipo().getSigla().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO)==0) {
			
			parameters.put("Contado", "X");
		}
		
		if (cobranza.getCondicionVentaTipo().getSigla().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CREDITO)==0) {
			
			parameters.put("Credito", "X");
		}
		
		parameters.put("Exenta",cobranza.getExento());
		parameters.put("iva5",cobranza.getIva5());
		parameters.put("iva10",cobranza.getIva10());
		parameters.put("Anulado", cobranza.isAnulado());
		
		
		return parameters;
	}

	@Override
	protected List<Object[]> cargarDatos() {
		String cobranzaDetalle = this.um.getSql("facturaDetalle.sql").replace("?1", this.id+"");
		List<Object[]>datos = this.reg.sqlNativo(cobranzaDetalle);
		
		return datos;
	}

	

}
