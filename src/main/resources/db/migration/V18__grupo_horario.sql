-- Horario del grupo (mismos códigos que estudiante.horario: 1 Escolarizado, 2 Sabatino, 3 Virtual).
-- Default 1 (Escolarizado) solo para no romper los grupos ya existentes; los nuevos lo capturan
-- explícitamente (ver GrupoForm, @NotNull).
ALTER TABLE grupo ADD COLUMN horario SMALLINT NOT NULL DEFAULT 1;
