select 
	cv.cursovigenteid as id,
 	c.curso as curso, 
 	TO_CHAR(cv.fechainicio, 'DD/MM/YYYY') as fechaini, 
 	TO_CHAR(cv.fechafin, 'DD/MM/YYYY') as fechafin, 
	cv.dias
from cursosvigentes cv
left join cursos c on c.cursoid = cv.cursoid
where cv.sedeid = ?1
and cv.inscripcionOnline = true
order by cv.cursovigenteid desc;