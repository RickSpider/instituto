SELECT cuentarubroid as id, cuentarubro as cuenta ,r.rubro as rubro
from cuentasrubros c
join rubros r on r.rubroid = c.rubroid
order by c.cuentarubroid asc;