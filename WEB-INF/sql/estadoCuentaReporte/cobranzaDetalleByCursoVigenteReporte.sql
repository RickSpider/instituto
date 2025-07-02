select 
c.fecha, 
concat(ct.tipo,' ',c.comprobantenum) as descripcion, 
cd.monto,
cd.estadocuentaid,
cd.montodescuento 
from cobranzasdetalles cd

left join cobranzas c on c.cobranzaid = cd.cobranzaid
left join tipos ct on ct.tipoid = c.comprobantetipoid

where 
cd.estadocuentaid in (select estadocuentaid from estadoscuentas 
			where  inactivo = false 
			--1 and cursovigenteid = ?1 
			--2 and alumnoid = ?4

			--3 and pago > 0 --pagados
			--4 and pago = 0 --vencidos

			and vencimiento between '?2' and '?3' 
			order by estadocuentaid)
and c.anulado = false
order by cd.estadocuentaid asc;