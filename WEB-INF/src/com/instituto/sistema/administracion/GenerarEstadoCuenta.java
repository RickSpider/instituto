package com.instituto.sistema.administracion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.doxacore.util.Register;
import com.instituto.modelo.CursoVigenteAlumno;
import com.instituto.modelo.CursoVigenteConcepto;
import com.instituto.modelo.CursoVigenteConvenio;
import com.instituto.modelo.Feriado;
import com.instituto.modelo.EstadoCuenta;

public class GenerarEstadoCuenta {

	private Register reg = new Register();

	public List<EstadoCuenta> generarMovimientoAlumno(CursoVigenteAlumno cva, List<CursoVigenteConcepto> lCVConceptos) {

		List<Feriado> lFeriados = this.reg.getAllObjects(Feriado.class.getName());

		String sql = "select \n" + "cvm.orden,\n" + "count(cvm.orden) as cantidadMaterias\n"
				+ "from cursosvigentesmaterias cvm\n" + "left join materias m on m.materiaid = cvm.materiaid\n"
				+ "left join tipos t on t.tipoid = m.materiatipoid\n" + "where \n" + "cursovigenteid = "
				+ cva.getCursoVigente().getCursovigenteid() + " and \n" + "t.sigla = 'MATERIA_AULA'\n"
				+ "group by cvm.orden\n" + "order by cvm.orden asc";

		List<Object[]> lOrdenMaterias = this.reg.sqlNativo(sql);

		ArrayList<EstadoCuenta> out = new ArrayList<EstadoCuenta>();

		for (CursoVigenteConcepto x : lCVConceptos) {

			boolean esIgual = false;

			if (x.getConcepto().getConcepto().compareTo("MATRICULA") == 0) {

				esIgual = true;

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(cva.getCursoVigente().getFechaInicio());
				int diaVencimiento = calendar.get(Calendar.DAY_OF_MONTH);

				int periodoEducativo = cva.getCursoVigente().getCurso().getPeriodoEducativo();

				int cantMeses = lOrdenMaterias.size() / periodoEducativo;

				for (int i = 0; i < periodoEducativo; i++) {

					EstadoCuenta ec = new EstadoCuenta();
					ec.setCursoVigente(cva.getCursoVigente());
					ec.setAlumno(cva.getAlumno());
					ec.setConcepto(x.getConcepto());
					ec.setPeriodo(i + 1);
					ec.setMonto(x.getImporte());

					calendar.set(Calendar.DAY_OF_MONTH, diaVencimiento);
					calendar = calculoVencimiento(calendar, lFeriados);

					ec.setVencimiento(calendar.getTime());

					out.add(ec);

					calendar.add(Calendar.MONTH, cantMeses);

				}

			}

			if (x.getConcepto().getConcepto().compareTo("MODULO") == 0) {

				esIgual = true;

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(cva.getCursoVigente().getFechaInicio());
				int diaVencimiento = calendar.get(Calendar.DAY_OF_MONTH);

				for (int i = 0; i < lOrdenMaterias.size(); i++) {

					int cantidadMaterias = Integer.parseInt(lOrdenMaterias.get(i)[1].toString());

					EstadoCuenta ec = new EstadoCuenta();
					ec.setCursoVigente(cva.getCursoVigente());
					ec.setAlumno(cva.getAlumno());
					ec.setConcepto(x.getConcepto());
					ec.setPeriodo(i + 1);
					ec.setMonto(x.getImporte() * cantidadMaterias);

					calendar.set(Calendar.DAY_OF_MONTH, diaVencimiento);
					calendar = calculoVencimiento(calendar, lFeriados);

					ec.setVencimiento(calendar.getTime());

					out.add(ec);

					calendar.add(Calendar.MONTH, 1);

				}

			}

			if (x.getConcepto().getConcepto().compareTo("TALLER") == 0) {

				esIgual = true;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(cva.getCursoVigente().getFechaInicio());
				int diaVencimiento = calendar.get(Calendar.DAY_OF_MONTH);

				String sqlTalleres = "select \n" + "cvm.orden,\n" + "count(cvm.orden) as cantidadMaterias\n"
						+ "from cursosvigentesmaterias cvm\n" + "left join materias m on m.materiaid = cvm.materiaid\n"
						+ "left join tipos t on t.tipoid = m.materiatipoid\n" + "where \n" + "cursovigenteid = "
						+ cva.getCursoVigente().getCursovigenteid() + " and \n" + "t.sigla = 'MATERIA_TALLER'\n"
						+ "group by cvm.orden\n" + "order by cvm.orden asc;";

				List<Object[]> lOrdenTalleres = this.reg.sqlNativo(sqlTalleres);

				int ordenMayor = Integer.parseInt(lOrdenTalleres.get(lOrdenTalleres.size() - 1)[0].toString());

				List<Date> fechasPosibles = new ArrayList<Date>();

				for (int i = 0; i < ordenMayor; i++) {

					calendar.set(Calendar.DAY_OF_MONTH, diaVencimiento);
					calendar = calculoVencimiento(calendar, lFeriados);

					fechasPosibles.add(calendar.getTime());

					calendar.add(Calendar.MONTH, 1);

				}

				for (int i = 0; i < lOrdenTalleres.size(); i++) {

					int ordenTaller = Integer.parseInt(lOrdenTalleres.get(i)[0].toString());
					int cantMaterias = Integer.parseInt(lOrdenTalleres.get(i)[1].toString());

					for (int j = 0; j < cantMaterias; j++) {

						EstadoCuenta ec = new EstadoCuenta();
						ec.setCursoVigente(cva.getCursoVigente());
						ec.setAlumno(cva.getAlumno());
						ec.setConcepto(x.getConcepto());
						ec.setPeriodo((i + 1));
						ec.setMonto(x.getImporte());
						ec.setVencimiento(fechasPosibles.get(ordenTaller - 1));
						out.add(ec);

					}

				}

			}

			if (x.getConcepto().getConcepto().compareTo("TITULO") == 0) {
				
				esIgual = true;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(cva.getCursoVigente().getFechaInicio());
				int diaVencimiento = calendar.get(Calendar.DAY_OF_MONTH);

				String sqlTalleres = "select \n" + "cvm.orden,\n" + "count(cvm.orden) as cantidadMaterias\n"
						+ "from cursosvigentesmaterias cvm\n" + "left join materias m on m.materiaid = cvm.materiaid\n"
						+ "left join tipos t on t.tipoid = m.materiatipoid\n" + "where \n" + "cursovigenteid = "
						+ cva.getCursoVigente().getCursovigenteid() + " \n" + "group by cvm.orden\n"
						+ "order by cvm.orden asc;";

				List<Object[]> lOrdenTalleres = this.reg.sqlNativo(sqlTalleres);

				int ordenMayor = Integer.parseInt(lOrdenTalleres.get(lOrdenTalleres.size() - 1)[0].toString());

				List<Date> fechasPosibles = new ArrayList<Date>();

				for (int i = 0; i < ordenMayor+1; i++) {

					calendar.set(Calendar.DAY_OF_MONTH, diaVencimiento);
					calendar = calculoVencimiento(calendar, lFeriados);

					fechasPosibles.add(calendar.getTime());

					calendar.add(Calendar.MONTH, 1);

				}

				EstadoCuenta ec = new EstadoCuenta();
				ec.setCursoVigente(cva.getCursoVigente());
				ec.setAlumno(cva.getAlumno());
				ec.setConcepto(x.getConcepto());
				ec.setPeriodo(1);
				ec.setMonto(x.getImporte());
				
				ec.setVencimiento(fechasPosibles.get(fechasPosibles.size()-1));
				out.add(ec);

			}

			/*
			 * if (!esIgual) {
			 * 
			 * Calendar calendar = Calendar.getInstance();
			 * calendar.setTime(cva.getCursoVigente().getFechaInicio()); int diaVencimiento
			 * = calendar.get(Calendar.DAY_OF_MONTH);
			 * 
			 * 
			 * EstadoCuenta ec = new EstadoCuenta();
			 * ec.setCursoVigente(cva.getCursoVigente()); ec.setAlumno(cva.getAlumno());
			 * ec.setConcepto(x.getConcepto()); ec.setPeriodo(1);
			 * ec.setMonto(x.getImporte());
			 * 
			 * calendar.set(Calendar.DAY_OF_MONTH, diaVencimiento); calendar =
			 * calculoVencimiento(calendar, lFeriados);
			 * 
			 * ec.setVencimiento(calendar.getTime());
			 * 
			 * out.add(ec);
			 * 
			 * calendar.add(Calendar.MONTH, 1);
			 * 
			 * }
			 */

		}

		return out;

	}

	private Calendar calculoVencimiento(Calendar calendar, List<Feriado> lFeriados) {

		if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {

			calendar.add(Calendar.MONTH, 1);

		}

		for (int i = 0; i < lFeriados.size(); i++) {

			if (calendar.get(Calendar.MONTH) == lFeriados.get(i).getMes()
					&& calendar.get(Calendar.DAY_OF_MONTH) == lFeriados.get(i).getDia()) {

				calendar.add(Calendar.DAY_OF_WEEK, 1);

				i = 0;

			}

		}

		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

			calendar.add(Calendar.DAY_OF_WEEK, 1);

		}

		return calendar;
	}

}
