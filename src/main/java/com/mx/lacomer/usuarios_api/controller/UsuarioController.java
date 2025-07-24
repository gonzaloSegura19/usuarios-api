package com.mx.lacomer.usuarios_api.controller;

import com.mx.lacomer.usuarios_api.dto.UsuarioRequest;
import com.mx.lacomer.usuarios_api.dto.UsuarioResponse;
import com.mx.lacomer.usuarios_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Gestión de Usuarios", description = "Operaciones para la administración de usuarios en la API")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping
    @Operation(summary = "Crea un nuevo usuario",
            description = "Permite registrar un nuevo usuario en la base de datos, incluyendo la validación del código postal a través de Copomex.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes, correo mal formado)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Código postal no encontrado en Copomex",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UsuarioResponse> createUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse createdUsuario = usuarioService.createUsuario(usuarioRequest);
        return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(summary = "Obtiene todos los usuarios",
            description = "Devuelve una lista de todos los usuarios registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.getAllUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un usuario por su ID",
            description = "Devuelve los detalles de un usuario específico usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioResponse> getUsuarioById(
            @Parameter(description = "ID del usuario a buscar", example = "1") // Anotación para el parámetro
            @PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.getUsuarioById(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un usuario existente",
            description = "Permite actualizar los datos de un usuario por su ID. Si el código postal cambia, la dirección se actualizará vía Copomex.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o Código postal no encontrado en Copomex"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioResponse> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse updatedUsuario = usuarioService.updateUsuario(id, usuarioRequest);
        return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un usuario por su ID",
            description = "Remueve un usuario de la base de datos usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente (Sin Contenido)"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}