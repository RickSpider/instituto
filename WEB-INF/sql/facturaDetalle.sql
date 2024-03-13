SELECT 
	case
	when cd.descripcion is not null then cd.descripcion 
	when cd.servicioid is null then co.concepto ||' '|| es.periodo else s.servicio end as descripcion, 
	cd.monto,
	cd.exento,
	cd.iva5,
	cd.iva10
	
FROM cobranzasdetalles cd
	join cobranzas c ON 
	 c.cobranzaid = cd.cobranzaid 
	join estadoscuentas es ON 
	 es.estadocuentaid = cd.estadocuentaid 
	join conceptos co ON 
	 co.conceptoid = es.conceptoid 
	left join servicios s on s.servicioid = cd.servicioid	 
WHERE 
	 cd.cobranzaid = ?1;