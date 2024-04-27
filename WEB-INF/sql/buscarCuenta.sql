select cuentaid as id, e.entidad as entidad, c.numero as cuenta 
from cuentas c
join entidades e on e.entidadid = c.entidadid
where c.sedeid = ?1 
order by cuentaid asc;