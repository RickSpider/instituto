select cdc.monto, c.fecha, cdc.cuentanumcr, e.entidad, cdc.transaccionnum, 
case 
when c.personaid is not null then (p.nombre||' '||p.apellido) 
when c.alumnoid is not null then (p2.nombre||' '||p2.apellido)
end, 
c.comprobantenum, 
cdc.emision,
t.tipo   from cobranzas c
join cobranzasdetallescobros cdc on cdc.cobranzaid = c.cobranzaid
join cuentas cu on cu.numero = cdc.cuentanumcr
join entidades e on e.entidadid = cu.entidadid
join tipos t on t.tipoid = c.comprobantetipoid
left join personas p on p.personaid = c.personaid
left join alumnos a on a.alumnoid = c.alumnoid
left join personas p2 on p2.personaid = a.personaid
where c.fecha between '?1' and '?2'
--1 and a.alumnoid = ?3
--2 and p.personaid = ?4
order by cdc.cuentanumcr;
