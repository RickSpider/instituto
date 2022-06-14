SELECT 
c.conceptoid,
c.concepto
FROM
conceptos c
where conceptoid not in (select conceptoid from conveniosconceptos where convenioid = ?1 )
order by c.conceptoid asc;