package com.example.lesaccesorios.controller.usuario;

import com.example.lesaccesorios.dto.usuario.AuthResponse;
import com.example.lesaccesorios.dto.usuario.LoginRequest;
import com.example.lesaccesorios.dto.usuario.RegisterRequest;
import com.example.lesaccesorios.service.usuario.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/v01/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(value = "register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

}