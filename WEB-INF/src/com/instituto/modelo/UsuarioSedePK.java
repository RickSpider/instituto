package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.doxacore.modelo.Usuario;

@Embeddable
public class UsuarioSedePK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2511179380583218431L;

	@ManyToOne
	@JoinColumn(name="usuarioid")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="sedeid")
	private Sede sede;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Sede getSede() {
		return sede;
	}

	public void setSede(Sede sede) {
		this.sede = sede;
	}
	
	

}
