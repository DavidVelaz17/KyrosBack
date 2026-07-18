-- 1 = ACTIVO, 2 = BAJA (ver enums.EstatusEstudiante). Los alumnos existentes quedan ACTIVO por defecto.
ALTER TABLE estudiante
    ADD COLUMN estatus SMALLINT NOT NULL DEFAULT 1;
