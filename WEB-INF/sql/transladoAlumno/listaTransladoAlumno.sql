select t.transladoid, t.fecha, t.transladoNro ,p.documentonum ,p.apellido||', '||p.nombre as alumno, cori.curso as origen, cdest.curso as destino
from translados t
left join alumnos a on a.alumnoid = t.alumnoid
left join personas p on p.personaid = a.personaid
left join cursosvigentes cvori on cvori.cursovigenteid = t.cursovigenteorigenid
left join cursos cori on cori.cursoid = cvori.cursoid
left join cursosvigentes cvdest on cvdest.cursovigenteid = t.cursovigentedestinoid
left join cursos cdest on cdest.cursoid = cvdest.cursoid
where a.sedeid = ?1
order by t.transladoid desc;