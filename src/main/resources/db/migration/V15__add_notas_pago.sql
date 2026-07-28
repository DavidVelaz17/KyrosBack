-- Observaciones que el usuario captura al registrar un pago (se muestran en el recibo).
ALTER TABLE pago ADD COLUMN notas_pago VARCHAR(500) NULL;
