package com.mx.lacomer.usuarios_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioResponse {
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido paterno del usuario", example = "Pérez")
    private String apellidoPaterno;

    @Schema(description = "Apellido materno del usuario", example = "Gómez")
    private String apellidoMaterno;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String correo;

    @Schema(description = "Código postal de la dirección", example = "03100")
    private String codigoPostal;

    @Schema(description = "Nombre del asentamiento (colonia, barrio, etc.)", example = "Del Valle Centro")
    private String asentamiento;

    @Schema(description = "Tipo de asentamiento", example = "Colonia")
    private String tipoAsentamiento;

    @Schema(description = "Municipio al que pertenece el asentamiento", example = "Benito Juárez")
    private String municipio;

    @Schema(description = "Estado al que pertenece el asentamiento", example = "Ciudad de México")
    private String estado;

    @Schema(description = "Ciudad al que pertenece el asentamiento", example = "Ciudad de México")
    private String ciudad;

    @Schema(description = "Zona geográfica del asentamiento (ej. Urbana, Rural)", example = "Urbana")
    private String zona;

}