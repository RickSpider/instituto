SELECT co.concepto,
	es.periodo,
	cd.monto,
	cd.exento,
	cd.iva5,
	cd.iva10
	
	FROM cobranzasdetalles cd
	left join cobranzas c ON 
	 c.cobranzaid = cd.cobranzaid 
	left join estadoscuentas es ON 
	 es.estadocuentaid = cd.estadocuentaid 
	left join conceptos co ON 
	 co.conceptoid = es.conceptoid 
WHERE 
	 cd.cobranzaid = ?1