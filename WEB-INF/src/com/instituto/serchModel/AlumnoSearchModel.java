package com.instituto.serchModel;

public class AlumnoSearchModel {
	
	private long id;
	private String fullNombre;
	private String documento;
	
	
	
	public AlumnoSearchModel(long id, String fullNombre, String documento) {
		super();
		this.id = id;
		this.fullNombre = fullNombre;
		this.documento = documento;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFullNombre() {
		return fullNombre;
	}
	public void setFullNombre(String fullNombre) {
		this.fullNombre = fullNombre;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@Override
	public String toString() {
		return this.documento+" - "+this.fullNombre;
	}
	
	
	

}
