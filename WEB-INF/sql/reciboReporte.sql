SELECT co.concepto,
	es.periodo,
	cd.monto
FROM cobranzasdetalles cd
	left join estadoscuentas es on es.estadocuentaid = cd.estadocuentaid
	left join conceptos co on co.conceptoid = es.conceptoid
WHERE 
	 cd.cobranzaid = ?1;