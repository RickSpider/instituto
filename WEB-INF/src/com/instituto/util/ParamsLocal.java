package com.instituto.util;

import com.doxacore.util.Params;

public class ParamsLocal extends Params {
	
	/*
	 EJEMPLO DE COMO CREAR PARAMETROS 
	 
	 
	public static final String OP_CREAR_? = "Crear?";
	public static final String OP_EDITAR_? = "Editar?";
	public static final String OP_BORRAR_? = "Borrar?";
	
	 */
	
	//SIGLA TIPOTIPOS
	public static final String SIGLA_ESTADO_CIVIL = "ESTADO_CIVIL";
	public static final String SIGLA_DOCUMENTO = "DOCUMENTO";
	public static final String SIGLA_IMPUESTO = "IMPUESTO";
	public static final String SIGLA_IMPUESTO_IVA_10 = "IVA_10";
	public static final String SIGLA_IMPUESTO_IVA_5 = "IVA_5";
	public static final String SIGLA_IMPUESTO_IVA_EXENTO = "IVA_EXENTO";
	public static final String SIGLA_MATERIA = "MATERIA";
	public static final String SIGLA_PERSONA= "PERSONA";
	public static final String SIGLA_ESTADO_CV_MATERIA = "ESTADO_CV_MATERIA";
	public static final String SIGLA_MONEDA="MONEDA";
	public static final String SIGLA_MONEDA_GUARANI="MONEDA_GUARANI";
	public static final String SIGLA_MONEDA_DOLAR="MONEDA_DOLAR";
	public static final String SIGLA_COMPROBANTE = "COMPROBANTE";
	public static final String SIGLA_COMPROBANTE_FACTURA = "COMPROBANTE_FACTURA";
	public static final String SIGLA_COMPROBANTE_RECIBO = "COMPROBANTE_RECIBO";
	public static final String SIGLA_FORMA_PAGO = "FORMA_PAGO";
	public static final String SIGLA_FORMA_PAGO_EFECTIVO="FORMA_PAGO_EFECTIVO";
	public static final String SIGLA_FORMA_PAGO_TRANSFERENCIA="FORMA_PAGO_TRANSFERENCIA";
	public static final String SIGLA_FORMA_PAGO_TARJETA_DEBITO="FORMA_PAGO_TARJETA_DEBITO";
	public static final String SIGLA_FORMA_PAGO_TARJETA_CREDITO="FORMA_PAGO_TARJETA_CREDITO";
	public static final String SIGLA_FORMA_PAGO_GIRO="FORMA_PAGO_GIRO";
	public static final String SIGLA_FORMA_PAGO_QR="FORMA_PAGO_QR";
	public static final String SIGLA_FORMA_PAGO_CHEQUE="FORMA_PAGO_CHEQUE";
	public static final String SIGLA_CONDICION_VENTA="CONDICION_VENTA";
	public static final String SIGLA_CONDICION_VENTA_CONTADO="CONDICION_VENTA_CONTADO";
	public static final String SIGLA_CONDICION_VENTA_CREDITO="CONDICION_VENTA_CREDITO";
	
	
	//Sede
	public static final String OP_CREAR_SEDE = "CrearSede";
	public static final String OP_EDITAR_SEDE = "EditarSede";
	public static final String OP_BORRAR_SEDE = "BorrarSede";
	
	//UsuarioSede
	public static final String OP_AGREGAR_SEDE = "AgregarSede";
	public static final String OP_QUITAR_SEDE = "QuitarSede";
	
	//Persona
	
	public static final String OP_CREAR_PERSONA = "CrearPersona";
	public static final String OP_EDITAR_PERSONA = "EditarPersona";
	public static final String OP_BORRAR_PERSONA = "BorrarPersona";
	public static final String OP_CREAR_PERSONAENTIDAD = "CrearPersonaEntidad";
	public static final String OP_BORRAR_PERSONAENTIDAD = "BorrarPersonaEntidad";
	
	//Alumno
	public static final String OP_CREAR_ALUMNO = "CrearAlumno";
	public static final String OP_EDITAR_ALUMNO = "EditarAlumno";
	public static final String OP_BORRAR_ALUMNO = "BorrarAlumno";
	
	
	//GradoAcademico
	public static final String OP_CREAR_GRADOACADEMICO = "CrearGradoAcademico";
	public static final String OP_EDITAR_GRADOACADEMICO = "EditarGradoAcademico";
	public static final String OP_BORRAR_GRADOACADEMICO = "BorrarGradoAcademico";
	
	//curso
	public static final String OP_CREAR_CURSO = "CrearCurso";
	public static final String OP_EDITAR_CURSO = "EditarCurso";
	public static final String OP_BORRAR_CURSO = "BorrarCurso";
	
	//Materia
	public static final String OP_CREAR_MATERIA = "CrearMateria";
	public static final String OP_EDITAR_MATERIA = "EditarMateria";
	public static final String OP_BORRAR_MATERIA = "BorrarMateria";
	
