SELECT 
a.alumnoid,
p.nombre, 
p.apellido,
p.documentonum
FROM
alumnos a 
left join personas p on p.personaid = a.alumnoid
where a.activo = true and a.alumnoid not in (select alumnoid from cursosvigentesalumnos where cursovigenteid = ?1)
order by a.alumnoid asc;