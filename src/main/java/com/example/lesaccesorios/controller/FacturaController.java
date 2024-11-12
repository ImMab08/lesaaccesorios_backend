package com.example.lesaccesorios.controller;

import com.example.lesaccesorios.dto.factura.FacturaDTO;
import com.example.lesaccesorios.model.Factura;
import com.example.lesaccesorios.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/v01/factura/")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    // Obtener una factura por su ID
    @GetMapping("{id}")
    public ResponseEntity<?> getFacturaById(@PathVariable Integer id, Authentication authentication) {
        try {
            FacturaDTO facturaDTO = facturaService.getFacturaById(id, authentication);
            return new ResponseEntity<>(facturaDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Crear una nueva factura
    @PostMapping("create")
    public ResponseEntity<?> createFactura(@RequestBody FacturaDTO facturaDTO, Authentication authentication) {
        try {
            Factura nuevaFactura = facturaService.createFactura(facturaDTO, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}