package com.instituto.sistema.gestionSede;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.TemplateViewModel;
import com.doxacore.modelo.Usuario;
import com.instituto.modelo.Sede;
import com.instituto.modelo.UsuarioSede;
import com.instituto.util.ParamsLocal;

public class UsuarioSedeVM extends TemplateViewModel{
	
	private List<Usuario> lUsuarios;
	private List<Usuario> lUsuariosOri;
	private List<UsuarioSede> lSedesUsuarios;	
	private Usuario usuarioSelectedSede;
	private String filtroColumnsUsuario[];
	private UsuarioSede usuarioSedeSelected;
	
	private boolean opAgregarSede;
	private boolean opQuitarSede;

	@Init(superclass = true)
	public void initUsuarioSedeVM() {

		cargarUsuarios();
		inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeUsuarioSedeVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		
		this.opAgregarSede = this.operacionHabilitada(ParamsLocal.OP_AGREGAR_SEDE);
		this.opQuitarSede = this.operacionHabilitada(ParamsLocal.OP_QUITAR_SEDE);
		
	}
	
	private void cargarUsuarios() {
		 
		this.lUsuarios = this.reg.getAllObjectsByCondicionOrder(Usuario.class.getName(), null, "usuarioid asc");
		this.lUsuariosOri = this.lUsuarios;

	}
	
	private void inicializarFiltros(){
		
		this.filtroColumnsUsuario = new String[3]; // se debe de iniciar el filtro deacuerdo a la cantidad declarada en el modelo
		
		for (int i = 0 ; i<this.filtroColumnsUsuario.length; i++) {
			
			this.filtroColumnsUsuario[i] = "";
			
		}
		
	}

	@Command
	@NotifyChange("lUsuarios")
	public void filtrarUsuario() {

		lUsuarios = this.filtrarLT(this.filtroColumnsUsuario, this.lUsuariosOri);

	}
	
	// seccion buscador

		private List<Object[]> lSedesbuscarOri;
		private List<Object[]> lSedesBuscar;
		private Sede buscarSelectedSede;
		private String buscarSede = "";

		@Command
		@NotifyChange("lSedesBuscar")
		public void filtrarSedeBuscar() {

			this.lSedesBuscar = this.filtrarListaObject(buscarSede, this.lSedesbuscarOri);

		}

		@Command
		@NotifyChange("lSedesBuscar")
		public void generarListaBuscarSede() {

			if (this.usuarioSelectedSede == null) {

				this.mensajeInfo("Selecciona un Usuario.");

				return;
			}

			this.lSedesBuscar = this.reg.sqlNativo("select s.sedeid, s.sede, s.direccion from sedes s order by sedeid;");
			this.lSedesbuscarOri = this.lSedesBuscar;
		}
		
		@Command
		@NotifyChange("buscarSede")
		public void onSelectSede(@BindingParam("id") long id) {
			
			this.buscarSelectedSede = this.reg.getObjectById(Sede.class.getName(), id);
			this.buscarSede = this.buscarSelectedSede.getSede();
			
		}

		@Command
		@NotifyChange({"lSedesUsuarios","buscarSede"})
		public void agregarSede() {
			
			if (!this.opAgregarSede) {
				
				this.mensajeError("No tienes permiso para agregar un Sede al Usuario. ");
				return;
			}
			
			if (this.buscarSelectedSede == null) {
				
				this.mensajeInfo("Selecciona un Sede para agregar.");
				
				return;
				
			}
			
			for(UsuarioSede x : this.lSedesUsuarios) {
				
				if (this.buscarSelectedSede.getSedeid() == x.getSede().getSedeid()) {
					
					this.mensajeError("El Usuario ya tiene la Sede "+x.getSede().getSede());
					
					return;
					
				}
				
			}
			
			UsuarioSede us = new UsuarioSede();
			us.setSede(this.buscarSelectedSede);
			us.setUsuario(this.usuarioSelectedSede);
			
			if (this.lSedesUsuarios.size() == 0) {
				us.setActivo(true);
			}
			
			this.save(us);

			this.refrescarSedes(this.usuarioSelectedSede);

		}
		

		// fin buscador
		
		// Seccion sede
		
		@Command
		@NotifyChange({"lSedesUsuarios","buscarSede"})
		public void refrescarSedes(@BindingParam("usuario") Usuario usuario) {

			this.usuarioSelectedSede = usuario;
			this.lSedesUsuarios = this.reg.getAllObjectsByCondicionOrder(UsuarioSede.class.getName(),
					"usuarioid = " + usuario.getUsuarioid(), "sedeid asc");
			
			this.buscarSelectedSede = null;
			this.buscarSede="";

		}
		
