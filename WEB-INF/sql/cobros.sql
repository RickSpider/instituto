Select 
c.cobranzaid,
c.fecha,
t.tipo,
c.timbrado,
c.comprobantenum,
case when c.personaid is not null then (p.nombre||' '||p.apellido) 
when c.alumnoid is not null then (p2.nombre||' '||p2.apellido) end as persona,
(case when c.anulado = true then 'Si' else 'No' end) as anulado,
c.fechaanulacion,
c.usuarioanulacion
from cobranzas c
left join tipos t on t.tipoid = c.comprobantetipoid
left join personas p on p.personaid = c.personaid
left join alumnos a on a.alumnoid = c.alumnoid
left join personas p2 on p2.personaid = a.personaid
order by c.cobranzaid desc;