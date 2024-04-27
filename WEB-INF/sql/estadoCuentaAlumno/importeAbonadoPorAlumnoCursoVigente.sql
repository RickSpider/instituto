Select 
alumnoid, 
sum (pago+montodescuento) as importePagado
from estadoscuentas
where 
alumnoid=?1 and cursovigenteid =?2 and inactivo = false
group by alumnoid;