		@Command
		public void borrarSedeConfirmacion(@BindingParam("usuariosede") final UsuarioSede us) {
			
			if (!this.opQuitarSede) {
				
				this.mensajeError("No tienes permisos para Borrar Sedes a un Usuario.");
				
				return;
				
			}
			
			this.mensajeEliminar("La Sede "+us.getSede().getSede()+" sera removida del Usuario "+us.getUsuario().getAccount()+". \n Continuar?",  
					new EventListener() {

				@Override
				public void onEvent(Event evt) throws Exception {

					if (evt.getName().equals(Messagebox.ON_YES)) {

						borrarUsuarioSede(us);

					}

				}

			});

		}
		
		private void borrarUsuarioSede(UsuarioSede us) {
			
			this.reg.deleteObject(us);

			this.refrescarSedes(this.usuarioSelectedSede);

			BindUtils.postNotifyChange(null, null, this, "lSedesUsuarios");
			
		}
		
		@Command
		@NotifyChange("lSedesUsuarios")
		public void habilitarSede(@BindingParam("usuariosede") final UsuarioSede us) {
			
			for (UsuarioSede usx : this.lSedesUsuarios) {
				
				if (usx.isActivo()) {
					
					usx.setActivo(false);
					this.save(usx);
					break;
				}				
				
				
			}
			
			us.setActivo(true);
			this.save(us);
			this.refrescarSedes(this.usuarioSelectedSede);
			
		}
		
		// fin seccion sede
		
		private Window modal;
		
		@Command
		public void modalUsuarioSede(@BindingParam("usuariosede") UsuarioSede usuariosede) {
			
			this.usuarioSedeSelected = usuariosede;

			modal = (Window) Executions.createComponents("/instituto/zul/gestionSede/usuarioSedeModal.zul", this.mainComponent,
					null);
			Selectors.wireComponents(modal, this, false);
			modal.doModal();

		}
		
		@Command
		public void guardar() {
			
			this.save(this.usuarioSedeSelected);
			modal.detach();
			
			this.refrescarSedes(this.usuarioSelectedSede);
			
		}

		public List<Usuario> getlUsuarios() {
			return lUsuarios;
		}

		public void setlUsuarios(List<Usuario> lUsuarios) {
			this.lUsuarios = lUsuarios;
		}

		public List<UsuarioSede> getlSedesUsuarios() {
			return lSedesUsuarios;
		}

		public void setlSedesUsuarios(List<UsuarioSede> lSedesUsuarios) {
			this.lSedesUsuarios = lSedesUsuarios;
		}

		public Usuario getUsuarioSelectedSede() {
			return usuarioSelectedSede;
		}

		public void setUsuarioSelectedSede(Usuario usuarioSelectedSede) {
			this.usuarioSelectedSede = usuarioSelectedSede;
		}

		public boolean isOpAgregarSede() {
			return opAgregarSede;
		}

		public void setOpAgregarSede(boolean opAgregarSede) {
			this.opAgregarSede = opAgregarSede;
		}

		public boolean isOpQuitarSede() {
			return opQuitarSede;
		}

		public void setOpQuitarSede(boolean opQuitarSede) {
			this.opQuitarSede = opQuitarSede;
		}

		public List<Object[]> getlSedesBuscar() {
			return lSedesBuscar;
		}

		public void setlSedesBuscar(List<Object[]> lSedesBuscar) {
			this.lSedesBuscar = lSedesBuscar;
		}

		public Sede getBuscarSelectedSede() {
			return buscarSelectedSede;
		}

		public void setBuscarSelectedSede(Sede buscarSelectedSede) {
			this.buscarSelectedSede = buscarSelectedSede;
		}

		public String getBuscarSede() {
			return buscarSede;
		}

		public void setBuscarSede(String buscarSede) {
			this.buscarSede = buscarSede;
		}

		public String[] getFiltroColumnsUsuario() {
			return filtroColumnsUsuario;
		}

		public void setFiltroColumnsUsuario(String[] filtroColumnsUsuario) {
			this.filtroColumnsUsuario = filtroColumnsUsuario;
		}

		public UsuarioSede getUsuarioSedeSelected() {
			return usuarioSedeSelected;
		}

		public void setUsuarioSedeSelected(UsuarioSede usuarioSedeSelected) {
			this.usuarioSedeSelected = usuarioSedeSelected;
		}
		
		

}
