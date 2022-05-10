SELECT 
a.alumnoid,
p.nombre, 
p.apellido
FROM
alumnos a 
left join persona p on p.personaid = a.alumnoid
order by a.alumnoid asc;