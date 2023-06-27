package com.instituto.sistema.inicio.calendar;

import java.util.Calendar;
import java.util.TimeZone;
 
import org.zkoss.calendar.Calendars;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

 


public class CalendarController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Wire
	private Calendars calendars;
	
	@Wire
	private Label mes;
	
	@Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
     }
	

    //control the calendar position
    @Listen("onClick = #today")
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
      
    }
    @Listen("onClick = #next")
    public void gotoNext(){
        calendars.nextPage();
       
    }
    @Listen("onClick = #prev")
    public void gotoPrev(){
        calendars.previousPage();
       
    }
     
    //control page display
    @Listen("onClick = #pageDay")
    public void changeToDay(){
        calendars.setMold("default");
        calendars.setDays(1);
    }
    @Listen("onClick = #pageWeek")
    public void changeToWeek(){
        calendars.setMold("default");
        calendars.setDays(7);
    }
    @Listen("onClick = #pageMonth")
    public void changeToYear(){
        calendars.setMold("month");
    }
     
 /*   //control the filter
    @Listen("onClick = #applyFilter")
    public void applyFilter(){
        calendarModel.setFilterText(filter.getValue());
        calendars.setModel(calendarModel);
    }
    @Listen("onClick = #resetFilter")
    public void resetFilter(){
        filter.setText("");
        calendarModel.setFilterText("");
        calendars.setModel(calendarModel);
    }*/

}
