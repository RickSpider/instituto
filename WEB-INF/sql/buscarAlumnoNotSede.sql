SELECT a.alumnoid as id,  p.documentonum as documento,(p.nombre||' '||p.apellido) as Alumno
FROM alumnos a 
left join personas p on p.personaid = a.personaid
where a.activo = true
order by a.alumnoid asc;