-- Nullable porque los pagos ya existentes no tienen forma de saber quién los registró;
-- todo pago nuevo (vía PagoForm) sí lo exige.
ALTER TABLE pago
    ADD COLUMN id_usuario INT NULL,
    ADD CONSTRAINT fk_pago_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT;
