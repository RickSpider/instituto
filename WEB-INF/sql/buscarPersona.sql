select personaid as id, case when p.ruc is null or p.ruc like '' then  p.documentonum else p.ruc end as Ruc_CI, case when razonsocial is not null then p.razonsocial else (p.apellido||', '||p.nombre)end as RazonSocial 
from personas p
order by personaid asc;