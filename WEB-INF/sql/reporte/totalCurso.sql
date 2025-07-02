select 
	c.cobranzaid,  
	TO_CHAR(c.fecha, 'DD/MM/YYYY') as fecha,
	cd.estadocuentaid, 
	ec.cursovigenteid, 
	cur.curso,
	tcomp.tipoid,
	tcomp.tipo, 
	comprobantenum, 
	alu.alumnoid,
	pers.apellido ||', '||pers.nombre as alumno, 
	con.concepto||' '||ec.periodo as concepto,
	cd.monto, 
	cd.montodescuento  
from cobranzasdetalles cd 
join cobranzas c on c.cobranzaid = cd.cobranzaid
join estadoscuentas ec on ec.estadocuentaid = cd.estadocuentaid
join alumnos alu on alu.alumnoid = ec.alumnoid
join personas pers on pers.personaid = alu.personaid
join cursosvigentes cv on cv.cursovigenteid = ec.cursovigenteid
join cursos cur on cur.cursoid = cv.cursoid
join tipos tcomp on tcomp.tipoid = c.comprobantetipoid
join conceptos con on con.conceptoid = ec.conceptoid 
where c.anulado = false 
and cv.cursovigenteid in (?1) 
--2 and tcomp.tipoid = ?2
--3 and c.fecha between '?3' and '?4'
and cv.sedeid = ?5
order by ec.cursovigenteid desc,  alu.alumnoid asc, c.cobranzaid asc, estadocuentaid asc;