-- La edad, el número telefónico del alumno y la escuela de procedencia dejan de ser obligatorios en el alta de alumno.
ALTER TABLE estudiante MODIFY COLUMN edad SMALLINT NULL;
ALTER TABLE estudiante MODIFY COLUMN numero_telefonico BIGINT NULL;
ALTER TABLE estudiante MODIFY COLUMN escuela_procedencia VARCHAR(150) NULL;
