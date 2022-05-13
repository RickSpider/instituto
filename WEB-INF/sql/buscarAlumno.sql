SELECT 
a.alumnoid,
p.nombre, 
p.apellido
FROM
alumnos a 
left join personas p on p.personaid = a.alumnoid
where a.activo = true
order by a.alumnoid asc;