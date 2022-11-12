select 
cv.cursovigenteid, 
c.curso,
cv.fechainicio,
cv.fechafin
from cursosvigentes cv
left join cursos c on c.cursoid = cv.cursoid
order by cv.cursovigenteid;