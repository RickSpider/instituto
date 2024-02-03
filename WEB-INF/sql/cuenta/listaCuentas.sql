select c.cuentaid as id, c.numero as Cuenta, e.entidad as entidad
from cuentas c
left join entidades e on e.entidadid = c.entidadid
where c.sedeid = ?1
order by cuentaid asc; 