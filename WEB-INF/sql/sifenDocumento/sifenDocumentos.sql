select 

sd.sifendocumentoid,
c.fecha ,
sd.cdc, 
c.comprobantenum, 
sd.enviado, 
sd.estado, 
t.tipo

from sifendocumentos sd
join cobranzas c on c.cobranzaid = sd.cobranzaid
join tipos t on c.comprobantetipoid = t.tipoid
order by sd.sifendocumentoid desc;
