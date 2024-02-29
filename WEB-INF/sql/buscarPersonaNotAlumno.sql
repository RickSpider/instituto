Select 
p.personaid, 
concat(p.apellido,', ',p.nombre) as fullname, 
p.documentonum,
a.alumnoid
from personas p
left join alumnos a on a.personaid = p.personaid and a.sedeid = ##SEDEID##
where a.alumnoid is null
order by p.personaid asc;
