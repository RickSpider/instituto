SELECT 
p.personaid, 
concat(p.apellido,', ',p.nombre) as fullname, 
p.documentonum
from personas p
left join alumnos a on a.personaid = p.personaid and a.sedeid != ##SEDEID##
LEFT JOIN alumnos a_current ON a_current.personaid = p.personaid AND a_current.sedeid = ##SEDEID##
where a.alumnoid is not null 
and a_current.personaid IS NULL
order by p.personaid;
