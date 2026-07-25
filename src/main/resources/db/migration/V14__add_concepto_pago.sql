-- Permite que un pago (abono) tenga su propio concepto, distinto del concepto del cargo al que pertenece.
ALTER TABLE pago ADD COLUMN concepto_pago VARCHAR(255) NULL;
