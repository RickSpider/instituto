Select p.personaid as id, concat(p.apellido,', ',p.nombre) as NombreCompleto, p.documentonum as documento
from personas p
left join proveedores d on d.personaid = p.personaid and d.sedeid = ##SEDEID##
where d.proveedorid is null
order by p.personaid asc;