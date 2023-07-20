Select 
c.cobranzaid,
c.fecha,
t.tipo,
c.timbrado,
c.comprobantenum,
(case when c.anulado = true then 'Si' else 'No' end) as anulado,
c.fechaanulacion,
c.usuarioanulacion
from cobranzas c
left join tipos t on t.tipoid = c.comprobantetipoid
order by c.cobranzaid desc;