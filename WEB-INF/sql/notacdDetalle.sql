SELECT 
	case
	when ncdd.descripcion is not null then ncdd.descripcion 
	when ncdd.servicioid is null then co.concepto ||' '|| es.periodo else s.servicio end as descripcion, 
	ncdd.monto,
	ncdd.exento,
	ncdd.iva5,
	ncdd.iva10
	
FROM notacddetalles ncdd
	join notacds ncd ON 
	 ncd.notacdid = ncdd.notacdid
	join estadoscuentas es ON 
	 es.estadocuentaid = ncdd.estadocuentaid 
	join conceptos co ON 
	 co.conceptoid = es.conceptoid 
	left join servicios s on s.servicioid = ncdd.servicioid	 
WHERE 
	 ncd.notacdid = ?1;