select c.cobranzaid, c.comprobantenum, c.condicionventatipoid, t.tipo, ec.monto-ec.pago as saldo, ec.estadocuentaid, ec.vencimiento from cobranzas c
join tipos t on t.tipoid = c.condicionventatipoid
join cobranzasdetalles cd on cd.cobranzaid = c.cobranzaid
join estadoscuentas ec on ec.estadocuentaid = cd.estadocuentaid
where c.personaid = ?1 
and t.tipoid = ?2
and cd.monto > 0
and ec.monto-ec.pago > 0
and ec.inactivo = false
order by c.cobranzaid desc;