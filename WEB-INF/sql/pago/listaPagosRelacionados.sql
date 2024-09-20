select TO_CHAR(p.fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha, tcomp.tipo ,p.comprobantenum, (p.total10+ p.total5+ p.totalexento) as total 
from pagos p
join tipos tcomp on tcomp.tipoid = p.comprobantetipoid
where pagorelacionadoid = ?1
order by pagoid desc;