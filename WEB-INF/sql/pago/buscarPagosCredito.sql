Select p.pagoid as id, p.fecha as fecha, pers.razonsocial as razonsocial, comprobantenum as NRO from pagos p
join proveedores prov on prov.proveedorid = p.proveedorid
join personas pers on pers.personaid = prov.personaid
join tipos cond on cond.tipoid = p.condicionVentaTipoid
where cond.sigla = 'CONDICION_VENTA_CREDITO'
order by p.pagoid desc; 
