select 
cva.alumnoid, 
p.apellido, 
p.nombre,
p.documentonum, 
p.telefono,
concat(p.apellido, ' ',p.nombre) as fullname
from cursosvigentesalumnos cva
left join alumnos a on a.alumnoid = cva.alumnoid
left join personas p on p.personaid = a.alumnoid
where cursovigenteid = ?1
order by p.apellido asc;

