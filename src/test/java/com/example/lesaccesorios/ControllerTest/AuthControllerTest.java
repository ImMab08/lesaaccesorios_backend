package com.example.lesaccesorios.ControllerTest;

import com.example.lesaccesorios.controller.usuario.AuthController;
import com.example.lesaccesorios.dto.usuario.AuthResponse;
import com.example.lesaccesorios.dto.usuario.LoginInfoUsuario;
import com.example.lesaccesorios.dto.usuario.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationResult;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Test
    public void testlogin_true() {

        LoginRequest informacionLogueo = new LoginRequest();
        informacionLogueo.setEmail("prueba@gmail.com");
        informacionLogueo.setPassword("123");

        ResponseEntity<?> response = authController.login(informacionLogueo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Body is null");
//      assertTrue(response.getBody() instanceof LoginRequest, "Body request is null");

        AuthResponse infoautenticidad = (AuthResponse) response.getBody();

        assertNotNull(infoautenticidad.getToken(), "El token no puede ser nulo");
        assertTrue(infoautenticidad.getToken() instanceof String, "El token debe de ser de tipo String");
        assertFalse(infoautenticidad.getToken().isEmpty(), "El token no deberia de estar vacio");
    }

}
