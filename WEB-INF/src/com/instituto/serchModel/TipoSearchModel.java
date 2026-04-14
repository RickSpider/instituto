package com.instituto.serchModel;

import com.doxacore.modelo.Tipo;

public class TipoSearchModel {

	private long id;
	private String tipo;
	
	
	public TipoSearchModel(long id, String tipo) {
		this.id = id;
		this.tipo = tipo;
		
	}
	
	public TipoSearchModel(Tipo t) {
		
		this.id = t.getTipoid();
		this.tipo = t.getTipo();
		
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	@Override
	public String toString() {
		return this.tipo;
	}

	

}
