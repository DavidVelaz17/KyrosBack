-- V21 sembró los planteles que el front tenía fijos (Avenida 5, Calle 7, Calle 13, Orizaba), pero
-- los planteles reales son Calle 7, Avenida 5 y Avenida 3. V21 ya está aplicada en algunas bases,
-- así que el ajuste va aquí y no editando V21 (Flyway rechaza una migración aplicada que cambió).

INSERT INTO plantel (nombre_plantel)
SELECT 'Avenida 3' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM plantel WHERE nombre_plantel = 'Avenida 3');

-- Solo si ningún grupo los usa: si un grupo ya tenía "Calle 13" u "Orizaba", se conservan.
DELETE FROM plantel
WHERE nombre_plantel IN ('Calle 13', 'Orizaba')
  AND id_plantel NOT IN (SELECT id_plantel FROM grupo);
