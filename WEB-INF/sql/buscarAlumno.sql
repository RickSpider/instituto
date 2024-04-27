SELECT a.alumnoid as id, p.nombre as nombre, p.apellido as apellido, p.documentonum as documento
FROM alumnos a 
left join personas p on p.personaid = a.personaid
where a.activo = true
and a.sedeid=?1
order by a.alumnoid asc;