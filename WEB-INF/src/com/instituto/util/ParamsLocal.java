package com.instituto.util;

import com.doxacore.util.Params;

public class ParamsLocal extends Params {

	/*
	 * EJEMPLO DE COMO CREAR PARAMETROS
	 * 
	 * 
	 * public static final String OP_CREAR_? = "Crear?"; public static final String
	 * OP_EDITAR_? = "Editar?"; public static final String OP_BORRAR_? = "Borrar?";
	 * 
	 */

	// SIGLA TIPOTIPOS
	public static final String SIGLA_ESTADO_CIVIL = "ESTADO_CIVIL";
	public static final String SIGLA_DOCUMENTO = "DOCUMENTO";
	public static final String SIGLA_IMPUESTO = "IMPUESTO";
	public static final String SIGLA_IMPUESTO_IVA_10 = "IVA_10";
	public static final String SIGLA_IMPUESTO_IVA_5 = "IVA_5";
	public static final String SIGLA_IMPUESTO_IVA_EXENTO = "IVA_EXENTO";
	public static final String SIGLA_MATERIA = "MATERIA";
	public static final String SIGLA_PERSONA = "PERSONA";
	
	public static final String SIGLA_ESTADO_CV_MATERIA = "ESTADO_CV_MATERIA";
	public static final String SIGLA_ESTAOD_CV_MATERIA_PENDIENTE = "ESTADO_CV_MATERIA_PENDIENTE";
	public static final String SIGLA_ESTAOD_CV_MATERIA_PROCESO = "ESTADO_CV_MATERIA_PROCESO";
	public static final String SIGLA_ESTAOD_CV_MATERIA_FINALIZADO = "ESTADO_CV_MATERIA_FINALIZADO";
	
	public static final String SIGLA_MONEDA = "MONEDA";
	public static final String SIGLA_MONEDA_GUARANI = "MONEDA_GUARANI";
	public static final String SIGLA_MONEDA_DOLAR = "MONEDA_DOLAR";
	
	public static final String SIGLA_COMPROBANTE = "COMPROBANTE";
	public static final String SIGLA_COMPROBANTE_FACTURA = "COMPROBANTE_FACTURA";
	public static final String SIGLA_COMPROBANTE_RECIBO = "COMPROBANTE_RECIBO";
	
	public static final String SIGLA_FORMA_PAGO = "FORMA_PAGO";
	public static final String SIGLA_FORMA_PAGO_EFECTIVO = "FORMA_PAGO_EFECTIVO";
	public static final String SIGLA_FORMA_PAGO_TRANSFERENCIA = "FORMA_PAGO_TRANSFERENCIA";
	public static final String SIGLA_FORMA_PAGO_TARJETA_DEBITO = "FORMA_PAGO_TARJETA_DEBITO";
	public static final String SIGLA_FORMA_PAGO_TARJETA_CREDITO = "FORMA_PAGO_TARJETA_CREDITO";
	public static final String SIGLA_FORMA_PAGO_GIRO = "FORMA_PAGO_GIRO";
	public static final String SIGLA_FORMA_PAGO_QR = "FORMA_PAGO_QR";
	public static final String SIGLA_FORMA_PAGO_CHEQUE = "FORMA_PAGO_CHEQUE";
	public static final String SIGLA_FORMA_PAGO_DIFERIDO = "FORMA_PAGO_DIFERIDO";

	public static final String SIGLA_FORMA_PAGO_DEPOSITO_BANCARIO = "FORMA_PAGO_DEPOSITO_BANCARIO";
	public static final String SIGLA_FORMA_PAGO_DEPOSITO_ATM = "FORMA_PAGO_DEPOSITO_ATM";
	public static final String SIGLA_FORMA_PAGO_DEPOSITO_EXTERIOR = "FORMA_PAGO_DEPOSITO_EXTERIOR";
	public static final String SIGLA_FORMA_PAGO_BOCA_COBRANZA = "FORMA_PAGO_BOCA_COBRANZA";
	public static final String SIGLA_FORMA_PAGO_BILLETERA = "FORMA_PAGO_BILLETERA";

	public static final String SIGLA_CONDICION_VENTA = "CONDICION_VENTA";
	public static final String SIGLA_CONDICION_VENTA_CONTADO = "CONDICION_VENTA_CONTADO";
	public static final String SIGLA_CONDICION_VENTA_CREDITO = "CONDICION_VENTA_CREDITO";

