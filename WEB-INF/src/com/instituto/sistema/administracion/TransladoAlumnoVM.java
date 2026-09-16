package com.instituto.sistema.administracion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;

import com.doxacore.modelo.Tipo;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.Comprobante;
import com.instituto.modelo.CursoVigente;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.CursoVigenteConcepto;
import com.instituto.modelo.EstadoCuenta;
import com.instituto.modelo.Translado;
import com.instituto.modelo.TransladoDetalle;
import com.instituto.modelo.UsuarioSede;
import com.instituto.serchModel.AlumnoSearchModel;
import com.instituto.serchModel.CursoVigenteSearchModel;
import com.instituto.util.ParamsLocal;
import com.instituto.util.TemplateViewModelLocal;

public class TransladoAlumnoVM  extends TemplateViewModelLocal {

	private boolean opCrearTransladoAlumno;
	private AlumnoSearchModel alumnoSMSelected;
	private Translado transladoSelected;
	
	private List<Object[]> lTranslados;
	private List<Object[]> lTransladosOri;
	
	private ListModelArray<AlumnoSearchModel> lAlumnoSearchModel;
	private ListModelArray<CursoVigenteSearchModel> cursosVigentesOriModel;
	private ListModelArray<CursoVigenteSearchModel> cursosVigentesDestinoModel;
	private CursoVigenteSearchModel cvSMOri;
	private CursoVigenteSearchModel cvSMDestino;
	
	
	private List<EstadoCuenta> lEstadosCuentasOri;
	private List<EstadoCuenta> lEstadosCuentasDestino;
	
	private boolean transladarVisible = false;
	
	public double motoTotal;

	@Init(superclass = true)
	public void initTransladoAlumnoVM() {
		
		this.cargarDatos();
		

	}
	
	@NotifyChange("*")
	public void cambiarPantalla() {
		
		this.transladarVisible = !this.transladarVisible;
		this.limpiar();
		
		if (!transladarVisible) {
			cargarDatos();
		}
	}
	
	public void cargarDatos() {
		
		String sql = this.um.getSql("transladoAlumno/listaTransladoAlumno.sql")
				.replace("?1", this.getCurrentSede().getSedeid()+"");

		this.lTranslados = this.reg.sqlNativo(sql);
		this.lTransladosOri = this.lTranslados;
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[7]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
											// modelo sin id

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}
	
	@Command
	@NotifyChange("lEscala")
	public void filtrarEscala() {

		this.lTranslados = this.filtrarListaObject(this.filtroColumns, this.lTransladosOri);

	}
	
	private void limpiar() {
	
		this.transladoSelected = new Translado();
		this.transladoSelected.setComprobanteTipo(this.reg.getObjectBySigla(Tipo.class.getName(), ParamsLocal.SIGLA_COMPROBANTE_TRANSLADO));
		
		this.alumnoSMSelected = null;
		this.cvSMDestino = null;
		this.cvSMOri = null;
		
		this.lEstadosCuentasOri = null;
		this.lEstadosCuentasDestino = null;
		
		BindUtils.postNotifyChange(null, null, this, "*");
		
		
	}

	@AfterCompose(superclass = true)
	public void afterTransladoAlumnoVM() {

		this.generarSearchModel();
		
	}
	
	
	@Override
	protected void inicializarOperaciones() {

		this.opCrearTransladoAlumno = this.operacionHabilitada(ParamsLocal.OP_CREAR_TRANSLADOALUMNO);
		
	}

	public ListModelArray<CursoVigenteSearchModel> getCursosVigentesOriModel() {
		return cursosVigentesOriModel;
	}

	public void setCursosVigentesOriModel(ListModelArray<CursoVigenteSearchModel> cursosVigentesOriModel) {
		this.cursosVigentesOriModel = cursosVigentesOriModel;
	}

	private void generarSearchModel() {
		
		this.lAlumnoSearchModel = crearSearchModel(
		        this.um.getSql("buscarAlumno.sql").replace("?1", this.getCurrentSede().getSedeid()+""),
		        o -> new AlumnoSearchModel(
		        		Long.parseLong(o[0].toString()),
		        		o[2].toString()+", "+o[1].toString(),
		        		o[3].toString()		        		
		        )
		    );

		
	}
	
