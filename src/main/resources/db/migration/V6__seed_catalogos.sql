-- Datos de ejemplo (máx. 5 registros por tabla) para poder probar los catálogos
-- de "Ingresa a" sin tener que darlos de alta manualmente uno por uno.

INSERT INTO area (nombre_area) VALUES
    ('Ingeniería'),
    ('Ciencias Sociales'),
    ('Ciencias de la Salud'),
    ('Artes y Humanidades'),
    ('Ciencias Económico-Administrativas');

INSERT INTO universidad (nombre_universidad) VALUES
    ('UNAM'),
    ('IPN'),
    ('Universidad Autónoma Metropolitana'),
    ('Tecnológico de Monterrey'),
    ('Universidad Iberoamericana');

INSERT INTO carrera (nombre_carrera, id_area) VALUES
    ('Ingeniería en Sistemas Computacionales', (SELECT id_area FROM area WHERE nombre_area = 'Ingeniería')),
    ('Psicología', (SELECT id_area FROM area WHERE nombre_area = 'Ciencias Sociales')),
    ('Medicina', (SELECT id_area FROM area WHERE nombre_area = 'Ciencias de la Salud')),
    ('Diseño Gráfico', (SELECT id_area FROM area WHERE nombre_area = 'Artes y Humanidades')),
    ('Administración de Empresas', (SELECT id_area FROM area WHERE nombre_area = 'Ciencias Económico-Administrativas'));

INSERT INTO carrera_universidad (id_carrera, id_universidad) VALUES
    ((SELECT id_carrera FROM carrera WHERE nombre_carrera = 'Ingeniería en Sistemas Computacionales'), (SELECT id_universidad FROM universidad WHERE nombre_universidad = 'IPN')),
    ((SELECT id_carrera FROM carrera WHERE nombre_carrera = 'Psicología'), (SELECT id_universidad FROM universidad WHERE nombre_universidad = 'UNAM')),
    ((SELECT id_carrera FROM carrera WHERE nombre_carrera = 'Medicina'), (SELECT id_universidad FROM universidad WHERE nombre_universidad = 'UNAM')),
    ((SELECT id_carrera FROM carrera WHERE nombre_carrera = 'Diseño Gráfico'), (SELECT id_universidad FROM universidad WHERE nombre_universidad = 'Universidad Iberoamericana')),
    ((SELECT id_carrera FROM carrera WHERE nombre_carrera = 'Administración de Empresas'), (SELECT id_universidad FROM universidad WHERE nombre_universidad = 'Tecnológico de Monterrey'));

INSERT INTO curso_verano (nombre_curso_verano) VALUES
    ('Verano de Inglés Básico'),
    ('Verano de Matemáticas'),
    ('Verano de Robótica'),
    ('Verano de Arte y Creatividad'),
    ('Verano de Ciencias Naturales');

INSERT INTO materia (nombre_materia) VALUES
    ('Matemáticas'),
    ('Física'),
    ('Química'),
    ('Inglés'),
    ('Historia');

INSERT INTO secundaria (nombre_secundaria) VALUES
    ('Secundaria Técnica No. 5'),
    ('Secundaria General No. 12'),
    ('Secundaria Diurna No. 8'),
    ('Escuela Secundaria Justo Sierra'),
    ('Secundaria Federal No. 3');

INSERT INTO bachillerato (nombre_bachillerato) VALUES
    ('CCH Naucalpan'),
    ('Preparatoria No. 9'),
    ('CETIS No. 15'),
    ('Bachillerato Tecnológico Industrial'),
    ('Colegio de Bachilleres Plantel 4');
