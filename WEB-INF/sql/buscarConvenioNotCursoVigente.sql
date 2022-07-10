select 
c.convenioid,  
c.descripcion
from convenios c
where 
c.convenioid not in (select convenioid from cursosvigentesconvenios where cursovigenteid = ?1)
and c.activo = true and fechainicio < CURRENT_DATE AND fechafin > CURRENT_DATE;