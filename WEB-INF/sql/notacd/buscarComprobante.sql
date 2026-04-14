select 

sd.sifendocumentoid, 
sd.cobranzaid,
c.fecha ,
c.comprobantenum, 
c.ruc as ruc_documento, 
c.razonsocial, 
totaldetalle as monto,
sd.comprobantetipoid

from sifendocumentos sd
join cobranzas c on c.cobranzaid = sd.cobranzaid
join tipos ct on ct.tipoid = sd.comprobantetipoid
where estado like 'Aprobado'
and cancelado = false
and ct.sigla ='COMPROBANTE_FACTURA'
order by sd.sifendocumentoid desc;