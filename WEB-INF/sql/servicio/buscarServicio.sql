Select s.servicioid as id, s.servicio as servicio, c.concepto as concepto from servicios s
join conceptos c on c.conceptoid = s.conceptoid
order by s.servicioid;