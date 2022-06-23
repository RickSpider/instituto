select 
m.materiaid, 
m.materia, 
m.descripcion
from cursosmaterias cm
left join materias m on m.materiaid = cm.materiaid
where 
cm.cursoid = ?1 and
m.materiaid not in (select materiaid from cursosvigentesmaterias where cursovigenteid = ?2);