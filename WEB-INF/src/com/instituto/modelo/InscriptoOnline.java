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

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.doxacore.modelo.Ciudad;
import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Pais;

@Entity
@Table(name ="inscriptosonline")
public class InscriptoOnline extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 731774608801816632L;

	
	@Id
	@Column(name ="inscriptoonlineid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long inscriptoonlineid;
	
	@CreationTimestamp 
    @Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInscripcion;
	
	
	
	private String nombre;
	private String apellido;
	
	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;
	
	private String email;
	private String telefono;
	
	@Column(columnDefinition="text")
	private String direccion;
	
	private String ci;
	
	private String razonSocial;
	private String ruc;
	
	@ManyToOne
	@JoinColumn(name = "ciudadid")
	private Ciudad ciudad;
	
	@ManyToOne
	@JoinColumn(name = "paisid")
	private Pais pais;
	
	@ManyToOne
	@JoinColumn(name = "alumnoid")
	private Alumno alumno;
	
	@ManyToOne
	@JoinColumn(name = "cursovigenteid")
	private CursoVigente cursoVigente;
	
	@ColumnDefault("false")
	private boolean emailVerificado;
	
	
	private String keyTemporal;
	
	
	@ColumnDefault("false")
	private Boolean facturaTercero = false;
	
	/*private String emailTercero;
	private String telefonoTercero;*/
	
	private String tituloObtenido;
	
	private String encuesta;
	
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
	
	public Long getInscriptoonlineid() {
		return inscriptoonlineid;
	}
	public void setInscriptoonlineid(Long inscriptoonlineid) {
		this.inscriptoonlineid = inscriptoonlineid;
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
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public Alumno getAlumno() {
		return alumno;
	}
	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public CursoVigente getCursoVigente() {
		return cursoVigente;
	}
	public void setCursoVigente(CursoVigente cursoVigente) {
		this.cursoVigente = cursoVigente;
	}
	public Date getFechaInscripcion() {
		return fechaInscripcion;
	}
	public void setFechaInscripcion(Date fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}
	public boolean isEmailVerificado() {
		return emailVerificado;
	}
	public void setEmailVerificado(boolean emailVerificado) {
		this.emailVerificado = emailVerificado;
	}
	public String getKeyTemporal() {
		return keyTemporal;
	}
	public void setKeyTemporal(String keyTemporal) {
		this.keyTemporal = keyTemporal;
	}
	public Ciudad getCiudad() {
		return ciudad;
	}
	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public Pais getPais() {
		return pais;
	}
	public void setPais(Pais pais) {
		this.pais = pais;
	}
	public Boolean getFacturaTercero() {
		return facturaTercero;
	}
	public void setFacturaTercero(Boolean facturaTercero) {
		this.facturaTercero = facturaTercero;
	}
	public String getTituloObtenido() {
		return tituloObtenido;
	}
	public void setTituloObtenido(String tituloObtenido) {
		this.tituloObtenido = tituloObtenido;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getEncuesta() {
		return encuesta;
	}
	public void setEncuesta(String encuesta) {
		this.encuesta = encuesta;
	}
	
}
