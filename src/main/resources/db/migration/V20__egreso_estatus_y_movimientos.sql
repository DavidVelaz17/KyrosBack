-- Un egreso ya no se borra físicamente: se marca como ELIMINADO (y se puede restaurar), y las
-- correcciones sobrescriben sus valores. El estatus refleja el último movimiento:
-- 1 = SIN_CAMBIOS, 2 = CORREGIDO, 3 = ELIMINADO, 4 = RESTAURADO.
ALTER TABLE egreso
    ADD COLUMN estatus_egreso        SMALLINT  NOT NULL DEFAULT 1,
    -- Cuándo se capturó en el sistema (fecha_hora_egreso es cuándo fue el gasto). NULL en los
    -- egresos registrados antes de esta migración.
    ADD COLUMN fecha_registro_egreso DATETIME  NULL;

-- Historial de cada egreso: quién lo corrigió/eliminó/restauró, cuándo y por qué.
-- tipo_movimiento: 1 = CORRECCION, 2 = ELIMINACION, 3 = RESTAURACION.
-- En una CORRECCION se guardan los cuatro valores antes y después (aunque no todos cambien), para
-- poder mostrar el detalle exacto; en ELIMINACION/RESTAURACION van en NULL.
CREATE TABLE egreso_movimiento (
    id_egreso_movimiento      INT AUTO_INCREMENT PRIMARY KEY,
    id_egreso                 INT           NOT NULL,
    tipo_movimiento           SMALLINT      NOT NULL,
    fecha_hora_movimiento     DATETIME      NOT NULL,
    id_usuario                INT           NOT NULL,
    justificacion_movimiento  VARCHAR(500)  NOT NULL,
    id_tipo_egreso_anterior   INT           NULL,
    id_tipo_egreso_nuevo      INT           NULL,
    importe_anterior          DOUBLE        NULL,
    importe_nuevo             DOUBLE        NULL,
    fecha_hora_anterior       DATETIME      NULL,
    fecha_hora_nueva          DATETIME      NULL,
    observaciones_anteriores  VARCHAR(500)  NULL,
    observaciones_nuevas      VARCHAR(500)  NULL,
    CONSTRAINT fk_egreso_movimiento_egreso FOREIGN KEY (id_egreso) REFERENCES egreso (id_egreso) ON DELETE RESTRICT,
    CONSTRAINT fk_egreso_movimiento_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT,
    CONSTRAINT fk_egreso_movimiento_tipo_anterior FOREIGN KEY (id_tipo_egreso_anterior) REFERENCES tipo_egreso (id_tipo_egreso) ON DELETE RESTRICT,
    CONSTRAINT fk_egreso_movimiento_tipo_nuevo FOREIGN KEY (id_tipo_egreso_nuevo) REFERENCES tipo_egreso (id_tipo_egreso) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_egreso_movimiento_egreso ON egreso_movimiento (id_egreso, fecha_hora_movimiento);
