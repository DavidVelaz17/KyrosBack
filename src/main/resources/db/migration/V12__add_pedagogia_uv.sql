INSERT INTO carrera_universidad (id_carrera, id_universidad)
SELECT c.id_carrera, u.id_universidad
FROM carrera c, universidad u
WHERE c.nombre_carrera = 'Pedagogía'
  AND u.nombre_universidad = 'UV'
  AND NOT EXISTS (
      SELECT 1 FROM carrera_universidad cu
      WHERE cu.id_carrera = c.id_carrera AND cu.id_universidad = u.id_universidad
  );
