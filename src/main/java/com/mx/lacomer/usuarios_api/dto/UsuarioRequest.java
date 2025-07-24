package com.mx.lacomer.usuarios_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequest {

    @Schema(description = "Nombre del usuario", example = "Juan")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Schema(description = "Apellido paterno del usuario", example = "Pérez")
    @NotBlank(message = "El apellido paterno no puede estar vacío")
    @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres")
    private String apellidoPaterno;

    @Schema(description = "Apellido materno del usuario", example = "Gómez")
    @Size(max = 100, message = "El apellido materno no puede exceder los 100 caracteres")
    private String apellidoMaterno;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo es inválido")
    @Size(max = 255, message = "El correo no puede exceder los 255 caracteres")
    private String correo;

    @Schema(description = "Código postal de la dirección del usuario", example = "03100")
    @NotBlank(message = "El código postal no puede estar vacío")
    @Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe ser de 5 dígitos numéricos")
    private String codigoPostal;
}