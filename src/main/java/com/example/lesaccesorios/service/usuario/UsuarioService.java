package com.example.lesaccesorios.service.usuario;

import com.example.lesaccesorios.dto.usuario.UsuarioDTO;
import com.example.lesaccesorios.model.Usuario;
import com.example.lesaccesorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    // Traer los datos del usuario.
    public UsuarioDTO getUsuario(Authentication authentication) {
        // Obtenemos el usuario authenticado
        String email = authentication.getName();

        // Obtenemos al usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Convertirmos la información del usuario mediante nuestro DTO creado.
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId_usuario(usuario.getId_usuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());

        // Retornamos ahora el DTO y no el usuario como tal.
        return dto;
    }

    // Editar usuario.
    public UsuarioDTO updateUsuario(int id_usuario, Usuario updateUsuario, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el usuario autenticado es el mismo que se está intentando editar
        if (usuario.getId_usuario() != id_usuario) {
            throw new RuntimeException("No estás autorizado para editar esta información");
        }

        // Actualizar los campos del usuario autenticado con los datos de `updateUsuario`
        usuario.setNombre(updateUsuario.getNombre());
        usuario.setApellido(updateUsuario.getApellido());
        usuario.setEmail(updateUsuario.getEmail());

        // Guardar el usuario actualizado
        Usuario updatedUsuario = usuarioRepository.save(usuario);

        // Convertir el usuario guardado en un DTO antes de devolverlo
        UsuarioDTO dto = new UsuarioDTO();
        usuario.setId_usuario(updatedUsuario.getId_usuario());
        usuario.setNombre(updatedUsuario.getNombre());
        usuario.setApellido(updatedUsuario.getApellido());

        return dto;
    }

    // Eliminar usuario.
    public String deleteUsuario(int id_usuario, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getId_usuario() != id_usuario) {
            throw new RuntimeException("No tienes permisos para eliminar este usuario");
        }

        usuarioRepository.deleteById(id_usuario);
        return "Usuario eliminado con éxito";
    }

}
