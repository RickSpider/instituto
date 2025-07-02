select cvm.cursovigenteid, cvm.materiaid, m.materia from cursosvigentesmaterias cvm
join materias m on m.materiaid = cvm.materiaid
join tipos tm on tm.tipoid = m.materiatipoid
where cursovigenteid = ?1 and tm.sigla != 'MATERIA_CUOTA'
order by cvm.orden asc;