Select p.pagoid, p.fecha, pers.razonsocial, comprobantenum from pagos p
join proveedores prov on prov.proveedorid = p.proveedorid
join personas pers on pers.personaid = prov. proveedorid
join tipos cond on cond.tipoid = p.condicionVentaTipoid
where cond.sigla = 'CONDICION_VENTA_CREDITO'
order by p.pagoid desc; 
