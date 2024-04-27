select
ec.personaid, 
sum(ec.pago) as pago,
sum(ec.monto) as monto,
tipocomp.sigla as comprobante,
tipocond.sigla as condicion
from estadoscuentas ec 
join cobranzasdetalles cd on cd.estadocuentaid = ec.estadocuentaid
join cobranzas c on c.cobranzaid = cd.cobranzaid
join tipos tipocomp on tipocomp.tipoid = c.comprobantetipoid
join tipos tipocond on tipocond.tipoid = c.condicionventatipoid
where 
ec.personaid= ?1 and inactivo = false
and ec.creado between '?2' and '?3'
group by ec.personaid,tipocomp.sigla, tipocond.sigla;