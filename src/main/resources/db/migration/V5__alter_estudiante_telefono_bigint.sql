-- numero_telefonico era INT (máx. 2,147,483,647), insuficiente para un teléfono
-- mexicano de 10 dígitos (ej. 5512345678), que ya excede ese límite.
ALTER TABLE estudiante MODIFY COLUMN numero_telefonico BIGINT NOT NULL;