	public static final String SIGLA_TRANSACCION_EGRESO = "TRANSACCION_EGRESO";
	public static final String SIGLA_TRANSACCION_INGRESO = "TRANSACCION_INGRESO";

	public static final String SIGLA_ORDEN_COMPRA = "ORDEN_COMPRA";
	public static final String SIGLA_ORDEN_COMPRA_ACTIVO = "ORDEN_COMPRA_ACTIVO";
	public static final String SIGLA_ORDEN_COMPRA_PAGADO = "ORDEN_COMPRA_PAGADO";
	public static final String SIGLA_ORDEN_COMPRA_CANCELADO = "ORDEN_COMPRA_CANCELADO";
	
	public static final String SIGLA_PROVEEDOR = "PROVEEDOR";
	public static final String SIGLA_PROVEEDOR_PROVEEDOR = "PROVEEDOR_PROVEEDOR";
	public static final String SIGLA_PROVEEDOR_DOCENTE = "PROVEEDOR_DOCENTE";
	
	public static final String SIGLA_EVALUACION = "EVALUACION";
	public static final String SIGLA_EVALUACION_ORDINARIO = "EVALUACION_ORDINARIO";
	public static final String SIGLA_EVALUACION_COMPLEMENTARIO = "EVALUACION_COMPLEMENTARIO";
	public static final String SIGLA_EVALUACION_REGULARIZACION = "EVALUACION_REGULARIZACION";
	

	// Sede
	public static final String OP_CREAR_SEDE = "CrearSede";
	public static final String OP_EDITAR_SEDE = "EditarSede";
	public static final String OP_BORRAR_SEDE = "BorrarSede";

	// UsuarioSede
	public static final String OP_AGREGAR_SEDE = "AgregarSede";
	public static final String OP_QUITAR_SEDE = "QuitarSede";

	// Persona

	public static final String OP_CREAR_PERSONA = "CrearPersona";
	public static final String OP_EDITAR_PERSONA = "EditarPersona";
	public static final String OP_BORRAR_PERSONA = "BorrarPersona";
	public static final String OP_CREAR_PERSONAENTIDAD = "CrearPersonaEntidad";
	public static final String OP_BORRAR_PERSONAENTIDAD = "BorrarPersonaEntidad";

	// Alumno
	public static final String OP_CREAR_ALUMNO = "CrearAlumno";
	public static final String OP_EDITAR_ALUMNO = "EditarAlumno";
	public static final String OP_BORRAR_ALUMNO = "BorrarAlumno";

	// GradoAcademico
	public static final String OP_CREAR_GRADOACADEMICO = "CrearGradoAcademico";
	public static final String OP_EDITAR_GRADOACADEMICO = "EditarGradoAcademico";
	public static final String OP_BORRAR_GRADOACADEMICO = "BorrarGradoAcademico";

	// curso
	public static final String OP_CREAR_CURSO = "CrearCurso";
	public static final String OP_EDITAR_CURSO = "EditarCurso";
	public static final String OP_BORRAR_CURSO = "BorrarCurso";

	// Materia
	public static final String OP_CREAR_MATERIA = "CrearMateria";
	public static final String OP_EDITAR_MATERIA = "EditarMateria";
	public static final String OP_BORRAR_MATERIA = "BorrarMateria";

	// CursoMateria
	public static final String OP_AGREGAR_MATERIA = "AgregarMateria";
	public static final String OP_QUITAR_MATERIA = "QuitarMateria";

	// Institucion
	public static final String OP_CREAR_INSTITUCION = "CrearInstitucion";
	public static final String OP_EDITAR_INSTITUCION = "EditarInstitucion";
	public static final String OP_BORRAR_INSTITUCION = "BorrarInstitucion";

	// Concepto
	public static final String OP_CREAR_CONCEPTO = "CrearConcepto";
	public static final String OP_EDITAR_CONCEPTO = "EditarConcepto";
	public static final String OP_BORRAR_CONCEPTO = "BorrarConcepto";

	// Convenio
	public static final String OP_CREAR_CONVENIO = "CrearConvenio";
	public static final String OP_EDITAR_CONVENIO = "EditarConvenio";
	public static final String OP_BORRAR_CONVENIO = "BorrarConvenio";

