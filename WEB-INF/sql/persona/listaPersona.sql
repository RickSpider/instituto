select p.personaid, p.nombre, p.apellido, p.documentonum, p.direccion, c.ciudad, gd.gradoacademico, i.institucion  
from personas p
left join ciudades c on c.ciudadid = p.ciudadid
left join gradosacademicos gd on gd.gradoacademicoid = p.gradoacademicoid
left join instituciones i on i.institucionid = p.institucionid
order by personaid asc
;