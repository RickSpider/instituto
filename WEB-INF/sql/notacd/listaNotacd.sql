select 
n.notacdid,
n.fecha ,
n.timbrado, 
n.comprobantenum, 
n.ruc, 
n.razonsocial, 
case when n.anulado = true then 'SI' else 'NO' end as anulado, 
to_char(n.fechaanulacion, 'DD/MM/YYYY HH24:MI:SS') as fechaanulacion
from notacds n
order by n.notacdid desc;