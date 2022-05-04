select 
m.materiaid, 
m.materia, 
m.descripcion, 
cm.materiaid as cmmateriaid
from materias m
left join cursosmaterias cm on cm.materiaid = m.materiaid
where cm.cursoid != ?1 or cm.materiaid is null;;
