package com.instituto.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Pais;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name ="personas")
public class Persona extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2360755743582101792L;
	
	@Id
	@Column(name ="personaid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long personaid;
	
	private String nombre;
	private String apellido;
	private String email;
	private String telefono;
	private String direccion;
	
	@ManyToOne()
	@JoinColumn(name = "documentotipoid")
	private Tipo documentoTipo;
	private String documentoNum;
	
	private String ruc;
	
	@ManyToOne
	@JoinColumn(name = "paisid")
	private Pais nacionalidad;
	
	@ManyToOne
	@JoinColumn(name = "ciudadid")
	private Ciudad ciudad;
	
	@ManyToOne
	@JoinColumn(name = "estadocivilid")
	private Tipo estadoCivil;

	/*@OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Alumno alumno;*/
	
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

	public Long getPersonaid() {
		return personaid;
	}

	public void setPersonaid(Long personaid) {
		this.personaid = personaid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDocumentoNum() {
		return documentoNum;
	}

	public void setDocumentoNum(String documentoNum) {
		this.documentoNum = documentoNum;
	}

	public Tipo getDocumentoTipo() {
		return documentoTipo;
	}

	public void setDocumentoTipo(Tipo documentoTipo) {
		this.documentoTipo = documentoTipo;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public Pais getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(Pais nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public Tipo getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(Tipo estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	
	
	

}
