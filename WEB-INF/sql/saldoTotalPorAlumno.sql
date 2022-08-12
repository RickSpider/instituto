Select 
alumnoid, 
sum(monto - (pago+montodescuento) ) as saldo
from estadoscuentas
where 
alumnoid=?1
group by alumnoid;