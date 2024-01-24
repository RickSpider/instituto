select ec.vencimiento, c.concepto, ec.periodo, sum(ec.monto) as monto, sum(ec.pago) as pago, sum(ec.monto -(ec.montodescuento+ec.pago)) as saldo, c.conceptoid from estadoscuentas ec
left join conceptos c on c.conceptoid = ec.conceptoid
where cursovigenteid = ?1
group by ec.vencimiento, c.conceptoid, ec.periodo
order by c.conceptoid, ec.periodo;
