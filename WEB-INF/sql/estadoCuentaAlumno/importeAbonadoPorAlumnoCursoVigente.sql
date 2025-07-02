Select 
alumnoid, 
sum (pago) as importePagado,
sum (montodescuento) as importeDescuento
from estadoscuentas
where 
alumnoid=?1 and cursovigenteid =?2 and inactivo = false
group by alumnoid;