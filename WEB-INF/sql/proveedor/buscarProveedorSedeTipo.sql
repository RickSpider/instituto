select prov.proveedorid as id, p.nombre||' '||p.apellido as NombreCompleto, p.documentonum as documento
from proveedores prov
join personas p on p.personaid = prov.personaid
where prov.activo = true and prov.sedeid = ?1 and prov.proveedorTipoid = ?2
order by prov.proveedorid asc;