	private <T> ListModelArray<T> crearSearchModel(String sql, java.util.function.Function<Object[], T> mapper) {
	    List<Object[]> resultados = this.reg.sqlNativo(sql);
	    List<T> lista = new ArrayList<>(resultados.size());

	    for (Object[] fila : resultados) {
	        lista.add(mapper.apply(fila));
	    }

	    ListModelArray<T> modelo = new ListModelArray<>(lista);
	    return modelo;
	}
	
	
	@Command
	@NotifyChange("*")
	public void onSelectedAlumno() {
		
		if (this.alumnoSMSelected == null) {
			
			this.transladoSelected.setAlumno(null);
			this.lEstadosCuentasDestino = null;
			this.lEstadosCuentasOri =  null;
			this.cvSMDestino = null;
			this.cvSMOri = null;
			this.transladoSelected.setMontoTransladado(0);
			return;
		}
		
		this.transladoSelected.setAlumno(this.reg.getObjectById(Alumno.class.getName(), this.alumnoSMSelected.getId()));
		
		this.cargarListaCursoVigenteOri();
		
		
	}
	
	private void cargarListaCursoVigenteOri() {
		
		String cursoVigenteSQL = this.um.getSql("buscarCursoVigentePorAlumno.sql")
				.replace("?1", this.transladoSelected.getAlumno().getAlumnoid()+"")
				.replace("--1", "")
				.replace("?2", "false"); 
		
		List<Object[]> lCursosVigentes = this.reg.sqlNativo(cursoVigenteSQL);
		
		List<CursoVigenteSearchModel> lcv = new ArrayList<>();
		
		int lcvSize = lCursosVigentes.size();
		
		for (int i = 0; i<lcvSize; i++) {
			
			Object [] ocv = lCursosVigentes.get(i);
			
			lcv.add(new CursoVigenteSearchModel(Long.parseLong(ocv[0].toString()), ocv[1].toString(), ocv[2].toString(), ocv[3].toString()));
			
		}

		cursosVigentesOriModel = new ListModelArray<>(lcv);
		
		cursosVigentesOriModel.setMultiple(false);
		
	}
	
