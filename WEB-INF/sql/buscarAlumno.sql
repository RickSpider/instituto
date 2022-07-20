SELECT 
a.alumnoid,
p.nombre, 
p.apellido,
p.documentonum
FROM
alumnos a 
left join personas p on p.personaid = a.alumnoid
where a.activo = true
and a.sedeid=?1
order by a.alumnoid asc;