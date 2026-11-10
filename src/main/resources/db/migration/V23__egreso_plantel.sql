-- Cada egreso pertenece a un plantel, para que el corte de caja por plantel cuadre. Es NULL en los
-- egresos registrados antes de esta migración (no hay de dónde deducirlo); al crear o corregir un
-- egreso el plantel es obligatorio (ver EgresoForm / EgresoCorreccionRequest).
ALTER TABLE egreso
    ADD COLUMN id_plantel INT NULL,
    ADD CONSTRAINT fk_egreso_plantel FOREIGN KEY (id_plantel) REFERENCES plantel (id_plantel) ON DELETE RESTRICT;

-- Las correcciones también registran el cambio de plantel.
ALTER TABLE egreso_movimiento
    ADD COLUMN id_plantel_anterior INT NULL,
    ADD COLUMN id_plantel_nuevo    INT NULL,
    ADD CONSTRAINT fk_egreso_movimiento_plantel_anterior FOREIGN KEY (id_plantel_anterior) REFERENCES plantel (id_plantel) ON DELETE RESTRICT,
    ADD CONSTRAINT fk_egreso_movimiento_plantel_nuevo FOREIGN KEY (id_plantel_nuevo) REFERENCES plantel (id_plantel) ON DELETE RESTRICT;
