select 
c.cobranzaid as cobranzaid, 
c.fecha,
c.comprobantetipoid,
c.comprobantenum, 
c.razonsocial, 
c.ruc,
coalesce(cp.razonsocial,'') as receptor,
tc.tipo as tipocomprobante, 
case c.anulado when false then cd.monto else 0 end as monto, 
c.anulado
from cobranzasdetalles cd
left join cobranzas c on c.cobranzaid = cd.cobranzaid  
--left join tipos t on t.tipoid = cdc.formapagoid
left join tipos tc on tc.tipoid = c.comprobantetipoid
left join personas cp on cp.personaid = c.personaid

where c.cajaid = ?1
and condicionventatipoid = 29
order by c.comprobantetipoid asc;