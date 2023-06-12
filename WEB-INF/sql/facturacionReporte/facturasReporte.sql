SELECT cobranzaid, TO_CHAR(c.fecha, 'DD-MM-YYYY HH24:MI:SS') as fecha,t.tipo,c.comprobantenum, totaldetalle, (p.nombre||' '||p.apellido)as alumno, anulado
	FROM public.cobranzas c
	left join personas p on p.personaid = c.alumnoid
	left join tipos t on t.tipoid = c.comprobantetipoid
	where c.fecha between '?1' and '?2'
	--1 and c.anulado = ?3
	--2 and c.comprobantetipoid = ?4
	order by fecha asc, cobranzaid asc;