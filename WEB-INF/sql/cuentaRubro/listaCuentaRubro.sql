SELECT cr.cuentarubroid, cr.cuentarubro, r.rubro
from cuentasrubros cr
join rubros r on r.rubroid = cr.rubroid
order by cr.cuentarubroid asc;