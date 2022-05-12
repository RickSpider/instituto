SELECT 
i.institucionid, 
i.institucion, 
d.departamento, 
c.ciudad
FROM instituciones i
left join ciudades c on c.ciudadid = i.ciudadid
left join departamentos d on d.departamentoid = c.departamentoid
order by institucionid asc;