Select 
io.inscriptoonlineid,
c.curso,
io.fechaInscripcion,
io.nombre||' '|| io.apellido,
io.ci,
io.telefono,
io.email,
io.emailverificado

from inscriptosonline io
left join cursosvigentes cv on cv.cursovigenteid = io.cursovigenteid
left join cursos c on c.cursoid = cv.cursoid
order by io.inscriptoonlineid desc;