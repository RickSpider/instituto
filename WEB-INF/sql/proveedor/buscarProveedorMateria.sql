select c.curso, m.materia, t.tipo, s.sede, cvm.fechainicio from cursosvigentesmaterias cvm
join cursosvigentes cv on cvm.cursovigenteid = cv.cursovigenteid
join cursos c on c.cursoid = cv.cursoid
join materias m on m.materiaid = cvm.materiaid
join tipos t on t.tipoid = cvm.estadoid
join sedes s on s.sedeid = cv.sedeid
where proveedorid = ?1
and estadoid != 14;