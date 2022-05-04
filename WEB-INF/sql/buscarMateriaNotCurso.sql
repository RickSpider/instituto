select 
m.materiaid, 
m.materia, 
m.descripcion
from materias m
where 
m.materiaid not in (select materiaid from cursosmaterias where cursoid = ?1);