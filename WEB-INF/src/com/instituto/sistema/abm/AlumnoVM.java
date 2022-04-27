package com.instituto.sistema.abm;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.doxacore.TemplateViewModel;
import com.instituto.modelo.Persona;

public class AlumnoVM extends TemplateViewModel  {

	private List<Persona> lAlumnos;
	private List<Persona> lAlumnosOri;
	private Persona sedeSelected;

	private boolean opCrearAlumno;
	private boolean opEditarAlumno;
	private boolean opBorrarAlumno;

	@Init(superclass = true)
	public void initAlumnoVM() {



	}

	@AfterCompose(superclass = true)
	public void afterComposeAlumnoVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub
		
	}

}
