package com.instituto.sistema.inicio;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Window;

import com.instituto.modelo.Recordatorio;
import com.instituto.sistema.inicio.calendar.RecordatorioCalendarItem;
import com.instituto.util.TemplateViewModelLocal;

public class AgendamientoVM extends TemplateViewModelLocal {

	private SimpleCalendarModel calendarModel;

	@Init(superclass = true)
	public void initAgendamientoVM() throws ParseException {
		this.calendarModel = new SimpleCalendarModel();
		this.cargarMaterias();
		this.cargarCumpleaños();
		this.cargarAgendamiento();
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeAgendamientoVM(@ContextParam(ContextType.VIEW) Component view) {

		Selectors.wireEventListeners(view, this);

	}

	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub
		
	}
	
	public void cargarMaterias() {
		
		String sql = "select cv.cursovigenteid, c.curso,m.materia, cvm.fechainicio, cvm.fechafin \r\n" + 
				"from cursosvigentesmaterias cvm\r\n" + 
				"left join cursosvigentes cv on cv.cursovigenteid = cvm.cursovigenteid\r\n" + 
				"left join cursos c on c.cursoid = cv.cursoid\r\n" + 
				"left join materias m on m.materiaid = cvm.materiaid\r\n"+
				"where cvm.fechafin is not null;";
		
		List<Object[]> lMaterias = this.reg.sqlNativo(sql);
		
		if (lMaterias != null && lMaterias.size() > 0) {
			
			for (Object[] x : lMaterias) {
				
				RecordatorioCalendarItem sce = new RecordatorioCalendarItem();
				sce.setCursovigenteid((Long) x[0]);
				
				Date date = (Date) x[4];
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
			
				calendar.set(Calendar.HOUR, 7);
				
				sce.setBeginDate(calendar.getTime());
				calendar.add(Calendar.HOUR,1);
				sce.setEndDate(calendar.getTime());
				
				sce.setTitle("Culminacion");
				sce.setContent(x[1].toString()+"\n\r"+x[2].toString());
				sce.setStyle("background-color: #008000; color: #FFFFFF");
				this.calendarModel.add(sce);
								
			}
			
		}
		
	}
	
	@NotifyChange("calendarModel")
	public void cargarCumpleaños() {
		
		String sql = "select \r\n" + 
				"cva.cursovigenteid, \r\n" + 
				"cva.alumnoid, \r\n" + 
				"p.nombre, \r\n" + 
				"p.apellido, \r\n" + 
				"p.fechanacimiento \r\n" + 
				"from cursosvigentesalumnos cva\r\n" + 
				"left join alumnos a on a.alumnoid = cva.alumnoid \r\n" + 
				"left join personas p on p.personaid = a.personaid\r\n" + 
				"left join cursosvigentes cv on cv.cursovigenteid = cva.cursovigenteid\r\n" + 
				"where fechafin <= current_date\r\n" + 
				"order by cursovigenteid asc, alumnoid asc;";
		
		List<Object[]> lcumpleaños = this.reg.sqlNativo(sql);
		
		if (lcumpleaños != null && lcumpleaños.size()>0) {
			
			for (Object[] x : lcumpleaños) {
				
				RecordatorioCalendarItem sce = new RecordatorioCalendarItem();
			//	Date date = new SimpleDateFormat("yyyy-MM-dd").parse(x[4].toString());
				Date date = (Date) x[4];
				Calendar calendar = Calendar.getInstance();
				int currentYear = calendar.get(Calendar.YEAR);
				calendar.setTime(date);
				calendar.set(Calendar.YEAR, currentYear);
				calendar.set(Calendar.HOUR, 6);
				
				sce.setBeginDate(calendar.getTime());
				calendar.add(Calendar.HOUR,1);
				sce.setEndDate(calendar.getTime());
				
				sce.setContent(x[2].toString()+" "+x[3].toString());
				sce.setTitle("Cumpleaños");
				sce.setStyle("background-color: #ff8000; color: #FFFFFF");
				
				this.calendarModel.add(sce);
				
			} 
			
		}
		
	}
	
