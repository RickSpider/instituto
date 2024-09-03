SELECT cobranzaid, TO_CHAR(c.fecha, 'DD-MM-YYYY HH24:MI:SS') as fecha,
t.tipo||case when tc.tipo is not null then (' '||tc.tipo)else ('') end
,c.comprobantenum, totaldetalle, 
case 
when c.personaid is not null then (p.nombre||' '||p.apellido) 
when c.alumnoid is not null then (p2.nombre||' '||p2.apellido)
end, 
anulado
FROM public.cobranzas c
		left join personas p on p.personaid = c.personaid
		left join alumnos a on a.alumnoid = c.alumnoid
		left join personas p2 on p2.personaid = a.personaid
        	left join tipos t on t.tipoid = c.comprobantetipoid
		left join tipos tc on tc.tipoid = c.condicionventatipoid
        where c.fecha between '?1' and '?2'
        --1 and c.anulado = ?3
        --2 and c.comprobantetipoid = ?4
        order by c.fecha asc, c.cobranzaid asc;
