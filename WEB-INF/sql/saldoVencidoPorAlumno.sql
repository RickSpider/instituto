select 
alumnoid, 
sum(monto - (pago+montodescuento) ) as saldo
from estadoscuentas
where 
alumnoid=?1
and vencimiento < current_date
group by alumnoid;