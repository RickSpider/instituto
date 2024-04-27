select cv.cursovigenteid as id, c.curso as curso, cv.fechainicio as fechaIni, cv.fechafin as fechafin
from cursosvigentes cv
left join cursos c on c.cursoid = cv.cursoid
left join cursosvigentesalumnos cva on cva.cursovigenteid = cv.cursovigenteid
where cva.alumnoid = ?1
order by cv.cursovigenteid desc;