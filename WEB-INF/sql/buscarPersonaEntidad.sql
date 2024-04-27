select pe.entidadid as id, e.entidad as entidad, pe.cuenta as cuenta
from personaentidad pe
left join entidades e on e.entidadid = pe.entidadid
where personaid = ?1;