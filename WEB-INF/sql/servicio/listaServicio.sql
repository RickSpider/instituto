Select s.servicioid, s.servicio, c.concepto from servicios s
join conceptos c on c.conceptoid = s.conceptoid
order by s.servicioid;