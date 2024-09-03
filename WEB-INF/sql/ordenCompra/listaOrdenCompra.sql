Select ordencompraid, fecha, fechavencimiento, pers.razonsocial, t.tipo
from ordenescompras oc
join proveedores p on p.proveedorid = oc.proveedorid
join personas pers on pers.personaid = p.personaid
join tipos t on t.tipoid = oc.estadotipoid
order by oc.ordencompraid asc;