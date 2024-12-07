package com.example.lesaccesorios.ControllerTest;

import com.example.lesaccesorios.controller.usuario.UsuarioController;
import com.example.lesaccesorios.dto.usuario.UsuarioDTO;
import com.example.lesaccesorios.service.usuario.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioControllerTest {

    @Autowired
    private UsuarioController usuarioController;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private Authentication authentication;

    @Test
    public void testGetSettingsInfoUsuarioDto() {
        // Simulación del correo electrónico en la autenticación
        Mockito.when(authentication.getName()).thenReturn("prueba@gmail.com");

        // Crear un usuario simulado para la prueba
        UsuarioDTO usuarioSimulado = new UsuarioDTO();
        usuarioSimulado.setId_usuario(1);
        usuarioSimulado.setNombre("prueba");
        usuarioSimulado.setApellido("pruebita");
        usuarioSimulado.setEmail("prueba@gmail.com");

        // Simular la respuesta del servicio
        Mockito.when(usuarioService.getUsuario(authentication)).thenReturn(usuarioSimulado);

        // Llamar al controlador
        UsuarioDTO resultado = usuarioController.getUsuario(authentication);

        // Verificar resultados
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals("prueba", resultado.getNombre(), "El nombre no coincide");
        assertEquals("pruebita", resultado.getApellido(), "El apellido no coincide");
        assertEquals("prueba@gmail.com", resultado.getEmail(), "El correo electrónico no coincide");
    }
}

