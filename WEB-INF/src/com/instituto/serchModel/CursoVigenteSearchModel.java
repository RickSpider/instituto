package com.instituto.serchModel;

public class CursoVigenteSearchModel {

	private long id;
	private String nombre;
	private String inicio;
	private String fin;
	private String dias;
	
	public CursoVigenteSearchModel(long id, String nombre, String inicio, String fin) {
		this.id = id;
		this.nombre = nombre;
		this.inicio =  inicio;
		this.fin = fin;
	}
	
	public CursoVigenteSearchModel(long id, String nombre, String inicio, String fin, String dias) {
		this.id = id;
		this.nombre = nombre;
		this.inicio =  inicio;
		this.fin = fin;
		this.dias = convertirDias(dias);
	}
	
	private String convertirDias(String dias) {
		
		String[] DIAS = {"DOMINGO","LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO"};
		
		 String[] valores = dias.split(";");
	        StringBuilder resultado = new StringBuilder();

	        for (int i = 0; i < valores.length && i < DIAS.length; i++) {
	            if ("true".equalsIgnoreCase(valores[i].trim())) {
	                if (resultado.length() > 0) {
	                    resultado.append(" - ");
	                }
	                resultado.append(DIAS[i]);
	            }
	        }

	        return resultado.toString();
		
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

	public String getDias() {
		return dias;
	}

	public void setDias(String dias) {
		this.dias = dias;
	}

	@Override
	public String toString() {
		
		return this.nombre+" - "+this.inicio+" - "+this.fin;
	}
	
}
