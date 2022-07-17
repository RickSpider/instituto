SELECT 
p.personaid, 
concat(p.apellido,', ',p.nombre) as fullname, 
p.razonsocial,
p.ruc
from personas p;