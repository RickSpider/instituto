package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Usuario;

@Entity
@Table(name="UsuariosSedes")
public class UsuarioSede extends Modelo implements Serializable{

	private static final long serialVersionUID = 5046204547702055201L;
	
	@EmbeddedId
	private UsuarioSedePK usuariosedepk = new UsuarioSedePK();

	private boolean activo = false;

	@Override
	public Object[] getArrayObjectDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	public Usuario getUsuario() {
		
		return this.usuariosedepk.getUsuario();
		
	}
	
	public void setUsuario(Usuario usuario) {
		
		this.usuariosedepk.setUsuario(usuario);
		
	}
	
	public Sede getSede() {
		
		return this.usuariosedepk.getSede();
		
	}
	
	public void setSede(Sede sede) {
		
		this.usuariosedepk.setSede(sede);
		
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public UsuarioSedePK getUsuariosedepk() {
		return usuariosedepk;
	}

	public void setUsuariosedepk(UsuarioSedePK usuariosedepk) {
		this.usuariosedepk = usuariosedepk;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