	@NotifyChange("calendarModel")
	public void cargarAgendamiento() {
		
		List<Recordatorio> lRecordatorio = this.reg.getAllObjects(Recordatorio.class.getName());

		
		if (lRecordatorio != null && lRecordatorio.size() > 0) {
			
			for (Recordatorio x : lRecordatorio) {
				
				RecordatorioCalendarItem sce = new RecordatorioCalendarItem();
				
				sce.setRecordatorioManual(true);
				sce.setBeginDate(x.getFechaIni());
				sce.setEndDate(x.getFechaFin());
				sce.setRecordatorioid(x.getRecordatorioid());
				sce.setContent(x.getRecordatorio());
				sce.setTitle(x.getTitulo());
				sce.setStyle("background-color: #0000FF; color: #FFFFFF");

				this.calendarModel.add(sce);
			}
			
		}
		
		
		
	/*	RecordatorioCalendarItem sce = new RecordatorioCalendarItem();
		Date fecha = new Date();
		sce.setBeginDate(fecha);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MINUTE,60);
		
		
		sce.setRecordatorioid(1L);
		sce.setEndDate(calendar.getTime());
		
		
		
		sce.setContent("test content");
		sce.setStyle("background-color: #F44336; color: #FFFFFF");
		sce.setHeaderStyle("background-color: #F44336; color: #FFFFFF");
		
		this.calendarModel.add(sce);*/
		
	}
	
	private Window modal;
	private RecordatorioCalendarItem recordatorioItemSelected;
	
	@Listen(CalendarsEvent.ON_ITEM_CREATE + " = #calendars; " + CalendarsEvent.ON_ITEM_EDIT + "  = #calendars")
	public void agendamientoModal(CalendarsEvent event) {

		event.stopClearGhost();
		this.recordatorioItemSelected = (RecordatorioCalendarItem) event.getCalendarItem();
		
		if (recordatorioItemSelected == null) {
			
			this.recordatorioItemSelected = new RecordatorioCalendarItem();
			this.recordatorioItemSelected.setRecordatorioManual(true);
			this.recordatorioItemSelected.setBegin(event.getBeginDate());
			this.recordatorioItemSelected.setEnd(event.getEndDate());
			
		}
	
		modal = (Window) Executions.createComponents("/instituto/zul/inicio/agendamientoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}
	
	@Command
	@NotifyChange("*")
	public void guardar() {
		
		Recordatorio r = new Recordatorio();
		
		if (this.recordatorioItemSelected.isRecordatorioManual() && this.recordatorioItemSelected.getRecordatorioid() != null) {
			
			 r = this.reg.getObjectById(Recordatorio.class.getName(), this.recordatorioItemSelected.getRecordatorioid());
			
		}		
		
		r.setFechaIni(this.recordatorioItemSelected.getBeginDate());
		r.setFechaFin(this.recordatorioItemSelected.getEndDate());
		r.setRecordatorio(this.recordatorioItemSelected.getContent());
		r.setTitulo(this.recordatorioItemSelected.getTitle());
		
		this.save(r);
		
		this.calendarModel = new SimpleCalendarModel();
		this.cargarMaterias();
		this.cargarCumpleaños();
		this.cargarAgendamiento();
		
		this.modal.detach();
		
	}

	public SimpleCalendarModel getCalendarModel() {
		return calendarModel;
	}

	public void setCalendarModel(SimpleCalendarModel calendarModel) {
		this.calendarModel = calendarModel;
	}

	public RecordatorioCalendarItem getRecordatorioItemSelected() {
		return recordatorioItemSelected;
	}

	public void setRecordatorioItemSelected(RecordatorioCalendarItem recordatorioItemSelected) {
		this.recordatorioItemSelected = recordatorioItemSelected;
	}
	


}
