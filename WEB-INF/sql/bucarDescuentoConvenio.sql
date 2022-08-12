SELECT 
cv.cursovigenteid, --0
cvc.convenioid, --1
c.descripcion, --2
co.concepto, --3
cc.importe, --4
cc.porcentaje, --5
cc.periodos --6

FROM cursosvigentes cv
left join cursosvigentesconvenios cvc on cvc.cursovigenteid = cv.cursovigenteid
left join convenios c on c.convenioid = cvc.convenioid
left join conveniosalumnos ca on ca.convenioid = c.convenioid
left join conveniosconceptos cc on cc.convenioid = c.convenioid
left join conceptos co on co.conceptoid = cc.conceptoid

WHERE 
c.activo = 'true' 
and c.fechainicio <= CURRENT_DATE 
and c.fechafin >= CURRENT_DATE 
and cv.cursovigenteid = ?1
and co.conceptoid= ?2 
and ca.alumnoid = ?3
order by convenioid desc;