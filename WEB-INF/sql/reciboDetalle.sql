SELECT case when cd.servicioid is null then co.concepto ||' '|| es.periodo else s.servicio end as descripcion,
	cd.monto
FROM cobranzasdetalles cd
	join estadoscuentas es on es.estadocuentaid = cd.estadocuentaid
	join conceptos co on co.conceptoid = es.conceptoid
	left join servicios s on s.servicioid = cd.servicioid
WHERE 
	 cd.cobranzaid = ?1;