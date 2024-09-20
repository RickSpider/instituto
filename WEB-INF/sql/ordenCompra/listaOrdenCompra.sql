Select oc.ordencompraid, oc.fecha , TO_CHAR(oc.fechavencimiento, 'DD/MM/YYYY') as vencimiento, pers.razonsocial, t.tipo, sum(ocd.monto)
from ordenescompras oc
join proveedores p on p.proveedorid = oc.proveedorid
join personas pers on pers.personaid = p.personaid
join tipos t on t.tipoid = oc.estadotipoid
join ordenescomprasdetalles ocd on ocd.ordencompraid = oc.ordencompraid
--1 where ?1 ?2 '?3' 
group by oc.ordencompraid, oc.fecha, oc.fechavencimiento, pers.razonsocial, t.tipo
order by oc.ordencompraid desc;