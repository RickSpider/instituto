Select 
io.inscriptoonlineid,
io.cursovigenteid|| ' - ' || c.curso as cursoVigente,

concat_ws(', ',
    CASE WHEN (string_to_array(cv.dias, ';'))[1] = 'true' THEN 'Domingo' END,
    CASE WHEN (string_to_array(cv.dias, ';'))[2] = 'true' THEN 'Lunes' END,
    CASE WHEN (string_to_array(cv.dias, ';'))[3] = 'true' THEN 'Martes' END,
    CASE WHEN (string_to_array(cv.dias, ';'))[4] = 'true' THEN 'Miercoles' END,
    CASE WHEN (string_to_array(cv.dias, ';'))[5] = 'true' THEN 'Jueves' END,
    CASE WHEN (string_to_array(cv.dias, ';'))[6] = 'true' THEN 'Viernes' END,
    CASE WHEN (string_to_array(cv.dias, ';'))[7] = 'true' THEN 'Sabado' END
) 
as cursoDia,
TO_CHAR(io.fechaInscripcion, 'DD/MM/YYYY HH24:MI:SS') as fecha,
io.nombre||' '|| io.apellido as nombreCompleto,
io.ci,
io.ruc,
io.razonsocial,
case when (io.facturaTercero) then 'SI' else 'NO' end as facturaTercero,
io.telefono,
io.email,
io.tituloObtenido,

case when (io.emailverificado = true) then 'SI' else 'Sin Verificacion' end as verificado,
case when ec.pago > 0 then 'SI' else 'NO' end as matriculaPagada,
io.encuesta

from inscriptosonline io
left join cursosvigentes cv on cv.cursovigenteid = io.cursovigenteid
left join cursos c on c.cursoid = cv.cursoid
left join (
    select 
        cursovigenteid,
        alumnoid,
        sum(monto) as monto,
        sum(montodescuento) as montodescuento,
        sum(pago) as pago
    from estadoscuentas
    where conceptoid = 1 and periodo = 1
    group by cursovigenteid, alumnoid
) ec 
on ec.cursovigenteid = io.cursovigenteid 
and ec.alumnoid = io.alumnoid
where io.fechaInscripcion between '?1' and '?2'
order by io.inscriptoonlineid desc;