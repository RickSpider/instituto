select 
'2' as codreg, 
'11' as codtipidprov, 
SUBSTRING(pers.ruc, 1, 
    CASE 
        WHEN POSITION('-' IN pers.ruc) > 0 
        THEN POSITION('-' IN pers.ruc) - 1 
        ELSE LENGTH(pers.ruc)
    END
) AS ruc,
pers.razonsocial, 
'109' as codcomp, 
TO_CHAR(p.fecha, 'DD/MM/YYYY') as fecha, 
p.timbrado, 
p.comprobantenum, 
p.total10, 
p.total5,
p.totalexento, 
(p.total10+ p.total5+ p.totalexento) as total,
case when cond.sigla ='CONDICION_VENTA_CONTADO' then 1 else 2 end as cond,
case when moneda.sigla ='MONEDA_GUARANI' then 'N' else 'S' end as monedaex,
case when p.imputaiva then 'S' else 'N' end as impiva,
case when p.imputaire then 'S' else 'N' end as impire,
case when p.imputairprsp then 'S' else 'N' end as impirprsp,
case when p.noimputa then 'S' else 'N' end as nomimp,
'' as numcompasoc,
'' as timnumcompasoc


from pagos p
join proveedores prov on prov.proveedorid = p.proveedorid
join personas pers on pers.personaid = prov.personaid
join tipos cond on cond.tipoid = p.condicionventatipoid
join tipos moneda on moneda.tipoid = p.monedatipoid
join tipos comp on comp.tipoid = p.comprobantetipoid
where comp.sigla = 'COMPROBANTE_FACTURA' and p.comprobanteElectronico = false
AND TO_CHAR(p.fecha, 'YYYY-MM') = '?1';