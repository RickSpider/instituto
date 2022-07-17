package com.instituto.sistema.abm;

import java.util.List;

import org.zkoss.zul.Textbox;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.South;
import org.zkoss.zul.Window;

import com.doxacore.TemplateViewModel;
import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Pais;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Tipotipo;
import com.instituto.modelo.GradoAcademico;
import com.instituto.modelo.Institucion;
import com.instituto.modelo.Persona;
import com.instituto.util.ParamsLocal;


public class PersonaVM extends TemplateViewModel {

	private List<Persona> lPersonas;
	private List<Persona> lPersonasOri;
	private Persona personaSelected;

	private boolean opCrearPersona;
	private boolean opEditarPersona;
	private boolean opBorrarPersona;
	
	@Init(superclass = true)
	public void initPersonaVM() {

		cargarPersonas();
		inicializarFiltros();

	}
	
	@AfterCompose(superclass = true)
	public void afterComposePersonaVM() {

	}
	
	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearPersona = this.operacionHabilitada(ParamsLocal.OP_CREAR_PERSONA);
		this.opEditarPersona = this.operacionHabilitada(ParamsLocal.OP_EDITAR_PERSONA);
		this.opBorrarPersona = this.operacionHabilitada(ParamsLocal.OP_BORRAR_PERSONA);
		
	}

	private void cargarPersonas() {

		this.lPersonas = this.reg.getAllObjectsByCondicionOrder(Persona.class.getName(), null, "Personaid asc");
		this.lPersonasOri = this.lPersonas;
	}

	// seccion filtro

		private String filtroColumns[];

		private void inicializarFiltros() {

			this.filtroColumns = new String[7]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el
												// modelo sin id

			for (int i = 0; i < this.filtroColumns.length; i++) {

				this.filtroColumns[i] = "";

			}

		}

		@Command
		@NotifyChange("lPersonas")
		public void filtrarPersona() {

			this.lPersonas = this.filtrarLT(this.filtroColumns, this.lPersonasOri);

		}

		// fin seccion
		
		//seccion modal
		
		private Window modal;
		private boolean editar = false;

		@Command
		public void modalPersonaAgregar() {

			if(!this.opCrearPersona)
				return;

			this.editar = false;
			modalPersona(-1);

		}

		@Command
		public void modalPersona(@BindingParam("personaid") long personaid) {

			if (personaid != -1) {

				if(!this.opEditarPersona)
					return;
				
				this.personaSelected = this.reg.getObjectById(Persona.class.getName(), personaid);
				this.buscarDocumento = this.personaSelected.getDocumentoTipo().getTipo();
				this.buscarEstadoCivil = this.personaSelected.getEstadoCivil().getTipo();
				this.buscarPais = this.personaSelected.getNacionalidad().getGentilicio();
				this.buscarCiudad = this.personaSelected.getCiudad().getCiudad();
				this.buscarGradoAcademico = this.personaSelected.getGradoAcademico().getGradoacademico();
				this.buscarInstitucion = "";
				this.buscarPersonaTipo = "";
				
				if ( this.personaSelected.getInstitucion() != null) {
					
					this.buscarInstitucion = this.personaSelected.getInstitucion().getInstitucion();
					
				}
					
				if ( this.personaSelected.getPersonaTipo() != null) {
					
					this.buscarPersonaTipo = this.personaSelected.getPersonaTipo().getTipo();
					
				}
				
				this.editar = true;

			} else {
				
				personaSelected = new Persona();
				this.buscarDocumento = "";
				this.buscarEstadoCivil = "";
				this.buscarPais = "";
				this.buscarCiudad = "";
				this.buscarGradoAcademico = "";
				this.buscarInstitucion = "";
				this.buscarPersonaTipo = "";

			}

			modal = (Window) Executions.createComponents("/instituto/zul/abm/personaModal.zul", this.mainComponent,
					null);
			Selectors.wireComponents(modal, this, false);
			modal.doModal();

		}
		
		private boolean verificarCampos() {
			
			if (personaSelected.getNombre() == null || this.personaSelected.getNombre().length() <= 0) {
				
				return false;
				
			}
			
			if (personaSelected.getApellido() == null || this.personaSelected.getApellido().length() <= 0) {
				
				return false;
				
			}
			
			if (personaSelected.getEmail() == null || this.personaSelected.getEmail().length() <= 0) {
							
				return false;
						
			}
			
			if (personaSelected.getTelefono() == null || this.personaSelected.getTelefono().length() <= 0) {
				
				return false;
				
			}
			
			if (personaSelected.getDireccion() == null || this.personaSelected.getDireccion().length() <= 0) {
				
				return false;
				
			}
			
			if (personaSelected.getDocumentoNum() == null || this.personaSelected.getDocumentoNum().length() <= 0) {
					
				return false;
				
			}
			
			if (personaSelected.getDocumentoTipo() == null) {
				
				return false;
				
			}
			
			if (personaSelected.getEstadoCivil() == null) {
				
				return false;
				
			}
			

			if (personaSelected.getNacionalidad() == null) {
				
				return false;
				
			}
			

			if (personaSelected.getCiudad() == null) {
				
				return false;
				
			}
			
			if (personaSelected.getGradoAcademico() == null) {
				
				return false;
				
			}
			
			if (personaSelected.getEgresoAno() <= 0 ) {
				
				return false;
				
			}
			
			return true;
		}
		
	
		@Command
		public void onBlurDocumentoNum(@BindingParam("comp") Textbox comp) {
			
			Persona persona = reg.getObjectByColumnString(Persona.class.getName(), "documentoNum", comp.getValue() );
			
			if (persona != null && editar == false) {

				
				Notification.show("Ya existe Documento "+comp.getValue()+".", "error", comp, "after_start",2000,false);
				
				comp.focus();
				
			}
			
			
		}

		@Command
		@NotifyChange("lPersonas")
		public void guardar() {
			
			if (!verificarCampos()) {
				return;
			}

			this.save(personaSelected);

			this.personaSelected = null;

			this.cargarPersonas();

			this.modal.detach();
			
			if (editar) {
				
				Notification.show("La Persona fue Actualizada.");
				this.editar = false;
			}else {
				
				Notification.show("La Persona fue agregada.");
			}
			
			

		}

		//fin modal
		
		@Command
		public void borrarPersonaConfirmacion(@BindingParam("persona") final Persona persona) {
			
			if (!this.opBorrarPersona)
				return;
			
			EventListener event = new EventListener () {

				@Override
				public void onEvent(Event evt) throws Exception {
					
					if (evt.getName().equals(Messagebox.ON_YES)) {
						
						borrarPersona(persona);
						
					}
					
				}

			};
			
			this.mensajeEliminar("La persona sera eliminada. \n Continuar?", event);
		}
		
		private void borrarPersona (Persona persona) {
			
			this.reg.deleteObject(persona);
			
			this.cargarPersonas();
			
			BindUtils.postNotifyChange(null,null,this,"lPersonas");
			
		}
		

		//buscarDocumento
		
		private List<Object[]> lDocumentosBuscarOri = null;
		private List<Object[]> lDocumentosBuscar = null;
		private Tipo buscarSelectedDocumento = null;
		private String buscarDocumento = "";
					
		@Command
		@NotifyChange("lDocumentosBuscar")
		public void filtrarDocumentoBuscar() {

			this.lDocumentosBuscar = this.filtrarListaObject(buscarDocumento, this.lDocumentosBuscarOri);

		}
		
		@Command
		@NotifyChange("lDocumentosBuscar")
		public void generarListaBuscarDocumento() {
			
			Tipotipo tipotipo = this.reg.getObjectByColumnString(Tipotipo.class.getName(), "sigla", ParamsLocal.SIGLA_DOCUMENTO);
			String buscarDocumentoSQL = this.um.getSql("buscarTipos.sql").replace("?1", ""+tipotipo.getTipotipoid());
			
			//System.out.println(buscarDocumentoSQL);
			
			this.lDocumentosBuscar = this.reg.sqlNativo(buscarDocumentoSQL);
			
			this.lDocumentosBuscarOri = this.lDocumentosBuscar;
		}
		
		@Command
		@NotifyChange("buscarDocumento")
		public void onSelectDocumento(@BindingParam("id") long id) {
			
			this.buscarSelectedDocumento = this.reg.getObjectById(Tipo.class.getName(), id);	
			this.buscarDocumento = this.buscarSelectedDocumento.getTipo();
			this.personaSelected.setDocumentoTipo(buscarSelectedDocumento);

		}
		
		//fin buscarDocumento
		
		//buscarEstadoCivil
		
		private List<Object[]> lEstadosCivilesBuscarOri = null;
		private List<Object[]> lEstadosCivilesBuscar = null;
		private Tipo buscarSelectedEstadoCivil = null;
		private String buscarEstadoCivil = "";
					
		@Command
		@NotifyChange("lEstadosCivilesBuscar")
		public void filtrarEstadoCivilBuscar() {

			this.lEstadosCivilesBuscar = this.filtrarListaObject(buscarEstadoCivil, this.lEstadosCivilesBuscarOri);

		}
		
		@Command
		@NotifyChange("lEstadosCivilesBuscar")
		public void generarListaBuscarEstadoCivil() {
			
			Tipotipo tipotipo = this.reg.getObjectByColumnString(Tipotipo.class.getName(), "sigla", ParamsLocal.SIGLA_ESTADO_CIVIL);
			String buscarEstadoCivilSQL = this.um.getSql("buscarTipos.sql").replace("?1", ""+tipotipo.getTipotipoid());
			
			System.out.println(buscarEstadoCivilSQL);
			
			this.lEstadosCivilesBuscar = this.reg.sqlNativo(buscarEstadoCivilSQL);
			
			this.lEstadosCivilesBuscarOri = this.lEstadosCivilesBuscar;
		}
		
		@Command
		@NotifyChange("buscarEstadoCivil")
		public void onSelectEstadoCivil(@BindingParam("id") long id) {
			
			this.buscarSelectedEstadoCivil = this.reg.getObjectById(Tipo.class.getName(), id);	
			this.buscarEstadoCivil = this.buscarSelectedEstadoCivil.getTipo();
			this.personaSelected.setEstadoCivil(buscarSelectedEstadoCivil);

		}
		
		//fin buscarEstadoCivil
		
		// buscador de Ciudad

			private List<Object[]> lCiudadesBuscarOri = null;
			private List<Object[]> lCiudadesBuscar = null;
			private Ciudad buscarSelectedCiudad = null;
			private String buscarCiudad = "";

			@Command
			@NotifyChange("lCiudadesBuscar")
			public void filtrarCiudadBuscar() {

				this.lCiudadesBuscar = this.filtrarListaObject(buscarCiudad, this.lCiudadesBuscarOri);

			}
			
			@Command
			@NotifyChange("lCiudadesBuscar")
			public void generarListaBuscarCiudad() {

				this.lCiudadesBuscar = this.reg.sqlNativo(
						"select c.ciudadid,"
						+ " c.ciudad,"
						+ " d.departamento,"
						+ " p.pais \n" + 
						"from ciudades c\n" + 
						"left join departamentos d on d.departamentoid = c.departamentoid\n" + 
						"left join paises p on p.paisid = d.paisid\n" + 
						"order by c.ciudadid asc;");
				
				this.lCiudadesBuscarOri = this.lCiudadesBuscar;
			}
			
			@Command
			@NotifyChange("buscarCiudad")
			public void onSelectCiudad(@BindingParam("id") long id) {
				
				this.buscarSelectedCiudad = this.reg.getObjectById(Ciudad.class.getName(), id);	
				this.buscarCiudad = this.buscarSelectedCiudad.getCiudad();
				this.personaSelected.setCiudad(buscarSelectedCiudad);

			}
			
			// fin buscar ciudad
			
			//buscar Pais
			
			private List<Object[]> lPaisesBuscarOri = null;
			private List<Object[]> lPaisesBuscar = null;
			private Pais buscarSelectedPais = null;
			private String buscarPais = "";
			
			@Command
			@NotifyChange("lPaisesBuscar")
			public void filtrarPaisBuscar() {

				this.lPaisesBuscar = this.filtrarListaObject(buscarPais, this.lPaisesBuscarOri);

			}
			
			@Command
			@NotifyChange("lPaisesBuscar")
			public void generarListaBuscarPais() {
				
				this.lPaisesBuscar = this.reg.sqlNativo(
						"SELECT \n" + 
						"p.paisid, \n" + 
						"p.pais, \n" + 
						"p.gentilicio\n" + 
						"FROM paises p;");
				
				this.lPaisesBuscarOri = this.lPaisesBuscar;
			}
			
			
			@Command
			@NotifyChange("buscarPais")
			public void onSelectPais(@BindingParam("id") long id) {
				
				this.buscarSelectedPais = this.reg.getObjectById(Pais.class.getName(), id);	
				this.buscarPais = this.buscarSelectedPais.getGentilicio();
				this.personaSelected.setNacionalidad(buscarSelectedPais);

			}
			
			//fin buscar pais
			
			//buscarGradoAcademico
			
			private List<Object[]> lGradosAcademicosBuscarOri = null;
			private List<Object[]> lGradosAcademicosBuscar = null;
			private GradoAcademico buscarSelectedGradoAcademico = null;
			private String buscarGradoAcademico = "";
						
			@Command
			@NotifyChange("lGradosAcademicosBuscar")
			public void filtrarGradoAcademicoBuscar() {

				this.lGradosAcademicosBuscar = this.filtrarListaObject(buscarGradoAcademico, this.lGradosAcademicosBuscarOri);

			}
			
			@Command
			@NotifyChange("lGradosAcademicosBuscar")
			public void generarListaBuscarGradoAcademico() {
				
				
				String buscarGradoAcademicoSQL = this.um.getSql("buscarGradoAcademico.sql");
				
				//System.out.println(buscarGradoAcademicoSQL);
				
				this.lGradosAcademicosBuscar = this.reg.sqlNativo(buscarGradoAcademicoSQL);
				
				this.lGradosAcademicosBuscarOri = this.lGradosAcademicosBuscar;
			}
			
			@Command
			@NotifyChange("buscarGradoAcademico")
			public void onSelectGradoAcademico(@BindingParam("id") long id) {
				
				this.buscarSelectedGradoAcademico = this.reg.getObjectById(GradoAcademico.class.getName(), id);	
				this.buscarGradoAcademico = this.buscarSelectedGradoAcademico.getGradoacademico();
				this.personaSelected.setGradoAcademico(buscarSelectedGradoAcademico);

			}
			
			//fin buscarGradoAcademico
			
			// buscador de Institucion

			private List<Object[]> lInstitucionesBuscarOri = null;
			private List<Object[]> lInstitucionesBuscar = null;
			private Institucion buscarSelectedInstitucion = null;
			private String buscarInstitucion = "";

			@Command
			@NotifyChange("lInstitucionesBuscar")
			public void filtrarInstitucionBuscar() {

				this.lInstitucionesBuscar = this.filtrarListaObject(buscarInstitucion, this.lInstitucionesBuscarOri);

			}

			@Command
			@NotifyChange("lInstitucionesBuscar")
			public void generarListaBuscarInstitucion() {

				String sqlInstitucion = this.um.getSql("buscarInstitucion.sql") ;
				
				this.lInstitucionesBuscar = this.reg.sqlNativo(sqlInstitucion);

				this.lInstitucionesBuscarOri = this.lInstitucionesBuscar;
			}

			@Command
			@NotifyChange("buscarInstitucion")
			public void onSelectInstitucion(@BindingParam("id") long id) {

				this.buscarSelectedInstitucion = this.reg.getObjectById(Institucion.class.getName(), id);
				this.buscarInstitucion = this.buscarSelectedInstitucion.getInstitucion();
				this.personaSelected.setInstitucion(buscarSelectedInstitucion);

			}

			// fin buscar ciudad
			
			//buscarPersonaTipo
			
			private List<Object[]> lPersonasTiposBuscarOri = null;
			private List<Object[]> lPersonasTiposBuscar = null;
			private Tipo buscarSelectedPersonaTipo = null;
			private String buscarPersonaTipo = "";
						
			@Command
			@NotifyChange("lPersonasTiposBuscar")
			public void filtrarPersonaTipoBuscar() {

				this.lPersonasTiposBuscar = this.filtrarListaObject(buscarPersonaTipo, this.lPersonasTiposBuscarOri);

			}
			
			@Command
			@NotifyChange("lPersonasTiposBuscar")
			public void generarListaBuscarPersonaTipo() {
				
				Tipotipo tipotipo = this.reg.getObjectByColumnString(Tipotipo.class.getName(), "sigla", ParamsLocal.SIGLA_PERSONA);
				String buscarPersonaTipoSQL = this.um.getSql("buscarTipos.sql").replace("?1", ""+tipotipo.getTipotipoid());
				
				//System.out.println(buscarPersonaTipoSQL);
				
				this.lPersonasTiposBuscar = this.reg.sqlNativo(buscarPersonaTipoSQL);
				
				this.lPersonasTiposBuscarOri = this.lPersonasTiposBuscar;
			}
			
			@Command
			@NotifyChange("buscarPersonaTipo")
			public void onSelectPersonaTipo(@BindingParam("id") long id) {
				
				this.buscarSelectedPersonaTipo = this.reg.getObjectById(Tipo.class.getName(), id);	
				this.buscarPersonaTipo = this.buscarSelectedPersonaTipo.getTipo();
				this.personaSelected.setPersonaTipo(buscarSelectedPersonaTipo);

			}
			
			//fin buscarPersonaTipo

			
			public List<Persona> getlPersonas() {
				return lPersonas;
			}

			public void setlPersonas(List<Persona> lPersonas) {
				this.lPersonas = lPersonas;
			}

			public Persona getPersonaSelected() {
				return personaSelected;
			}

			public void setPersonaSelected(Persona personaSelected) {
				this.personaSelected = personaSelected;
			}

			public boolean isOpCrearPersona() {
				return opCrearPersona;
			}

			public void setOpCrearPersona(boolean opCrearPersona) {
				this.opCrearPersona = opCrearPersona;
			}

			public boolean isOpEditarPersona() {
				return opEditarPersona;
			}

			public void setOpEditarPersona(boolean opEditarPersona) {
				this.opEditarPersona = opEditarPersona;
			}

			public boolean isOpBorrarPersona() {
				return opBorrarPersona;
			}

			public void setOpBorrarPersona(boolean opBorrarPersona) {
				this.opBorrarPersona = opBorrarPersona;
			}

			public String[] getFiltroColumns() {
				return filtroColumns;
			}

			public void setFiltroColumns(String[] filtroColumns) {
				this.filtroColumns = filtroColumns;
			}

			public boolean isEditar() {
				return editar;
			}

			public void setEditar(boolean editar) {
				this.editar = editar;
			}

			public List<Object[]> getlCiudadesBuscar() {
				return lCiudadesBuscar;
			}

			public void setlCiudadesBuscar(List<Object[]> lCiudadesBuscar) {
				this.lCiudadesBuscar = lCiudadesBuscar;
			}

			public String getBuscarCiudad() {
				return buscarCiudad;
			}

			public void setBuscarCiudad(String buscarCiudad) {
				this.buscarCiudad = buscarCiudad;
			}

			public List<Object[]> getlPaisesBuscar() {
				return lPaisesBuscar;
			}

			public void setlPaisesBuscar(List<Object[]> lPaisesBuscar) {
				this.lPaisesBuscar = lPaisesBuscar;
			}

			public String getBuscarPais() {
				return buscarPais;
			}

			public void setBuscarPais(String buscarPais) {
				this.buscarPais = buscarPais;
			}

			public List<Object[]> getlEstadosCivilesBuscar() {
				return lEstadosCivilesBuscar;
			}

			public void setlEstadosCivilesBuscar(List<Object[]> lEstadosCivilesBuscar) {
				this.lEstadosCivilesBuscar = lEstadosCivilesBuscar;
			}

			public String getBuscarEstadoCivil() {
				return buscarEstadoCivil;
			}

			public void setBuscarEstadoCivil(String buscarEstadoCivil) {
				this.buscarEstadoCivil = buscarEstadoCivil;
			}

			public List<Object[]> getlDocumentosBuscar() {
				return lDocumentosBuscar;
			}

			public void setlDocumentosBuscar(List<Object[]> lDocumentosBuscar) {
				this.lDocumentosBuscar = lDocumentosBuscar;
			}

			public String getBuscarDocumento() {
				return buscarDocumento;
			}

			public void setBuscarDocumento(String buscarDocumento) {
				this.buscarDocumento = buscarDocumento;
			}

			public String getBuscarGradoAcademico() {
				return buscarGradoAcademico;
			}

			public void setBuscarGradoAcademico(String buscarGradoAcademico) {
				this.buscarGradoAcademico = buscarGradoAcademico;
			}

			public List<Object[]> getlGradosAcademicosBuscar() {
				return lGradosAcademicosBuscar;
			}

			public void setlGradosAcademicosBuscar(List<Object[]> lGradosAcademicosBuscar) {
				this.lGradosAcademicosBuscar = lGradosAcademicosBuscar;
			}

			public String getBuscarInstitucion() {
				return buscarInstitucion;
			}

			public void setBuscarInstitucion(String buscarInstitucion) {
				this.buscarInstitucion = buscarInstitucion;
			}

			public List<Object[]> getlInstitucionesBuscar() {
				return lInstitucionesBuscar;
			}

			public void setlInstitucionesBuscar(List<Object[]> lInstitucionesBuscar) {
				this.lInstitucionesBuscar = lInstitucionesBuscar;
			}

			public List<Object[]> getlPersonasTiposBuscar() {
				return lPersonasTiposBuscar;
			}

			public void setlPersonasTiposBuscar(List<Object[]> lPersonasTiposBuscar) {
				this.lPersonasTiposBuscar = lPersonasTiposBuscar;
			}

			public String getBuscarPersonaTipo() {
				return buscarPersonaTipo;
			}

			public void setBuscarPersonaTipo(String buscarPersonaTipo) {
				this.buscarPersonaTipo = buscarPersonaTipo;
			}
			
			//fin buscador de ciudad 
		
	
}
