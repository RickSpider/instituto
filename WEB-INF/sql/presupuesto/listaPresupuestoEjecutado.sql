SELECT 
    COALESCE(pagos.rubroid, presupuestos.rubroid) AS rubroid,
	COALESCE(pagos.rubro, presupuestos.rubro) AS rubro,
    COALESCE(presupuestos.total_presupuesto, 0) AS total_presupuesto,
    COALESCE(pagos.total_pago, 0) AS total_pagos,
    COALESCE(presupuestos.total_presupuesto, 0) - COALESCE(pagos.total_pago, 0) AS diferencia
FROM 
    -- Subconsulta para obtener la suma de los montos de pagosdetalles
    (SELECT cr.rubroid, r.rubro ,SUM(pd.monto) AS total_pago
     FROM pagosdetalles pd
     JOIN pagos p ON p.pagoid = pd.pagoid
     JOIN cuentasrubros cr ON cr.cuentarubroid = pd.cuentarubroid
	JOIN rubros r on r.rubroid = cr.rubroid
     WHERE (p.condicionventatipoid = 28 OR p.condicionventatipoid IS NULL) and EXTRACT(YEAR FROM p.fecha) = ?1 
     GROUP BY cr.rubroid, r.rubro) pagos

FULL OUTER JOIN 
    -- Subconsulta para obtener los montos de presupuestosdetalles
    (SELECT pd.rubroid, r.rubro ,SUM(pd.monto) AS total_presupuesto
     FROM presupuestosdetalles pd
     JOIN presupuestos p ON p.presupuestoid = pd.presupuestoid
	 JOIN rubros r on r.rubroid = pd.rubroid
     WHERE p.anho = ?1
     GROUP BY pd.rubroid, r.rubro) presupuestos

ON pagos.rubroid = presupuestos.rubroid;