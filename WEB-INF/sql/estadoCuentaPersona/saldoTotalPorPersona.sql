select
personaid, 
sum(monto - (pago+montodescuento) ) as saldo
from estadoscuentas
where 
personaid=?1 and inactivo = false
and monto > 0
group by personaid;