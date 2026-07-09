package com.kyros.demokyros.entity;

import jakarta.persistence.Column;
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

@Entity
@Table(name = "secundaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Secundaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_secundaria", nullable = false, updatable = false)
    private Integer idSecundaria;

    @Column(name = "nombre_secundaria", nullable = false)
    private String nombreSecundaria;
}
