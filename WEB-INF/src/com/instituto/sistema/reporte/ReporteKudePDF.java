package com.instituto.sistema.reporte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.doxacore.report.ReportPDF;
import com.doxacore.util.PasarNumerosLetras;
import com.instituto.fe.util.GenerarQR;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.Empresa;
import com.instituto.modelo.SifenDocumento;
import com.instituto.util.ParamsLocal;

public class ReporteKudePDF extends ReportPDF {
	
	/*
	 * 
	 * Esta clase es para enviod de emails
	 * 
	 */
	
	
	public ReporteKudePDF(Long id, String fileName) {
		super(id, fileName);
		
		this.source += "kude.jasper";
		
	}
	
	@Override
	protected String[] cargarColumas() {
		String[] columns = {"descripcion", "monto", "exento", "iva5", "iva10"};
		
		 return columns;
	}

	@Override
	protected Map<String, Object> cargarParametros() {
		
		Cobranza cobranza = this.reg.getObjectById(Cobranza.class.getName(), this.id);
		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("RucEmpresa", empresa.getRuc());
		parameters.put("RazonSocialEmpresa", empresa.getRazonSocial());
		parameters.put("NombreFantaciaEmpresa", empresa.getNombreFantasia());
		parameters.put("DireccionEmpresa", empresa.getDireccion());
		parameters.put("TelefonoEmpresa", empresa.getTelefono());
		parameters.put("CiudadPais", empresa.getExtra1());
		
		parameters.put("Fecha", cobranza.getFecha());
		parameters.put("RazonSocial", cobranza.getRazonSocial());
		
		if (cobranza.getAlumno() != null) {
			
			parameters.put("Alumno", cobranza.getAlumno().getFullNombre());
			
		}		
		
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
		
		parameters.put("iva5liq",cobranza.getIva5()/21);
		parameters.put("iva10liq",cobranza.getIva10()/11);
		
		parameters.put("Anulado", cobranza.isAnulado());
		
		SifenDocumento sd = this.reg.getObjectByCondicion(SifenDocumento.class.getName(), "cobranzaid = "+cobranza.getCobranzaid());
		
		parameters.put("cdc", sd.getCdc());
		
		GenerarQR qr= new GenerarQR();
		
		parameters.put("qr", qr.createQR(sd.getQr(), "UTF-8", 300, 300));
			
		
		return parameters;
	}

	@Override
	protected List<Object[]> cargarDatos() {
		String cobranzaDetalle = this.um.getSql("facturaDetalle.sql").replace("?1", this.id+"");
		List<Object[]>datos = this.reg.sqlNativo(cobranzaDetalle);
		
		return datos;
	}

}
