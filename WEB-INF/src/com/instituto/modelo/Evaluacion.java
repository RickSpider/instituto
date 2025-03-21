package com.instituto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.doxacore.modelo.Modelo;
import com.doxacore.modelo.Tipo;

@Entity
@Table(name="evaluaciones")
public class Evaluacion extends Modelo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8768247911793426256L;

	@Id
	@Column(name ="evaluacionid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long evaluacionid;
	
	@ManyToOne
	@JoinColumn(name="docenteid", nullable = false)
	private Proveedor docente;
	
	@CreationTimestamp 
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha = new Date();
	
	@ManyToOne
	@JoinColumn(name="evaluaciontipoid", nullable = false)
	private Tipo evaluacionTipo;
	
	/*@ManyToOne
	@JoinColumn(name="cursovigenteid", nullable = false)
	private CursoVigente cursovigente;
	
	@ManyToOne
	@JoinColumn(name="materiaid", nullable = false)
	private Materia materia;*/
	
	@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "cursovigenteid", referencedColumnName = "cursovigenteid"),
        @JoinColumn(name = "materiaid", referencedColumnName = "materiaid")
    })
	private CursoVigenteMateria cursoVigenteMateria;
	
	@OrderBy("evaluaciondetalleid ASC")
	@OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EvaluacionDetalle> detalles = new ArrayList<EvaluacionDetalle>();
	
	
	private double totalProceso1 = 0;
	private double totalProceso2 = 0;
	private double totalProceso3 = 0;
	private double totalProceso4 = 0;
	private double totalProceso5 = 0;
	private double totalfinal = 0;
	
	@ColumnDefault("false")
	private boolean finalizadoDocente = false;
	
	@ColumnDefault("false")
	private boolean finalizado = false;
	
	
	
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

	public Long getEvaluacionid() {
		return evaluacionid;
	}

	public void setEvaluacionid(Long evaluacionid) {
		this.evaluacionid = evaluacionid;
	}

	public Proveedor getDocente() {
		return docente;
	}

	public void setDocente(Proveedor docente) {
		this.docente = docente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Tipo getEvaluacionTipo() {
		return evaluacionTipo;
	}

	public void setEvaluacionTipo(Tipo evaluacionTipo) {
		this.evaluacionTipo = evaluacionTipo;
	}

	public List<EvaluacionDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<EvaluacionDetalle> detalles) {
		this.detalles = detalles;
	}

	public double getTotalProceso1() {
		return totalProceso1;
	}

	public void setTotalProceso1(double totalProceso1) {
		this.totalProceso1 = totalProceso1;
	}

	public double getTotalProceso2() {
		return totalProceso2;
	}

	public void setTotalProceso2(double totalProceso2) {
		this.totalProceso2 = totalProceso2;
	}

	public double getTotalProceso3() {
		return totalProceso3;
	}

	public void setTotalProceso3(double totalProceso3) {
		this.totalProceso3 = totalProceso3;
	}

	public double getTotalProceso4() {
		return totalProceso4;
	}

	public void setTotalProceso4(double totalProceso4) {
		this.totalProceso4 = totalProceso4;
	}

	public double getTotalProceso5() {
		return totalProceso5;
	}

	public void setTotalProceso5(double totalProceso5) {
		this.totalProceso5 = totalProceso5;
	}

	public double getTotalfinal() {
		return totalfinal;
	}

	public void setTotalfinal(double totalfinal) {
		this.totalfinal = totalfinal;
	}

	public CursoVigenteMateria getCursoVigenteMateria() {
		return cursoVigenteMateria;
	}

	public void setCursoVigenteMateria(CursoVigenteMateria cursoVigenteMateria) {
		this.cursoVigenteMateria = cursoVigenteMateria;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

	public boolean isFinalizadoDocente() {
		return finalizadoDocente;
	}

	public void setFinalizadoDocente(boolean finalizadoDocente) {
		this.finalizadoDocente = finalizadoDocente;
	}
	
	
	
}
