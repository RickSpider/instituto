SELECT 
p.personaid, 
concat(p.apellido,', ',p.nombre) as fullname, 
p.documentonum, a.alumnoid
from personas p
left join alumnos a on a.personaid = p.personaid 
where a.sedeid != ##SEDEID##
order by personaid;