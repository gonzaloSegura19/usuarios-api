package com.mx.lacomer.usuarios_api.service;

import com.mx.lacomer.usuarios_api.dto.UsuarioRequest;
import com.mx.lacomer.usuarios_api.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse createUsuario(UsuarioRequest usuarioRequest);
    List<UsuarioResponse> getAllUsuarios();
    UsuarioResponse getUsuarioById(Long id);
    UsuarioResponse updateUsuario(Long id, UsuarioRequest usuarioRequest);
    void deleteUsuario(Long id);
}