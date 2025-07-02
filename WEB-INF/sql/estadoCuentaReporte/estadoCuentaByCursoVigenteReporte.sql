select 

ec.vencimiento, 
concat(c.concepto, '/',ec.periodo) as descripcion, 
ec.monto,
ec.estadocuentaid,
ec.alumnoid,
concat(a.nombre,' ',a.apellido) as fullname,
cu.curso

from estadoscuentas ec
left join conceptos c on c.conceptoid = ec.conceptoid
left join alumnos al on al.alumnoid = ec.alumnoid
left join personas a on a.personaid = al.personaid
left join cursosvigentes cv on cv.cursovigenteid = ec.cursovigenteid
left join cursos cu on cu.cursoid = cv.cursoid
where ec.inactivo = false 
and al.alumnoid is not null

--1 and ec.cursovigenteid = ?1 
--2 and ec.alumnoid = ?4

--3 and ec.pago > 0 --pagados
--4 and ec.pago = 0 --vencidos

and ec.vencimiento between '?2' and '?3'
order by ec.alumnoid asc, ec.estadocuentaid asc ;