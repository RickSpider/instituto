Select 
alumnoid, 
sum(monto - (pago+montodescuento) ) as saldo
from estadoscuentas
where 
alumnoid=?1 and inactivo = false
group by alumnoid;