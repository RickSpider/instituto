package com.instituto.noLogin;

public class DataModelNacionalidad {
	
	private long id;
	private String gentilicio;

	public DataModelNacionalidad(long id, String gentilicio) {
		super();
		this.id = id;
		this.gentilicio = gentilicio;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGentilicio() {
		return gentilicio;
	}
	public void setGentilicio(String gentilicio) {
		this.gentilicio = gentilicio;
	}
	
	@Override
	public String toString() {
		return this.id+" - "+this.gentilicio;
	}

}
