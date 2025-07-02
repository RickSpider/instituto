select e.evaluacionid, e.evaluaciontipoid ,ed.alumnoid, p.nombre ||' '||p.apellido,p.documentonum ,e.cursovigenteid, e.materiaid, m.materia ,cvm.orden ,ed.evaluaciondetalleid, ed.calificacion,TO_CHAR(cva.fechainscripcion, 'DD/MM/YYYY') as Inscripcion  
from evaluacionesdetalles ed
join evaluaciones e on e.evaluacionid = ed.evaluacionid
join cursosvigentesalumnos cva on cva.cursovigenteid = e.cursovigenteid
join cursosvigentesmaterias cvm on cvm.cursovigenteid = e.cursovigenteid and cvm.materiaid = e.materiaid
join materias m on m.materiaid = e.materiaid
join alumnos a on a.alumnoid = ed.alumnoid
join personas p on p.personaid = a.personaid
where e.cursovigenteid = ?1 and e.evaluaciontipoid = ?2
order by p.apellido asc, cvm.orden asc;
