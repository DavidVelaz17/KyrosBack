package com.kyros.demokyros.entity;

import com.kyros.demokyros.convertors.MetodoPagoConverter;
import com.kyros.demokyros.enums.MetodoPago;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago", nullable = false, updatable = false)
    private Integer idPago;

    @Column(name = "monto_pagado_pago", nullable = false)
    private Double montoPagadoPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Convert(converter = MetodoPagoConverter.class)
    @Column(name = "metodo_pago", nullable = false, columnDefinition = "smallint")
    private MetodoPago metodoPago;

    @Column(name = "id_cargo", nullable = false)
    private Integer idCargo;
}
