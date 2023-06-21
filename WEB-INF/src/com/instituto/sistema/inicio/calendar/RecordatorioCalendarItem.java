package com.instituto.sistema.inicio.calendar;

import org.zkoss.calendar.impl.SimpleCalendarItem;

public class RecordatorioCalendarItem extends SimpleCalendarItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -753561305531419667L;
	
	private Long recordatorioid;
	private Long cursovigenteid;
	

	public Long getRecordatorioid() {
		return recordatorioid;
	}

	public void setRecordatorioid(Long recordatorioid) {
		this.recordatorioid = recordatorioid;
	}

	public Long getCursovigenteid() {
		return cursovigenteid;
	}

	public void setCursovigenteid(Long cursovigenteid) {
		this.cursovigenteid = cursovigenteid;
	}

	

}
