package com.instituto.sistema.administracion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.doxacore.util.PasarNumerosLetras;
import com.doxacore.report.TemplateReportViewModel;
import com.instituto.fe.util.GenerarQR;
import com.instituto.modelo.Empresa;
import com.instituto.modelo.NotaCD;
import com.instituto.modelo.SifenDocumento;



public class KudeNCDReporteVM extends TemplateReportViewModel {
	
	/*
	 * 
	 * 
	 * si acatualizas esta clase anada actualiza reporte/ReporteKudePDF.java
	 * 
	 * 
	 */
	
	@Init(superclass = true)
	public void initFacturaVM() {

		this.source += "kudeNC.jasper";

	}

	@AfterCompose(superclass = true)
	public void afterComposeFacturaVM() {

	}
	
	@Override
	protected String[] cargarColumas() {
		String[] columns = {"descripcion", "monto", "exento", "iva5", "iva10"};
		
		 return columns;
	}

	@Override
	protected Map<String, Object> cargarParametros() {
		
		NotaCD nc = this.reg.getObjectById(NotaCD.class.getName(), this.id);
		
		Empresa empresa = this.reg.getObjectById(Empresa.class.getName(), 1);

		Map<String, Object> parameters = new HashMap<>();
		
		try {
			parameters.put("Logo", ImageIO.read(new ByteArrayInputStream(empresa.getLogo())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		parameters.put("RucEmpresa", empresa.getRuc());
		parameters.put("RazonSocialEmpresa", empresa.getRazonSocial());
		parameters.put("NombreFantaciaEmpresa", empresa.getNombreFantasia());
		parameters.put("DireccionEmpresa", empresa.getDireccion());
		parameters.put("TelefonoEmpresa", empresa.getTelefono());
		parameters.put("CiudadPais", empresa.getExtra1());
		
		
		parameters.put("Fecha", nc.getFecha());
		parameters.put("RazonSocial", nc.getRazonSocial());
		parameters.put("Direccion", nc.getDireccion());
		
		System.out.println("la direccion es : "+nc.getDireccion());
		
		if (nc.getPersona() != null) {
			
			parameters.put("Alumno", nc.getPersona().getFullNombre());
			
		}		
		
		//parameters.put("CreditoVencimiento", nc.getFechaCreditoVencimiento());
		
		parameters.put("Ruc", nc.getRuc());
		parameters.put("ComprobanteNum", nc.getComprobanteNum());
		parameters.put("Total", new Double(nc.getTotalDetalle()));
		PasarNumerosLetras pnl = new PasarNumerosLetras();		
		
		DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
		
		parameters.put("TotalLetras",pnl.Convertir(df.format(nc.getTotalDetalle()), true));
		
		parameters.put("Timbrado",nc.getTimbrado());
		parameters.put("FechaInicio", nc.getComprobanteEmision());
		parameters.put("FechaFin", nc.getComprobanteVencimiento());
		
		parameters.put("FacturaNro", nc.getCobranza().getComprobanteNum());
		parameters.put("CdcAsociado", nc.getSifenDocumento().getCdc());
		
		
		
		/*if (nc.getCondicionVentaTipo().getSigla().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO)==0) {
			
			parameters.put("Contado", "X");
		}
		
		if (nc.getCondicionVentaTipo().getSigla().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CREDITO)==0) {
			
			parameters.put("Credito", "X");
		}*/
		
		parameters.put("Exenta",nc.getExento());
		parameters.put("iva5",nc.getIva5());
		parameters.put("iva10",nc.getIva10());
		
		parameters.put("iva5liq",nc.getIva5()/21);
		parameters.put("iva10liq",nc.getIva10()/11);
		
		parameters.put("Anulado", nc.isAnulado());
		
		SifenDocumento sd = this.reg.getObjectByCondicion(SifenDocumento.class.getName(), "notacdid = "+nc.getNotacdid());
		
		parameters.put("cdc", sd.getCdc());
		
		GenerarQR qr= new GenerarQR();
		
		parameters.put("qr", qr.createQR(sd.getQr(), "UTF-8", 300, 300));
			
		
		return parameters;
	}

	@Override
	protected List<Object[]> cargarDatos() {
		String notacdDetalle = this.um.getSql("notacdDetalle.sql").replace("?1", this.id+"");
		List<Object[]>datos = this.reg.sqlNativo(notacdDetalle);
		
		return datos;
	}

}
