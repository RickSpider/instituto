Select 
alumnoid, 
sum(monto - (pago+montodescuento) ) as saldo
from estadoscuentas
where 
alumnoid=?1 and cursovigenteid =?2 
group by alumnoid;