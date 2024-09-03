select oc.ordencompraid as id, oc.fecha as fecha, pers.razonsocial as proveedor from OrdenesCompras oc
join proveedores prov on prov.proveedorid = oc.proveedorid
join personas pers on pers.personaid = prov.personaid
where estadotipoid = ?1
and fechavencimiento >= current_date
order by ordencompraid desc;