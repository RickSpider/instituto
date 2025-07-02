package com.instituto.sistema.reporte;

public class DataModelCursoVigente {

	private long id;
	private String nombre;
	private String inicio;
	private String fin;
	
	public DataModelCursoVigente(long id, String nombre, String inicio, String fin) {
		this.id = id;
		this.nombre = nombre;
		this.inicio =  inicio;
		this.fin = fin;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	@Override
	public String toString() {
		
		return this.nombre;
	}
	
}
