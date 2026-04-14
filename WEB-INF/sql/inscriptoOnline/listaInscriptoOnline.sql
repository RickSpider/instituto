Select 
io.inscriptoonlineid,
io.cursovigenteid|| ' - ' || c.curso as cursovigente,
io.fechaInscripcion,
io.nombre||' '|| io.apellido,
io.ci,
io.telefono,
io.email,
case when (io.emailverificado = true) then 'SI' else 'Sin Verificacion' end as verificado,
case when ec.pago > 0 then 'SI' else 'NO' end as matriculaPagada

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
order by io.inscriptoonlineid desc;