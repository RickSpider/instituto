select d.proveedorid, p.razonsocial, p.ruc,  tp.tipo,
case when d.activo then 'Si' else 'No' end as activo
from proveedores d
join personas p on p.personaid = d.personaid
join tipos tp on tp.tipoid = d.proveedorTipoId
where d.sedeid = ?1
order by d.proveedorid asc;