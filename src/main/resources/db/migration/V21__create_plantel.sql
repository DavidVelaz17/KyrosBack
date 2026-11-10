-- Planteles como catálogo (se administran desde Catálogos en el front) en vez de un texto libre en
-- cada grupo: renombrar un plantel se refleja en todos sus grupos.
CREATE TABLE plantel (
    id_plantel      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_plantel  VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Los planteles que el front tenía fijos, más cualquier otro nombre que ya use algún grupo.
INSERT INTO plantel (nombre_plantel) VALUES
    ('Avenida 5'),
    ('Calle 7'),
    ('Calle 13'),
    ('Orizaba');

INSERT INTO plantel (nombre_plantel)
SELECT DISTINCT g.nombre_plantel
FROM grupo g
WHERE g.nombre_plantel NOT IN (SELECT p.nombre_plantel FROM plantel p);

ALTER TABLE grupo ADD COLUMN id_plantel INT NULL;

UPDATE grupo g
JOIN plantel p ON p.nombre_plantel = g.nombre_plantel
SET g.id_plantel = p.id_plantel;

-- RESTRICT: un plantel con grupos no se puede borrar (PlantelService lo valida antes con un
-- mensaje claro).
ALTER TABLE grupo
    MODIFY COLUMN id_plantel INT NOT NULL,
    ADD CONSTRAINT fk_grupo_plantel FOREIGN KEY (id_plantel) REFERENCES plantel (id_plantel) ON DELETE RESTRICT,
    DROP COLUMN nombre_plantel;
