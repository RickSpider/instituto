Select pagoid, TO_CHAR(fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha, pers.razonsocial, timbrado, comprobantenum, 
case when tcomp.sigla like 'COMPROBANTE_FACTURA' then tcomp.tipo||' '|| tcondvent.tipo else tcomp.tipo end as tipocomprobante,
(total10+total5+totalExento) as total
from pagos p
join proveedores pr on pr.proveedorid = p.proveedorid
join personas pers on pers.personaid = pr.personaid
join tipos tcomp on tcomp.tipoid = p.comprobanteTipoID
join tipos tcondvent on tcondvent.tipoid = p.condicionventatipoid
where fecha BETWEEN '?1' AND '?2'
--1 and p.comprobantetipoid = ?3
order by p.fecha desc;