select 
alumnoid, 
sum(monto - (pago+montodescuento) ) as saldo
from estadoscuentas
where 
alumnoid=?1 and cursovigenteid = ?2 
and vencimiento < current_date and inactivo = false
group by alumnoid;