	private void cargarListaCursoVigenteDestino() {
		
		String cursoVigenteSQL = this.um.getSql("buscarCursoVigente.sql")
				.replace("?1", this.getCurrentSede().getSedeid()+"")
				.replace("--1", "")
				.replace("?2", "false")
				.replace("--2", "")
				.replace("?3", this.transladoSelected.getCursoVigenteOrigen().getCursovigenteid()+"")
				.replace("--3", ""); 
		
		List<Object[]> lCursosVigentes = this.reg.sqlNativo(cursoVigenteSQL);
		
		List<CursoVigenteSearchModel> lcv = new ArrayList<>();
		
		int lcvSize = lCursosVigentes.size();
		
		for (int i = 0; i<lcvSize; i++) {
			
			Object [] ocv = lCursosVigentes.get(i);
			
			lcv.add(new CursoVigenteSearchModel(Long.parseLong(ocv[0].toString()), ocv[1].toString(), ocv[2].toString(), ocv[3].toString()));
			
		}

		cursosVigentesDestinoModel = new ListModelArray<>(lcv);
		
		cursosVigentesDestinoModel.setMultiple(false);
		
	}
	
	
	@Command
	@NotifyChange("*")
	public void onSelectedCVOri() {
		
		if(this.cvSMOri == null) {
			this.transladoSelected.setCursoVigenteOrigen(null);
			this.lEstadosCuentasOri = null;
			this.lEstadosCuentasDestino = null;
			this.cvSMDestino = null;
			this.transladoSelected.setMontoTransladado(0);
			return;
		}
		
		CursoVigente cv = this.reg.getObjectById(CursoVigente.class.getName(), this.cvSMOri.getId());
		
		this.transladoSelected.setCursoVigenteOrigen(cv);
		
		this.lEstadosCuentasOri = this.reg.getAllObjectsByCondicionOrder(EstadoCuenta.class.getName(),
				"cursoVigenteid = " + cv.getCursovigenteid() + " AND alumnoid = "
						+ this.transladoSelected.getAlumno().getAlumnoid(),
				"vencimiento asc");
		
		this.cargarListaCursoVigenteDestino();
		
		double sum = 0;
		
		for (EstadoCuenta ec : this.lEstadosCuentasOri) {
			
			sum += ec.getPago();
			
		}
		
		this.transladoSelected.setMontoTransladado(sum);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectedCVDestino() {
		
		if (this.cvSMDestino == null) {
			
			this.transladoSelected.setCursoVigenteDestino(null);
			this.lEstadosCuentasDestino = null;
			return;
		}
		
		CursoVigente cv = this.reg.getObjectById(CursoVigente.class.getName(), this.cvSMDestino.getId());
		
		this.transladoSelected.setCursoVigenteDestino(cv);
		
		CursoVigenteAlumno cursoVigenteAlumno = new CursoVigenteAlumno();
		
		cursoVigenteAlumno.setAlumno(this.transladoSelected.getAlumno());

		cursoVigenteAlumno.setCursoVigente(cv);
		
		cursoVigenteAlumno = this.reg.saveObject(cursoVigenteAlumno, "sys");
		
		List<CursoVigenteConcepto> lConceptosCursosVigentes = this.reg.getAllObjectsByCondicionOrder(CursoVigenteConcepto.class.getName(),
				"cursoVigenteid = " + cv.getCursovigenteid(), "conceptoid asc");
	
		GenerarEstadoCuenta gec = new GenerarEstadoCuenta();
		lEstadosCuentasDestino = gec.generarMovimientoAlumno(cursoVigenteAlumno, lConceptosCursosVigentes);
		
		double resto = this.transladoSelected.getMontoTransladado();
		
		for (EstadoCuenta ec : lEstadosCuentasDestino) {
			
			if (resto >= ec.getMonto()) {
				
				ec.setPago(ec.getMonto());
				resto -= ec.getMonto();
			}else {
				
				ec.setPago(resto);
				resto = 0;
			}
			
			if (resto == 0) {
				break;
			}
			
		}
		
	}
	
	private synchronized Object[] getNumeroComprobante() {

		UsuarioSede us = this.getCurrentUsuarioSede();

		StringBuffer numero = new StringBuffer();
		Object[] out = new Object[5];
		
		

		Comprobante comprobante = this.reg.getObjectByCondicion(Comprobante.class.getName(),
				"activo = true " + "AND sedeid = " + us.getSede().getSedeid() + " " + "AND comprobantetipoid = "
						+ this.transladoSelected.getComprobanteTipo().getTipoid() + " " + "AND puntoExpdicion = '"
						+ us.getPuntoExpedicion() + "' " + "AND emision <= current_date "
						+ "AND vencimiento >= current_date " + "AND siguiente <= fin ");

		numero.append(this.getCurrentSede().getEstablecimiento() + "-" + comprobante.getPuntoExpdicion() + "-");

		numero.append(String.format("%07d", comprobante.getSiguiente()));

/*		if (comprobante.getComprobanteTipo().getSigla().compareTo(ParamsLocal.SIGLA_COMPROBANTE_FACTURA) == 0) {
			out[0] = comprobante.getTimbrado();
		}*/

		out[1] = comprobante.getEmision();
		out[2] = comprobante.getVencimiento();
		out[3] = numero;
		
		//envia campo si es electronico
		out[4] = comprobante.isElectronico();

		comprobante.setSiguiente(comprobante.getSiguiente() + 1);

		this.save(comprobante);

		return out;
	}

	@Command
	public void transladoConfirmacion() {
		
		EventListener event = new EventListener() {

			@Override
			public void onEvent(Event evt) throws Exception {

				if (evt.getName().equals(Messagebox.ON_YES)) {

					generarTranslado();

				}

			}
		
		};
		
		this.mensajeSiNo("Se generara el translado del alumno, esto es irreversible. \n Continuar?", "Translado Alumno", event);
		
	}
	
	private void generarTranslado() {
		
		Object[] num = this.getNumeroComprobante();
		
		//this.transladoSelected.setFecha(new Date());
		this.transladoSelected.setTransladoNro(num[3].toString());
		
		this.transladoSelected = this.save(this.transladoSelected);
		
		CursoVigenteAlumno cvaORi = this.reg.getObjectByCondicion(CursoVigenteAlumno.class.getName(),
				"cursovigenteid = "+this.transladoSelected.getCursoVigenteOrigen().getCursovigenteid()+
				"and alumnoid = "+this.transladoSelected.getAlumno().getAlumnoid());
		
		cvaORi.setTranslado(this.transladoSelected);
		cvaORi.setInscripcionAnulada(true);
		
		this.save(cvaORi);
		
		for (EstadoCuenta x : this.lEstadosCuentasOri) {
			
			if (x.getPago() == 0) {
				
				x.setInactivo(true);
				x.setFechaInactivacion(new Date());
				x.setMotivoInactivacion("Translado");
				this.save(x);
				
			}
			
		}
		
		
		CursoVigenteAlumno cvaDest = new CursoVigenteAlumno();
		
		cvaDest.setAlumno(this.transladoSelected.getAlumno());
		cvaDest.setCursoVigente(this.transladoSelected.getCursoVigenteDestino());
		cvaDest.setFechaInscripcion(new Date());	
		
		cvaDest = this.save(cvaDest);
		
		for (EstadoCuenta ec : this.lEstadosCuentasDestino) {
			
			ec = this.save(ec);
			TransladoDetalle td = new TransladoDetalle();
			
			td.setTransladoAlumno(this.transladoSelected);
			td.setEstadoCuenta(ec);
			
			this.save(td);
			
		}
		
		this.mensajeInfo("Translado Generado.");
		
		this.cambiarPantalla();

	}
	
	@Command
	public void onChangePago(@BindingParam("estadoCuenta") EstadoCuenta estadoCuenta) {
		
		double pagoTotal = 0;
		
		for (EstadoCuenta ec : this.lEstadosCuentasDestino) {
			
			pagoTotal += ec.getPago();
			
		}
		
		if(pagoTotal > this.transladoSelected.getMontoTransladado()) {
			
			this.mensajeError("La sumatoria de pagos destino supera el monto a transladar");
			estadoCuenta.setPago(0);
			
		}
		
		BindUtils.postNotifyChange(null, null, this, "*");
		
	}	

	public boolean isOpCrearTransladoAlumno() {
		return opCrearTransladoAlumno;
	}

	public void setOpCrearTransladoAlumno(boolean opCrearTransladoAlumno) {
		this.opCrearTransladoAlumno = opCrearTransladoAlumno;
	}

	public double getMotoTotal() {
		return motoTotal;
	}

	public void setMotoTotal(double motoTotal) {
		this.motoTotal = motoTotal;
	}

	public ListModelArray<AlumnoSearchModel> getlAlumnoSearchModel() {
		return lAlumnoSearchModel;
	}

	public void setlAlumnoSearchModel(ListModelArray<AlumnoSearchModel> lAlumnoSearchModel) {
		this.lAlumnoSearchModel = lAlumnoSearchModel;
	}

	public Translado getTransladoSelected() {
		return transladoSelected;
	}

	public void setTransladoSelected(Translado transladoSelected) {
		this.transladoSelected = transladoSelected;
	}

	public AlumnoSearchModel getAlumnoSMSelected() {
		return alumnoSMSelected;
	}

	public void setAlumnoSMSelected(AlumnoSearchModel alumnoSMSelected) {
		this.alumnoSMSelected = alumnoSMSelected;
	}

	public CursoVigenteSearchModel getCvSMOri() {
		return cvSMOri;
	}

	public void setCvSMOri(CursoVigenteSearchModel cvSMOri) {
		this.cvSMOri = cvSMOri;
	}

	public List<EstadoCuenta> getlEstadosCuentasOri() {
		return lEstadosCuentasOri;
	}

	public void setlEstadosCuentasOri(List<EstadoCuenta> lEstadosCuentasOri) {
		this.lEstadosCuentasOri = lEstadosCuentasOri;
	}

	public ListModelArray<CursoVigenteSearchModel> getCursosVigentesDestinoModel() {
		return cursosVigentesDestinoModel;
	}

	public void setCursosVigentesDestinoModel(ListModelArray<CursoVigenteSearchModel> cursosVigentesDestinoModel) {
		this.cursosVigentesDestinoModel = cursosVigentesDestinoModel;
	}

	public CursoVigenteSearchModel getCvSMDestino() {
		return cvSMDestino;
	}

	public void setCvSMDestino(CursoVigenteSearchModel cvSMDestino) {
		this.cvSMDestino = cvSMDestino;
	}

	public List<EstadoCuenta> getlEstadosCuentasDestino() {
		return lEstadosCuentasDestino;
	}

	public void setlEstadosCuentasDestino(List<EstadoCuenta> lEstadosCuentasDestino) {
		this.lEstadosCuentasDestino = lEstadosCuentasDestino;
	}

	public List<Object[]> getlTranslados() {
		return lTranslados;
	}

	public void setlTranslados(List<Object[]> lTranslados) {
		this.lTranslados = lTranslados;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public boolean isTransladarVisible() {
		return transladarVisible;
	}

	public void setTransladarVisible(boolean transladarVisible) {
		this.transladarVisible = transladarVisible;
	}
	
	

}
