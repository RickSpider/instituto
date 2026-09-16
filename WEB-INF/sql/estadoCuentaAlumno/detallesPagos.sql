select * from (
select 
c.cobranzaid as id,
cd.estadocuentaid as estadocuentaid,
to_char(c.fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha, 
t.tipo,  
c.comprobantenum, 
case when c.anulado = true then 'SI' else 'NO' end as anulado 
from cobranzasdetalles cd
join cobranzas c on c.cobranzaid = cd.cobranzaid 
join tipos t on t.tipoid = c.comprobantetipoid
where cd.estadocuentaid = ?1

union

select 
n.notacdid as id,
nd.estadocuentaid as estadocuentaid,
to_char(n.fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha, 
t.tipo, 
n.comprobantenum, 
case when n.anulado = true then 'SI' else 'NO' end as anulado 
from notacddetalles nd
join notacds n on n.notacdid = nd.notacdid
join tipos t on t.tipoid = n.comprobantetipoid
where nd.estadocuentaid = ?1 

union

select 
tr.transladoid as id,
td.estadocuentaid as estadocuentaid,
to_char(tr.fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha, 
t.tipo, 
tr.transladoNro,
'NO' as anulado
from transladosdetalles td
join translados tr on tr.transladoid = td.transladoid
join tipos t on t.tipoid = tr.comprobantetipoid
where td.estadocuentaid = ?1 )
order by fecha asc
;