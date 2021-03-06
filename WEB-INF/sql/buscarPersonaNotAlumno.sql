SELECT 
p.personaid, 
concat(p.apellido,', ',p.nombre) as fullname, 
p.documentonum, a.alumnoid
from personas p
left join alumnos a on a.alumnoid = p.personaid 
where alumnoid is null;