Select 
cursovigenteid, 
sum(monto - (pago+montodescuento) ) as saldo
from estadoscuentas
where 
cursovigenteid = ?1 and inactivo = false
group by cursovigenteid;