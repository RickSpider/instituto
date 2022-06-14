SELECT 
c.conceptoid,
c.concepto
FROM
conceptos c
where conceptoid not in (select conceptoid from cursosvigentesconceptos where cursovigenteid = ?1 )
order by c.conceptoid asc;