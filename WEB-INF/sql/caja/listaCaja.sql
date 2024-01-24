SELECT 
c.cajaid,
u1.account as usuariocaja,
c.apertura,
c.montoApertura,
u2.account as usuariocierre,
c.cierre,
c.montoCierre
FROM cajas c
left join usuarios u1 on u1.usuarioid = c.usuariocajaid
left join usuarios u2 on u2.usuarioid = c.usuariocierreid
where sedeid = ?1
order by cajaid desc;