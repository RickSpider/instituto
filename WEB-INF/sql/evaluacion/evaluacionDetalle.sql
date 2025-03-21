select 
ROW_NUMBER() OVER (ORDER BY p.apellido) AS linea, 
p.apellido, 
p.nombre, 
p.documentonum,
ev.proceso1, 
ev.proceso2, 
ev.proceso3, 
ev.proceso4, 
ev.proceso5, 
ev.pfinal, 
ev.calificacion,
 CASE 
        WHEN ev.calificacion = 0 THEN 'CERO'
	WHEN ev.calificacion = 1 THEN 'UNO'
        WHEN ev.calificacion = 2 THEN 'DOS'
        WHEN ev.calificacion = 3 THEN 'TRES'
        WHEN ev.calificacion = 4 THEN 'CUATRO'
	WHEN ev.calificacion = 5 THEN 'CINCO'
       
    	END AS calificacion_letras

from evaluacionesdetalles ev
join alumnos a on a.alumnoid = ev.alumnoid
join personas p on p.personaid = a.personaid
where ev.evaluacionid = ?1
order by p.apellido;