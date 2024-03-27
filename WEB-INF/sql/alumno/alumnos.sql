select a.alumnoid, p.nombre, p.apellido, p.documentonum, c.ciudad, gc.gradoacademico, i.institucion, 
case when a.activo then 'Si' else 'No' end as activo
from alumnos a
join personas p on p.personaid = a.personaid
left join ciudades c on c.ciudadid = p.ciudadid
left join gradosacademicos gc on gc.gradoacademicoid = p.gradoacademicoid
left join instituciones i on i.institucionid = p.institucionid
where a.sedeid = ?1
order by alumnoid asc;