select 
c.cobranzaid as cobranzaid, 
c.fecha,
c.comprobantetipoid,
c.comprobantenum, 
c.razonsocial, 
c.ruc,
(p.nombre||' '||p.apellido) as alumno,
tc.tipo as tipocomprobante, 
case c.anulado when false then cdc.monto else 0 end as monto, 
cdc.formapagoid as formapagoid, 
t.tipo as formapago,
c.anulado
from cobranzasdetallescobros cdc
left join cobranzas c on c.cobranzaid = cdc.cobranzaid  
left join tipos t on t.tipoid = cdc.formapagoid
left join tipos tc on tc.tipoid = c.comprobantetipoid
left join alumnos a on a.alumnoid = c.alumnoid
left join personas p on p.personaid = a.personaid
where c.cajaid = ?1 
order by c.comprobantetipoid asc, cdc.formapagoid asc;