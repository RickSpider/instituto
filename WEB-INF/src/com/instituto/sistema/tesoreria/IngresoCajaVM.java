package com.instituto.sistema.tesoreria;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import com.doxacore.components.finder.FinderModel;
import com.instituto.modelo.Alumno;
import com.instituto.modelo.Persona;
import com.instituto.util.TemplateViewModelLocal;

public class IngresoCajaVM extends TemplateViewModelLocal {

	//private List<Object[]> lIngresoCaja;
	private List<IngresoCajaDataModel> lIngresoCaja;
	private Date fechaInicio;
	private Date fechaFin;
	private double totalGeneral = 0;
	
	private Alumno alumnoSelected;
	private Persona personaSelected;
	
	@Init(superclass = true)
	public void initIngresoCajaVM() {
	
		this.fechaInicio = this.um.modificarHorasMinutosSegundos(new Date(), 0,0,0,0);
		this.fechaFin = this.um.modificarHorasMinutosSegundos(this.fechaInicio, 23, 59, 59, 999);
		cargarDatos();
		inicializarFinders();
		
	}

	@AfterCompose(superclass = true)
	public void afterIngresoCajaVM() {

	}
	
	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	@NotifyChange("*")
	public void cargarDatos() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String sql = this.um.getSql("cajaIngreso/ingresoCajaList.sql")
				.replace("?1", sdf.format(this.fechaInicio))
				.replace("?2", sdf.format(this.fechaFin));
		
		if (radioSelected == 1 && this.alumnoSelected != null) {
			
			sql = sql.replace("--1", "").replace("?3", this.alumnoSelected.getAlumnoid()+"");
			
		}
		
		if (radioSelected == 2 && this.personaSelected != null) {
			
			sql = sql.replace("--2", "").replace("?4", this.personaSelected.getPersonaid()+"");
			
		}
	
		this.lIngresoCaja = new ArrayList<IngresoCajaDataModel>();
		List<Object[]> ingresos = this.reg.sqlNativo(sql);
		
		
		
		
		
		if (ingresos == null || ingresos.isEmpty() ) {
			
			
			return;
			
		}
		
		
		IngresoCajaDataModel icm = new IngresoCajaDataModel(ingresos.get(0)[2].toString(), ingresos.get(0)[3].toString());
		
		for (Object[] x : ingresos) {
			
			if (icm.getCuentaNum().compareTo(x[2].toString()) == 0 ) {
				
				icm.getLdata().add(x);
				icm.setTotal(icm.getTotal() + (double)x[0]);
				
				
			}else {
				
				this.lIngresoCaja.add(icm);
				
				icm = new IngresoCajaDataModel(x[2].toString(), x[3].toString());
				icm.getLdata().add(x);
				icm.setTotal(icm.getTotal() + (double)x[0]);
			}
			
			this.totalGeneral += (double)x[0];
		}
		
		this.lIngresoCaja.add(icm);
		
	}
	
	private boolean visibleAP =true;
	private int radioSelected = 1;
	
	@Command
	@NotifyChange("*")
	public void onCheckRadioButton(@BindingParam("radio") int radio ) {
		
		//Notification.show("Radio "+radio);
		
		this.radioSelected = radio;
		
		if (radio == 1) {
			
			visibleAP = true;
			
		}else {
			
			visibleAP = false;
			
		}
		
		
	}
	
	private FinderModel alumnoFinder;
	private FinderModel personaFinder;
	
	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlAlumno = this.um.getSql("buscarAlumno.sql").replace("?1",
				this.getCurrentSede().getSedeid() + "")
				;
		alumnoFinder = new FinderModel("alumno", sqlAlumno);
		
		String sqlPersona = "select personaid as id, "
				+ "case when p.ruc is null or p.ruc like '' then  p.documentonum else p.ruc end as Ruc_CI, "
				+ "case when razonsocial is not null then p.razonsocial else (p.apellido||', '||p.nombre)end as RazonSocial "
				+ "from personas p;\r\n" + "";
		
		personaFinder = new FinderModel("persona", sqlPersona);

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.alumnoFinder.getNameFinder()) == 0) {

			this.alumnoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.alumnoFinder, "listFinder");
			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");
			return;
		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.alumnoFinder.getNameFinder()) == 0) {

			this.alumnoFinder
					.setListFinder(this.filtrarListaObject(filter, this.alumnoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.alumnoFinder, "listFinder");
			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder
					.setListFinder(this.filtrarListaObject(filter, this.personaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");
			return;
		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.alumnoFinder.getNameFinder()) == 0) {

			this.alumnoSelected = this.reg.getObjectById(Alumno.class.getName(), id);
			this.personaSelected = null;
			this.cargarDatos();
			return;
			
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaSelected = this.reg.getObjectById(Persona.class.getName(), id);
			this.alumnoSelected = null;
			this.cargarDatos();
			return;
			
		}
		
	}
	
	

	public List<IngresoCajaDataModel> getlIngresoCaja() {
		return lIngresoCaja;
	}

	public void setlIngresoCaja(List<IngresoCajaDataModel> lIngresoCaja) {
		this.lIngresoCaja = lIngresoCaja;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public double getTotalGeneral() {
		return totalGeneral;
	}

	public void setTotalGeneral(double totalGeneral) {
		this.totalGeneral = totalGeneral;
	}

	public Alumno getAlumnoSelected() {
		return alumnoSelected;
	}

	public void setAlumnoSelected(Alumno alumnoSelected) {
		this.alumnoSelected = alumnoSelected;
	}

	public Persona getPersonaSelected() {
		return personaSelected;
	}

	public void setPersonaSelected(Persona personaSelected) {
		this.personaSelected = personaSelected;
	}

	public boolean isVisibleAP() {
		return visibleAP;
	}

	public void setVisibleAP(boolean visibleAP) {
		this.visibleAP = visibleAP;
	}

	public int getRadioSelected() {
		return radioSelected;
	}

	public void setRadioSelected(int radioSelected) {
		this.radioSelected = radioSelected;
	}

	public FinderModel getAlumnoFinder() {
		return alumnoFinder;
	}

	public void setAlumnoFinder(FinderModel alumnoFinder) {
		this.alumnoFinder = alumnoFinder;
	}

	public FinderModel getPersonaFinder() {
		return personaFinder;
	}

	public void setPersonaFinder(FinderModel personaFinder) {
		this.personaFinder = personaFinder;
	}

	

}