	public static final String OP_AGREGAR_ALUMNO = "AgregarAlumno";
	public static final String OP_QUITAR_ALUMNO = "QuitarAlumno";

	public static final String OP_AGREGAR_CONVENIO_CONCEPTO = "AgregarConvenioConcepto";
	public static final String OP_QUITAR_CONVENIO_CONCEPTO = "QuitarConvenioConcepto";
	public static final String OP_EDITAR_CONVENIO_CONCEPTO = "EditarConvenioConcepto";

	// CursoConcepto
	public static final String OP_AGREGAR_CURSO_CONCEPTO = "AgregarCursoConcepto";
	public static final String OP_QUITAR_CURSO_CONCEPTO = "QuitarCursoConcepto";

	// CursoVigente
	public static final String OP_CREAR_CURSOVIGENTE = "CrearCursoVigente";
	public static final String OP_EDITAR_CURSOVIGENTE = "EditarCursoVigente";
	public static final String OP_BORRAR_CURSOVIGENTE = "BorrarCursoVigente";

	public static final String OP_AGREGAR_CURSOVIGENTE_ALUMNO = "AgregarCursoVigenteAlumno";
	public static final String OP_QUITAR_CURSOVIGENTE_ALUMNO = "QuitarCursoVigenteAlumno";
	public static final String OP_EDITAR_CURSOVIGENTE_ALUMNO = "EditarCursoVigenteAlumno";
	public static final String OP_ANULAR_CURSOVIGENTE_ALUMNO = "AnularCursoVigenteAlumno";

	public static final String OP_AGREGAR_CURSOVIGENTE_CONCEPTO = "AgregarCursoVigenteConcepto";
	public static final String OP_QUITAR_CURSOVIGENTE_CONCEPTO = "QuitarCursoVigenteConcepto";
	public static final String OP_EDITAR_CURSOVIGENTE_CONCEPTO = "EditarCursoVigenteConcepto";

	public static final String OP_AGREGAR_CURSOVIGENTE_MATERIA = "AgregarCursoVigenteMateria";
	public static final String OP_QUITAR_CURSOVIGENTE_MATERIA = "QuitarCursoVigenteMateria";
	public static final String OP_EDITAR_CURSOVIGENTE_MATERIA = "EditarCursoVigenteMateria";

	public static final String OP_AGREGAR_CURSOVIGENTE_CONVENIO = "AgregarCursoVigenteConvenio";
	public static final String OP_QUITAR_CURSOVIGENTE_CONVENIO = "QuitarCursoVigenteConvenio";
	public static final String OP_EDITAR_CURSOVIGENTE_CONVENIO = "EditarCursoVigenteConvenio";

	// Cotizacion

	public static final String OP_CREAR_COTIZACION = "CrearCotizacion";
	public static final String OP_EDITAR_COTIZACION = "EditarCotizacion";
	public static final String OP_BORRAR_COTIZACION = "BorrarCotizacion";

	// Entidad
	public static final String OP_CREAR_ENTIDAD = "CrearEntidad";
	public static final String OP_EDITAR_ENTIDAD = "EditarEntidad";
	public static final String OP_BORRAR_ENTIDAD = "BorrarEntidad";

	// Comprobante
	public static final String OP_CREAR_COMPROBANTE = "CrearComprobante";
	public static final String OP_EDITAR_COMPROBANTE = "EditarComprobante";
	public static final String OP_BORRAR_COMPROBANTE = "BorrarComprobante";

	// Cobranza
	public static final String OP_CREAR_COBRANZA = "CrearCobranza";
	public static final String OP_EDITAR_COBRANZA = "EditarCobranza";
	public static final String OP_BORRAR_COBRANZA = "BorrarCobranza";
	public static final String OP_ANULAR_COBRANZA = "AnularCobranza";
	public static final String OP_DEFINIR_FECHA_COBRANZA = "DefinirFechaCobranza";

	// Cobro
	public static final String OP_ANULAR_COBRO = "AnularCobro";

	// Estado Cuenta
	public static final String OP_CREAR_ESTADOCUENTA = "CrearEstadoCuenta";
	public static final String OP_INACTIVAR_ESTADOCUENTA = "InactivarEstadoCuenta";
	public static final String OP_BORRAR_ESTADOCUENTA = "BorrarEstadoCuenta";

