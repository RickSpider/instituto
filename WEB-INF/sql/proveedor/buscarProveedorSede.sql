select d.proveedorid as id, p.nombre||' '||p.apellido as NombreCompleto, p.documentonum as documento, tp.tipo as tipo
from proveedores d
join personas p on p.personaid = d.personaid
join tipos tp on tp.tipoid = d.proveedorTipoId
where d.activo = true and d.sedeid = ?1
order by d.proveedorid asc;