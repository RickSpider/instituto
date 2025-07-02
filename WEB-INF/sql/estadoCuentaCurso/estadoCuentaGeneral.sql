select ec.estadocuentaid, (p.nombre||' '||p.apellido) as Alumno,TO_CHAR(cva.fechainscripcion, 'DD/MM/YYYY') inscripcion,ec.monto, ec.pago, (ec.monto -(ec.montodescuento+ec.pago)) as saldo, ec.inactivo
from estadoscuentas ec
left join alumnos a on a.alumnoid = ec.alumnoid
left join personas p on p.personaid = a.personaid
left join cursosvigentesalumnos cva on cva.cursovigenteid = ec.cursovigenteid and cva.alumnoid = ec.alumnoid
where ec.cursovigenteid = ?1 and ec.conceptoid = ?2 and ec.periodo = ?3
order by ec.estadocuentaid;
