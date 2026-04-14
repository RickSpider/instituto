select 

sd.sifendocumentoid,
case when c.fecha is not null then c.fecha else ncd.fecha end as fecha,
sd.cdc, 
case when c.comprobantenum is not null then c.comprobantenum else ncd.comprobantenum end as comprobantenum,
sd.enviado, 
sd.estado, 
case when sd.cancelado = true then 'SI' else 'NO' end as cancelado, 
to_char(sd.canceladofecha, 'DD/MM/YYYY HH24:MI:SS') as canceladofecha,
t.tipo


from sifendocumentos sd
left join cobranzas c on c.cobranzaid = sd.cobranzaid
left join tipos t on sd.comprobantetipoid = t.tipoid
left join notacds ncd on ncd.notacdid = sd.notacdid
order by sd.sifendocumentoid desc;