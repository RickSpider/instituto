select c.cuentaid, c.numero, e.entidad
from cuentas c
left join entidades e on e.entidadid = c.entidadid
where c.sedeid = ?1
orderby cuentaid asc; 