	//CursoMateria
	public static final String OP_AGREGAR_MATERIA = "AgregarMateria";
	public static final String OP_QUITAR_MATERIA = "QuitarMateria";
	
	//Institucion
	public static final String OP_CREAR_INSTITUCION = "CrearInstitucion";
	public static final String OP_EDITAR_INSTITUCION = "EditarInstitucion";
	public static final String OP_BORRAR_INSTITUCION = "BorrarInstitucion";
	
	//Concepto
	public static final String OP_CREAR_CONCEPTO = "CrearConcepto";
	public static final String OP_EDITAR_CONCEPTO = "EditarConcepto";
	public static final String OP_BORRAR_CONCEPTO = "BorrarConcepto";
	
	//Convenio
	public static final String OP_CREAR_CONVENIO = "CrearConvenio";
	public static final String OP_EDITAR_CONVENIO = "EditarConvenio";
	public static final String OP_BORRAR_CONVENIO = "BorrarConvenio";
	
	
	public static final String OP_AGREGAR_ALUMNO = "AgregarAlumno";
	public static final String OP_QUITAR_ALUMNO = "QuitarAlumno";
	

	public static final String OP_AGREGAR_CONVENIO_CONCEPTO = "AgregarConvenioConcepto";
	public static final String OP_QUITAR_CONVENIO_CONCEPTO = "QuitarConvenioConcepto";
	public static final String OP_EDITAR_CONVENIO_CONCEPTO = "EditarConvenioConcepto";
	
	//CursoConcepto
	public static final String OP_AGREGAR_CURSO_CONCEPTO = "AgregarCursoConcepto";
	public static final String OP_QUITAR_CURSO_CONCEPTO = "QuitarCursoConcepto";
	
	//CursoVigente
	public static final String OP_CREAR_CURSOVIGENTE = "CrearCursoVigente";
	public static final String OP_EDITAR_CURSOVIGENTE = "EditarCursoVigente";
	public static final String OP_BORRAR_CURSOVIGENTE = "BorrarCursoVigente";
	
	public static final String OP_AGREGAR_CURSOVIGENTE_ALUMNO = "AgregarCursoVigenteAlumno";
	public static final String OP_QUITAR_CURSOVIGENTE_ALUMNO = "QuitarCursoVigenteAlumno";
	public static final String OP_EDITAR_CURSOVIGENTE_ALUMNO = "EditarCursoVigenteAlumno";
	
	public static final String OP_AGREGAR_CURSOVIGENTE_CONCEPTO = "AgregarCursoVigenteConcepto";
	public static final String OP_QUITAR_CURSOVIGENTE_CONCEPTO = "QuitarCursoVigenteConcepto";
	public static final String OP_EDITAR_CURSOVIGENTE_CONCEPTO = "EditarCursoVigenteConcepto";
	
	public static final String OP_AGREGAR_CURSOVIGENTE_MATERIA = "AgregarCursoVigenteMateria";
	public static final String OP_QUITAR_CURSOVIGENTE_MATERIA = "QuitarCursoVigenteMateria";
	public static final String OP_EDITAR_CURSOVIGENTE_MATERIA = "EditarCursoVigenteMateria";	

	public static final String OP_AGREGAR_CURSOVIGENTE_CONVENIO = "AgregarCursoVigenteConvenio";
	public static final String OP_QUITAR_CURSOVIGENTE_CONVENIO = "QuitarCursoVigenteConvenio";
	public static final String OP_EDITAR_CURSOVIGENTE_CONVENIO = "EditarCursoVigenteConvenio";
	
	//Cotizacion
	
	public static final String OP_CREAR_COTIZACION = "CrearCotizacion";
	public static final String OP_EDITAR_COTIZACION = "EditarCotizacion";
	public static final String OP_BORRAR_COTIZACION = "BorrarCotizacion";
	
	//Entidad
	public static final String OP_CREAR_ENTIDAD = "CrearEntidad";
	public static final String OP_EDITAR_ENTIDAD = "EditarEntidad";
	public static final String OP_BORRAR_ENTIDAD = "BorrarEntidad";
	
	//Comprobante
	public static final String OP_CREAR_COMPROBANTE = "CrearComprobante";
	public static final String OP_EDITAR_COMPROBANTE = "EditarComprobante";
	public static final String OP_BORRAR_COMPROBANTE = "BorrarComprobante";
	
	//Cobranza
	public static final String OP_CREAR_COBRANZA = "CrearCobranza";
	public static final String OP_EDITAR_COBRANZA = "EditarCobranza";
	public static final String OP_BORRAR_COBRANZA = "BorrarCobranza";
	public static final String OP_ANULAR_COBRANZA = "AnularCobranza";
	public static final String OP_DEFINIR_FECHA_COBRANZA = "DefinirFechaCobranza";
	
	//Cobro
	public static final String OP_ANULAR_COBRO = "AnularCobro";
}
