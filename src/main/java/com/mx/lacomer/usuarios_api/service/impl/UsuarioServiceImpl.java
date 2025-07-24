package com.mx.lacomer.usuarios_api.service.impl;

import com.mx.lacomer.usuarios_api.dto.UsuarioRequest;
import com.mx.lacomer.usuarios_api.dto.UsuarioResponse;
import com.mx.lacomer.usuarios_api.model.Direccion;
import com.mx.lacomer.usuarios_api.model.Usuario;
import com.mx.lacomer.usuarios_api.repository.UsuarioRepository;
import com.mx.lacomer.usuarios_api.service.UsuarioService;
import com.mx.lacomer.usuarios_api.service.SepomexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SepomexService sepomexService; // Inyecta el nuevo SepomexService

    @Override
    public UsuarioResponse createUsuario(UsuarioRequest usuarioRequest) {

        Direccion direccion = null;
        try {
            direccion = getDireccionFromCopomex(usuarioRequest.getCodigoPostal());
        } catch (IOException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error de comunicación con la API de Copomex: " + e.getMessage(), e);
        }



        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioRequest.getNombre());
        usuario.setApellidoPaterno(usuarioRequest.getApellidoPaterno());
        usuario.setApellidoMaterno(usuarioRequest.getApellidoMaterno());
        usuario.setCorreo(usuarioRequest.getCorreo());
        usuario.setDireccion(direccion); // Asignar la dirección obtenida


        Usuario savedUsuario = usuarioRepository.save(usuario);


        return mapToUsuarioResponse(savedUsuario);
    }

    @Override
    public List<UsuarioResponse> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::mapToUsuarioResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponse getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
        return mapToUsuarioResponse(usuario);
    }

    @Override
    public UsuarioResponse updateUsuario(Long id, UsuarioRequest usuarioRequest) {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));


        existingUsuario.setNombre(usuarioRequest.getNombre());
        existingUsuario.setApellidoPaterno(usuarioRequest.getApellidoPaterno());
        existingUsuario.setApellidoMaterno(usuarioRequest.getApellidoMaterno());
        existingUsuario.setCorreo(usuarioRequest.getCorreo());


        if (existingUsuario.getDireccion() == null || !existingUsuario.getDireccion().getCodigoPostal().equals(usuarioRequest.getCodigoPostal())) {
            Direccion nuevaDireccion = null;
            try {
                nuevaDireccion = getDireccionFromCopomex(usuarioRequest.getCodigoPostal());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error de comunicación con la API de Copomex al actualizar: " + e.getMessage(), e);
            }


            if (existingUsuario.getDireccion() != null) {
                nuevaDireccion.setId(existingUsuario.getDireccion().getId()); // Mantener el ID para que JPA actualice
            }
            existingUsuario.setDireccion(nuevaDireccion);
        }

        Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
        return mapToUsuarioResponse(updatedUsuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }



    private Direccion getDireccionFromCopomex(String codigoPostal) throws IOException {

        List<Map<String, Object>> responseList = sepomexService.getAddressInfo(codigoPostal);


        Map<String, Object> data = responseList.get(0);

        Direccion direccion = new Direccion();
        direccion.setCodigoPostal(codigoPostal); // El CP lo pasamos nosotros
        direccion.setAsentamiento((String) data.get("asentamiento"));
        direccion.setTipoAsentamiento((String) data.get("tipo_asentamiento"));
        direccion.setMunicipio((String) data.get("municipio"));
        direccion.setEstado((String) data.get("estado"));
        direccion.setCiudad((String) data.get("ciudad"));
        direccion.setZona((String) data.get("zona"));
        return direccion;
    }

    private UsuarioResponse mapToUsuarioResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setApellidoPaterno(usuario.getApellidoPaterno());
        response.setApellidoMaterno(usuario.getApellidoMaterno());
        response.setCorreo(usuario.getCorreo());

        if (usuario.getDireccion() != null) {
            response.setCodigoPostal(usuario.getDireccion().getCodigoPostal());
            response.setAsentamiento(usuario.getDireccion().getAsentamiento());
            response.setTipoAsentamiento(usuario.getDireccion().getTipoAsentamiento());
            response.setMunicipio(usuario.getDireccion().getMunicipio());
            response.setEstado(usuario.getDireccion().getEstado());
            response.setCiudad(usuario.getDireccion().getCiudad());
            response.setZona(usuario.getDireccion().getZona());
        }
        return response;
    }
}