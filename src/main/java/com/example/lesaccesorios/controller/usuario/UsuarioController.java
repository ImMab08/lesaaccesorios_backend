package com.example.lesaccesorios.controller.usuario;

import com.example.lesaccesorios.dto.usuario.UsuarioDTO;
import com.example.lesaccesorios.model.Usuario;
import com.example.lesaccesorios.service.usuario.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/v01/usuario/")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    // Obtener información del usuario.
    @GetMapping("info")
    public ResponseEntity<?> getUsuarioInfo(Authentication authentication) {
        try {
            UsuarioDTO getUsuarioInfo = usuarioService.getUsuario(authentication);
            return ResponseEntity.ok(getUsuarioInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la informaicón del usaurio");
        }
    }

    // Actualizar usuarip
    @PutMapping("update/{id_usuario}")
    public ResponseEntity<?> updateUsuarioInfo(@PathVariable("id_usuario") int id_usuario, @RequestBody Usuario updateUsario, Authentication authentication) {
        try {
            UsuarioDTO usuario = usuarioService.updateUsuario(id_usuario, updateUsario, authentication);
            return ResponseEntity.ok("Usuario actualizado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la información del usuario.");
        }
    }

    // Eliminar usuario
    @DeleteMapping("delete/{id_usuario}")
    public ResponseEntity<?> deleteUsuarioInfo(@PathVariable("id_usuario") int id_usuario, Authentication authentication) {
        try {
            String usuario = usuarioService.deleteUsuario(id_usuario, authentication);
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar al usuario.");
        }
    }

}
