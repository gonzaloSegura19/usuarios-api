package com.mx.lacomer.usuarios_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "direcciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_postal", nullable = false)
    private String codigoPostal;

    @Column(name = "asentamiento", nullable = false)
    private String asentamiento;

    @Column(name = "tipo_asentamiento", nullable = false)
    private String tipoAsentamiento;

    @Column(name = "municipio", nullable = false)
    private String municipio;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "zona")
    private String zona;
}