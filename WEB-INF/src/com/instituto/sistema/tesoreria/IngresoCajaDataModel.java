package com.instituto.sistema.tesoreria;

import java.util.ArrayList;
import java.util.List;

public class IngresoCajaDataModel {
	
	private String cuentaNum;
	private String entidad;
	private double total;
	private List<Object[]> ldata;
	
	
	public IngresoCajaDataModel(String cuentaNum, String entidad) {
	
		this.cuentaNum = cuentaNum;
		this.entidad = entidad;
		this.ldata = new ArrayList<Object[]>();
	
	}
	
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public List<Object[]> getLdata() {
		return ldata;
	}
	public void setLdata(List<Object[]> ldata) {
		this.ldata = ldata;
	}
	public String getCuentaNum() {
		return cuentaNum;
	}
	public void setCuentaNum(String cuentaNum) {
		this.cuentaNum = cuentaNum;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

}
