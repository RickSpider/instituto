package com.instituto.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@ManyToOne
	@JoinColumn(name = "gradoacademicoid")
	private GradoAcademico gradoAcademico;
	
	@Column(nullable=false, columnDefinition = "int default 0")
	private int egresoAno;	
	
	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;
	
	@ManyToOne
	@JoinColumn(name = "documentotipoid")
	private Tipo documentoTipo;
	
	@Column(name = "documentoNum", unique = true)
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
	
	@ManyToOne
	@JoinColumn(name = "institucionid")
	private Institucion institucion;
	
	@Override
	public Object[] getArrayObjectDatos() {
		
		Object[] o = {this.nombre, this.apellido, this.documentoNum,this.email, this.telefono, this.direccion};
		
		return o;
	}

	@Override
	public String getStringDatos() {
		return this.personaid + " " + this.nombre + " " + this.apellido +" "+this.documentoNum+" "+ this.email+" " + this.telefono +" "+this.direccion;
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
	
	public String getNombreCompleto() {
			
		return this.apellido.trim()+", "+this.nombre.trim();
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

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public GradoAcademico getGradoAcademico() {
		return gradoAcademico;
	}

	public void setGradoAcademico(GradoAcademico gradoAcademico) {
		this.gradoAcademico = gradoAcademico;
	}

	public int getEgresoAno() {
		return egresoAno;
	}

	public void setEgresoAno(int egresoAno) {
		this.egresoAno = egresoAno;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Institucion getInstitucion() {
		return institucion;
	}

	public void setInstitucion(Institucion institucion) {
		this.institucion = institucion;
	}

}
