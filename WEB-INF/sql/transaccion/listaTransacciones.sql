Select 

t.transaccionid,
t.fecha,
c.numero,
tt.tipo,
t.monto

from transacciones t
left join cuentas c on c.cuentaid = t.cuentaid
left join tipos tt on tt.tipoid = t.transacciontipoid

order by transaccionid desc;