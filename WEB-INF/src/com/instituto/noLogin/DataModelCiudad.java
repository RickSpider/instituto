package com.instituto.noLogin;

public class DataModelCiudad {
	
	private long id;
	private String ciudad;
	private String departamento;
	
	public DataModelCiudad(long id, String ciudad, String departamento) {
		
		this.id = id;
		this.ciudad = ciudad;
		this.departamento = departamento;
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	@Override
	public String toString() {
		return this.departamento+"-"+this.ciudad;
	}
	
	

}
