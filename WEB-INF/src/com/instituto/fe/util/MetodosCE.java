package com.instituto.fe.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.doxacore.util.Control;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.instituto.fe.model.Cheque;
import com.instituto.fe.model.ComprobanteElectronico;
import com.instituto.fe.model.ComprobanteElectronicoDetalle;
import com.instituto.fe.model.CondicionOperacion;
import com.instituto.fe.model.ConsultaCDC;
import com.instituto.fe.model.Contribuyente;
import com.instituto.fe.model.InfoComprasPublicas;
import com.instituto.fe.model.Kude;
import com.instituto.fe.model.Receptor;
import com.instituto.fe.model.Tarjeta;
import com.instituto.fe.model.Timbrado;
import com.instituto.fe.model.TipoPago;
import com.instituto.fe.util.conexionRest.HttpConexion;
import com.instituto.fe.util.conexionRest.ResultRest;
import com.instituto.modelo.Cobranza;
import com.instituto.modelo.CobranzaDetalle;
import com.instituto.modelo.CobranzaDetalleCobro;
import com.instituto.modelo.Sede;
import com.instituto.modelo.SifenDocumento;
import com.instituto.util.ParamsLocal;

public class MetodosCE extends Control {
	
	public static String FACTURA = "/factura";
	public static String EVENTO_CANCELAR_FACTURA = "/evento/cancelarfactura";
	public static String CONSULTA_CDC = "/consultar/comprobante/";

