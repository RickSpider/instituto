SELECT pagoid, proveedorid, fecha, comprobanteid, condicionventaid ,tipo_documento, comprobantenum, total, relacionado_con, razonsocial 
FROM (
    SELECT p.pagoid, p.proveedorid,
           p.fecha, 
           tcomp.tipoid as comprobanteid,
		   tcond.tipoid as condicionventaid,
            case when tcomp.sigla = 'COMPROBANTE_FACTURA' then tcomp.tipo || ' ' || tcond.tipo else 'Recibo' end as tipo_documento, 
           p.comprobantenum, 
           (p.total10 + p.total5 + p.totalexento) AS total, 
           NULL AS relacionado_con,
	   pers.razonsocial as razonsocial
    FROM pagos p
    JOIN tipos tcomp ON tcomp.tipoid = p.comprobantetipoid
    JOIN tipos tcond ON tcond.tipoid = p.condicionventatipoid
	join proveedores prov on prov.proveedorid = p.proveedorid
	join personas pers on pers.personaid = prov.personaid
    WHERE p.pagorelacionadoid IS NULL  -- Traemos los pagos sin relación primero
	AND p.fecha BETWEEN '?1' AND '?2'
	--#1 AND p.proveedorid = ?3
	
    UNION ALL

    SELECT pr.pagoid, pr.proveedorid,
           pr.fecha, 
           tcomp.tipoid as comprobanteid,
		   tcond.tipoid as condicionventaid,
            case when tcomp.sigla = 'COMPROBANTE_FACTURA' then tcomp.tipo || ' ' || tcond.tipo else 'Recibo' end AS tipo_documento, 
           pr.comprobantenum, 
           (pr.total10 + pr.total5 + pr.totalexento) AS total, 
           pr.pagorelacionadoid AS relacionado_con,
	   pers.razonsocial as razonsocial
    FROM pagos pr
    JOIN tipos tcomp ON tcomp.tipoid = pr.comprobantetipoid
    JOIN tipos tcond ON tcond.tipoid = pr.condicionventatipoid
	join proveedores prov on prov.proveedorid = pr.proveedorid
	join personas pers on pers.personaid = prov.personaid
    WHERE pr.pagorelacionadoid IS NOT NULL -- Traemos los pagos relacionados
	AND pr.fecha BETWEEN '?1' AND '?2'
	--#1 AND pr.proveedorid = ?3
	
	
) AS pagos_union
ORDER BY proveedorid, COALESCE(relacionado_con, pagoid), relacionado_con IS NULL DESC, fecha asc;