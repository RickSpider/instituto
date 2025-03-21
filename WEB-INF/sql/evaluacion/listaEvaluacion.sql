Select e.evaluacionid, e.fecha, p.nombre||' '||p.apellido, et.tipo, c.curso ,m.materia
from evaluaciones e
left join proveedores prov on prov.proveedorid = e.docenteid
left join personas p on p.personaid = prov.personaid
left join tipos et on et.tipoid = e.evaluaciontipoid
left join cursosvigentes cv on cv.cursovigenteid = e.cursovigenteid
left join cursos c on c.cursoid = cv.cursoid
left join materias m on m.materiaid = e.materiaid
--1 where proveedorid = ?1
order by evaluacionid Asc;