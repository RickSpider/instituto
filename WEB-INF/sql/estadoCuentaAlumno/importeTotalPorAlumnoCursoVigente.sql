Select 
alumnoid, 
sum(monto) as importeTotal
from estadoscuentas
where 
alumnoid=?1 and cursovigenteid =?2 and inactivo = false
group by alumnoid;