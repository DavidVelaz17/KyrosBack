-- Grado escolar, dirección, fecha de inscripción, horario e ingresoA dejan de ser obligatorios
-- en el alta de alumno (nombre, apellido paterno y apellido materno se quedan como obligatorios).
ALTER TABLE estudiante MODIFY COLUMN grado_escolar VARCHAR(50) NULL;
ALTER TABLE estudiante MODIFY COLUMN direccion VARCHAR(255) NULL;
ALTER TABLE estudiante MODIFY COLUMN fecha_inscripcion DATE NULL;
ALTER TABLE estudiante MODIFY COLUMN horario SMALLINT NULL;
ALTER TABLE estudiante MODIFY COLUMN ingreso_a SMALLINT NULL;
