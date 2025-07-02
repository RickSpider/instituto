select cdc.monto, c.fecha, cdc.cuentanumcr, e.entidad, cdc.transaccionnum, 
case 
when c.personaid is not null then (p.nombre||' '||p.apellido) 
when c.alumnoid is not null then (p2.nombre||' '||p2.apellido)
end, 
c.comprobantenum, 
cdc.emision,
t.tipo,
cdc.cuentanum as cuentaDB, 
edb.entidad as entidadDB   
from cobranzas c
left join cobranzasdetallescobros cdc on cdc.cobranzaid = c.cobranzaid
left join cuentas cu on cu.numero = cdc.cuentanumcr
left join entidades e on e.entidadid = cu.entidadid
left join entidades edb on edb.entidadid = cdc.entidadid
left join tipos t on t.tipoid = c.comprobantetipoid
left join personas p on p.personaid = c.personaid
left join alumnos a on a.alumnoid = c.alumnoid
left join personas p2 on p2.personaid = a.personaid
where c.fecha between '?1' and '?2'
and c.anulado = false
--1 and a.alumnoid = ?3
--2 and p.personaid = ?4
and (condicionventatipoid = ?5 OR condicionventatipoid is null)
order by cdc.cuentanumcr, c.fecha;
