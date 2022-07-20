select 
cv.cursovigenteid, 
c.curso,
cv.fechainicio,
cv.fechafin
from cursosvigentes cv
left join cursos c on c.cursoid = cv.cursoid
left join cursosvigentesalumnos cva on cva.cursovigenteid = cv.cursovigenteid
where cva.alumnoid = ?1
order by cv.cursovigenteid;