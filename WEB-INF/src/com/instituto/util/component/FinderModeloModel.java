package com.instituto.util.component;

import java.util.List;

import com.doxacore.modelo.Modelo;
import com.doxacore.util.Register;

public class FinderModeloModel <T extends Modelo>{

	private Register register;
	private String nameFinder;
	
	private String condicion;
	private String className;
	
	private List<T> listFinderModelo;
	private List<T> listFinderModeloOri;
	
	
	
	public FinderModeloModel(String nameFinder, Register register, String className, String condicion) {
		super();
		this.nameFinder = nameFinder;
		this.register = register;
		this.className = className;
		this.condicion = condicion;

	}
	
	
	
	public void generarListFinder() {

		this.listFinderModelo = this.register.getAllObjectsByCondicionOrder(className, condicion, null );
		this.listFinderModeloOri = this.listFinderModelo;
		
	}
	
	public Register getRegister() {
		return register;
	}
	public void setRegister(Register register) {
		this.register = register;
	}
	public List<T> getListFinderModelo() {
		return listFinderModelo;
	}
	public void setListFinderModelo(List<T> listFinderModelo) {
		this.listFinderModelo = listFinderModelo;
	}
	public List<T> getListFinderModeloOri() {
		return listFinderModeloOri;
	}
	public void setListFinderModeloOri(List<T> listFinderModeloOri) {
		this.listFinderModeloOri = listFinderModeloOri;
	}



	public String getNameFinder() {
		return nameFinder;
	}



	public void setNameFinder(String nameFinder) {
		this.nameFinder = nameFinder;
	}



	public String getCondicion() {
		return condicion;
	}



	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}



	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}
	
	
}