	public SifenDocumento convertAndSend(Long cobranzaid, Long sedeid) {

		Sede sede = this.reg.getObjectById(Sede.class.getName(), sedeid);

		Cobranza cobranza = this.reg.getObjectById(Cobranza.class.getName(), cobranzaid);
		List<CobranzaDetalle> lDetalles = this.reg.getAllObjectsByCondicionOrder(CobranzaDetalle.class.getName(),
				"cobranzaid = " + cobranzaid, null);
		List<CobranzaDetalleCobro> lDetallesCobros = this.reg.getAllObjectsByCondicionOrder(
				CobranzaDetalleCobro.class.getName(), "cobranzaid = " + cobranzaid, null);

		ComprobanteElectronico ce = new ComprobanteElectronico();

		Contribuyente c = new Contribuyente();
		c.setContribuyenteid(Long.parseLong(this.getSistemaPropiedad("FE_ID").getValor()));
		c.setPass(this.getSistemaPropiedad("FE_PASS").getValor());

		ce.setContribuyente(c);

		Timbrado t = new Timbrado();
		t.setTimbrado(cobranza.getTimbrado().toString());

		// comp = this.reg.getObjectByCondicion(Comprobante.class.getName(), "timbrado =
		// "+this.cobranzaSelected.getTimbrado());

		String[] comprobanteNum = cobranza.getComprobanteNum().split("-");
		t.setEstablecimiento(comprobanteNum[0]);
		t.setPuntoExpedicion(comprobanteNum[1]);
		t.setDocumentoNro(comprobanteNum[2]);
		t.setFecIni(cobranza.getComprobanteEmision());

		ce.setTimbrado(t);
		ce.setSucursal(sede.getSede());

		Receptor r = new Receptor();
		
		if (cobranza.getPersona() != null && cobranza.getPersona().isGubernamental()) {
			
			r.setTipoOperacion(3L);
			
			if (cobranza.getModalidad() != null
					&& cobranza.getSecuencia() != null
					&& cobranza.getEntidad() != null
					&& cobranza.getAno() != null
					&& cobranza.getFechaEmisionCP() != null) {
				
				InfoComprasPublicas icp = new InfoComprasPublicas();
				
				icp.setModalidad(cobranza.getModalidad());
				icp.setEntidad(cobranza.getEntidad());
				icp.setAno(cobranza.getAno());
				icp.setSecuencia(cobranza.getSecuencia());
				icp.setFechaEmision(cobranza.getFechaEmisionCP());
				
				ce.setInfComprasPublicas(icp);
				
			}
			
			
			
		}
		
		r.setRazonSocial(cobranza.getRazonSocial());

		if (cobranza.getRuc().contains("-")) {

			String[] ruc = cobranza.getRuc().split("-");

			r.setDocNro(ruc[0]);
			r.setDv(ruc[1]);

		} else {

			r.setTipoDocumento(1L);
			r.setDocNro(cobranza.getRuc());

		}

		ce.setReceptor(r);
		ce.setFecha(cobranza.getFecha());

		CondicionOperacion co = new CondicionOperacion();

		if (cobranza.getCondicionVentaTipo().getSigla().compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CONTADO) == 0) {

			co.setCondicion(1l);
			co.setTiposPagos(new ArrayList<TipoPago>());

			for (CobranzaDetalleCobro x : lDetallesCobros) {

				TipoPago tp = new TipoPago();

				if (x.getCobranzacobrodetallepk().getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_EFECTIVO) == 0) {

					tp.setTipoPagoCodigo(1L);
					tp.setMonto(x.getMonto());

				} else if (x.getCobranzacobrodetallepk().getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_CHEQUE) == 0) {

					tp.setTipoPagoCodigo(2L);
					tp.setMonto(x.getMonto());
					Cheque che = new Cheque(x.getEntidad().getEntidad(), x.getChequeNum());
					tp.setCheque(che);
				} else if (x.getCobranzacobrodetallepk().getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TARJETA_CREDITO) == 0) {

					tp.setTipoPagoCodigo(3L);
					tp.setMonto(x.getMonto());
					Tarjeta tar = new Tarjeta(1l, 1l);
					tp.setTarjeta(tar);

				} else if (x.getCobranzacobrodetallepk().getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TARJETA_DEBITO) == 0) {

					tp.setTipoPagoCodigo(4L);
					tp.setMonto(x.getMonto());
					Tarjeta tar = new Tarjeta(1l, 1l);
					tp.setTarjeta(tar);

				} else if (x.getCobranzacobrodetallepk().getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_TRANSFERENCIA) == 0) {

					tp.setTipoPagoCodigo(5L);
					tp.setMonto(x.getMonto());

				} else if (x.getCobranzacobrodetallepk().getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_GIRO) == 0) {

					tp.setTipoPagoCodigo(6L);
					tp.setMonto(x.getMonto());

				} else if (x.getCobranzacobrodetallepk().getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_BILLETERA) == 0) {

					tp.setTipoPagoCodigo(7L);
					tp.setMonto(x.getMonto());

				} else if (x.getCobranzacobrodetallepk().getFormaPago().getSigla()
						.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_QR) == 0
						|| x.getCobranzacobrodetallepk().getFormaPago().getSigla()
								.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_BOCA_COBRANZA) == 0
						|| x.getCobranzacobrodetallepk().getFormaPago().getSigla()
								.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_ATM) == 0
						|| x.getCobranzacobrodetallepk().getFormaPago().getSigla()
								.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_BANCARIO) == 0
						|| x.getCobranzacobrodetallepk().getFormaPago().getSigla()
								.compareTo(ParamsLocal.SIGLA_FORMA_PAGO_DEPOSITO_EXTERIOR) == 0) {

					tp.setTipoPagoCodigo(21L);
					tp.setMonto(x.getMonto());

				}

				co.getTiposPagos().add(tp);

			}

		} else if (cobranza.getCondicionVentaTipo().getSigla()
				.compareTo(ParamsLocal.SIGLA_CONDICION_VENTA_CREDITO) == 0) {

			co.setCondicion(2l);
			co.setOperacionTipo(1l);

			long diff = cobranza.getFechaCreditoVencimiento().getTime() - cobranza.getFecha().getTime();
			long diferenciaEnDias = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

			co.setPlazoCredito(diferenciaEnDias + " dias");

		}

		ce.setCondicionOperacion(co);

		for (CobranzaDetalle cdx : lDetalles) {

			ComprobanteElectronicoDetalle det = new ComprobanteElectronicoDetalle();

			det.setItemCodigo(cdx.getEstadoCuenta().getEstadocuentaid() + "");
			det.setItemDescripcion(cdx.getDescripcion());
			det.setCantidad(1);
			det.setPrecioUnitario(cdx.getMonto());

			if (cdx.getExento() > 0) {

				det.setAfectacionTributaria(3l);
				det.setProporcionIVA(0);
				det.setTasaIVA(0);

			} else if (cdx.getIva5() > 0) {

				det.setAfectacionTributaria(1l);
				det.setProporcionIVA(100);
				det.setTasaIVA(5);

			} else if (cdx.getIva10() > 0) {

				det.setAfectacionTributaria(1l);
				det.setProporcionIVA(100);
				det.setTasaIVA(10);
			}
			
			if (cdx.getDncpG() != null && cdx.getDncpE() != null) {
				
				det.setDncpG(cdx.getDncpG());
				det.setDncpE(cdx.getDncpE());
				
				
			}

			ce.getDetalles().add(det);

		}

		ce.setTotalComprobante(cobranza.getTotalDetalleCobro());

		SifenDocumento sd = new SifenDocumento();
		sd.setCobranza(cobranza);
		// this.cobranzaSelected.setComprobanteElectronico(true);

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		sd.setJson(gson.toJson(ce));

		sd = this.reg.saveObject(sd, "sys");

		return this.enviarComprobante(sd, MetodosCE.FACTURA);
		
		
	}

	public SifenDocumento enviarComprobante(SifenDocumento sd, String ruta ){

			//System.out.println("Enviando Combprobante");
			
			String link = this.getSistemaPropiedad("FE_HOST").getValor() + ruta;
			
			//System.out.println("este es el link: "+link);
			
			ResultRest rr = enviarJson(link, sd.getJson());
			
			System.out.println("Json enviado: \n"+sd.getJson());
			
			//System.out.println("Esta es la respuesta: "+rr.getCode()+" "+rr.getMensaje());
				
			if (rr == null) {
				
				return null;
				
			}else if (rr.getCode() != 201) {
				
				return null;
				
			}
				
			Kude k = new GsonBuilder().create().fromJson(rr.getMensaje(), Kude.class);
				
			sd.setCdc(k.getCdc());
			sd.setQr(k.getQr());
			sd.setEnviado(true);

			return this.reg.saveObject(sd, "sys");
			
	

	}
	
	public ResultRest enviarJson(String link, String json) {
		
		HttpConexion con = new HttpConexion();
		try {
			ResultRest rr = con.consumirREST(link, HttpConexion.POST, json);
			return rr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return null;
		
	}
	
	public ConsultaCDC consultarCDC(String cdc) {
		
		String link = this.getSistemaPropiedad("FE_HOST").getValor()+MetodosCE.CONSULTA_CDC+cdc;
		
		System.out.println(link);
		
		HttpConexion con = new HttpConexion();
		
		try {
			ResultRest rr = con.consumirREST(link, HttpConexion.GET, null);
			
			Gson gson = new GsonBuilder().create();
			
			ConsultaCDC ccdc = gson.fromJson(rr.getMensaje(), ConsultaCDC.class);
			
			return ccdc;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
	}

}
