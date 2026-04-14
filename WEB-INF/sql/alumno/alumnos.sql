SELECT 
    a.alumnoid, 
    p.nombre, 
    p.apellido, 
    p.documentonum, 
    c.ciudad, 
    gc.gradoacademico, 
    i.institucion,
    CASE WHEN a.activo THEN 'Si' ELSE 'No' END AS activo,
    COALESCE(COUNT(cva.cursovigenteid), 0) AS cantidadCursos
FROM alumnos a
JOIN personas p ON p.personaid = a.personaid
LEFT JOIN ciudades c ON c.ciudadid = p.ciudadid
LEFT JOIN gradosacademicos gc ON gc.gradoacademicoid = p.gradoacademicoid
LEFT JOIN instituciones i ON i.institucionid = p.institucionid
LEFT JOIN cursosvigentesalumnos cva ON cva.alumnoid = a.alumnoid
WHERE a.sedeid = ?1
GROUP BY 
    a.alumnoid, p.nombre, p.apellido, p.documentonum,
    c.ciudad, gc.gradoacademico, i.institucion, a.activo
ORDER BY a.alumnoid ASC;