	// Estado Cuenta
	public static final String OP_CREAR_ESTADOCUENTAPERSONA = "CrearEstadoCuentaPersona";
	public static final String OP_INACTIVAR_ESTADOCUENTAPERSONA = "InactivarEstadoCuentaPersona";
	public static final String OP_BORRAR_ESTADOCUENTAPERSONA = "BorrarEstadoCuentaPersona";

	// EstadoCuenta
	public static final String OP_CREAR_ESTADOCUENTAREPORTE = "CrearEstadoCuentaReporte";

	// FacturacionReporte
	public static final String OP_CREAR_FACTURACIONREPORTE = "CrearFacturacionReporte";

	// Caja
	public static final String OP_CREAR_CAJA = "CrearCaja";
	public static final String OP_EDITAR_CAJA = "EditarCaja";
	public static final String OP_BORRAR_CAJA = "BorrarCaja";

	// Cuenta
	public static final String OP_CREAR_CUENTA = "CrearCuenta";
	public static final String OP_EDITAR_CUENTA = "EditarCuenta";
	public static final String OP_BORRAR_CUENTA = "BorrarCuenta";

	// Transaccion
	public static final String OP_CREAR_TRANSACCION = "CrearTransaccion";
	public static final String OP_EDITAR_TRANSACCION = "EditarTransaccion";
	public static final String OP_BORRAR_TRANSACCION = "BorrarTransaccion";

	// Transaccion
	public static final String OP_CREAR_SERVICIO = "CrearServicio";
	public static final String OP_EDITAR_SERVICIO = "EditarServicio";
	public static final String OP_BORRAR_SERVICIO = "BorrarServicio";

	// CobranzaServicio
	public static final String OP_CREAR_COBRANZASERVICIO = "CrearCobranzaServicio";
	public static final String OP_DEFINIR_FECHA_COBRANZASERVICIO = "DefinirFechaCobranzaServicio";

	// SifenDocumentos
	public static final String OP_CREAR_SIFENDOCUMENTO = "CrearSifenDocumento";
	public static final String OP_EDITAR_SIFENDOCUMENTO = "EditarSifenDocumento";
	public static final String OP_BORRAR_SIFENDOCUMENTO = "BorrarSifenDocumento";

	// Rubro
	public static final String OP_CREAR_RUBRO = "CrearRubro";
	public static final String OP_EDITAR_RUBRO = "EditarRubro";
	public static final String OP_BORRAR_RUBRO = "BorrarRubro";

	// CuentaRubro
	public static final String OP_CREAR_CUENTARUBRO = "CrearCuentaRubro";
	public static final String OP_EDITAR_CUENTARUBRO = "EditarCuentaRubro";
	public static final String OP_BORRAR_CUENTARUBRO = "BorrarCuentaRubro";

	// Proveedor
	public static final String OP_CREAR_PROVEEDOR = "CrearProveedor";
	public static final String OP_EDITAR_PROVEEDOR = "EditarProveedor";
	public static final String OP_BORRAR_PROVEEDOR = "BorrarProveedor";

	// PResupuesto
	public static final String OP_CREAR_PRESUPUESTO = "CrearPresupuesto";
	public static final String OP_EDITAR_PRESUPUESTO = "EditarPresupuesto";
	public static final String OP_BORRAR_PRESUPUESTO = "BorrarPresupuesto";

	// OrdenCompra
	public static final String OP_CREAR_ORDENCOMPRA = "CrearOrdenCompra";
	public static final String OP_EDITAR_ORDENCOMPRA = "EditarOrdenCompra";
	public static final String OP_BORRAR_ORDENCOMPRA = "BorrarOrdenCompra";
	
	// Pago
	public static final String OP_CREAR_PAGO = "CrearPago";
	public static final String OP_EDITAR_PAGO = "EditarPago";
	public static final String OP_BORRAR_PAGO = "BorrarPago";
	
	// Escala
	
	public static final String OP_CREAR_ESCALA = "CrearEscala";
	public static final String OP_EDITAR_ESCALA = "EditarEscala";
	public static final String OP_BORRAR_ESCALA = "BorrarEscala";
	
	// Evaluacion
	
	public static final String OP_CREAR_EVALUACION = "CrearEvaluacion";
	public static final String OP_EDITAR_EVALUACION = "EditarEvaluacion";
	public static final String OP_BORRAR_EVALUACION = "BorrarEvaluacion";

}
