SELECT 
    t.tipo,
    COUNT(CASE WHEN documentoestadoid = 51 THEN documentoalumnoid END) AS activo,
	COUNT(CASE WHEN documentoestadoid = 53 THEN documentoalumnoid END) AS cursoNoFinalizado,
	COUNT(CASE WHEN documentoestadoid = 54 THEN documentoalumnoid END) AS notitulohablilitante,
	COUNT(CASE WHEN documentoestadoid = 55 THEN documentoalumnoid END) AS Procesomec,
	COUNT(CASE WHEN documentoestadoid = 52 THEN documentoalumnoid END) AS entregado
FROM cvadocumentos doc
join tipos t on t.tipoid = doc.documentoalumnoid 
where cursovigenteid = ?1
GROUP BY documentoalumnoid, t.tipo
ORDER BY documentoalumnoid;
