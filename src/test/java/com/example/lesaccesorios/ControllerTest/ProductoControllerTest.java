package com.example.lesaccesorios.ControllerTest;

import com.example.lesaccesorios.controller.ProductoController;
import com.example.lesaccesorios.dto.producto.ProductoDTO;
import com.example.lesaccesorios.model.Producto;
import com.example.lesaccesorios.service.ProductoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ProductoControllerTest {

    @Autowired
    private ProductoController productoController;
    @MockBean
    private ProductoService productoService;
    @MockBean
    private Authentication authentication;

    @Test
    public void testGetProductosUsuario() {
        // Configurar el usuario autenticado
        Mockito.when(authentication.getName()).thenReturn("prueba@gmail.com");

        // Crear datos de prueba
        ProductoDTO producto1 = new ProductoDTO(1, "Producto A", "Descripción A", new BigDecimal(10000), 20, "imagen1.png", 1, "Articulo A");
        ProductoDTO producto2 = new ProductoDTO(2, "Producto B", "Descripción B", new BigDecimal(20000), 15, "imagen2.png", 1, "Articulo B");
        List<ProductoDTO> productos = Arrays.asList(producto1, producto2);

        // Simular comportamiento del servicio
        Mockito.when(productoService.getProducto(authentication)).thenReturn(productos);

        // Llamar al método del controlador
        List<ProductoDTO> resultado = productoController.getProductoInfo(authentication);

        // Verificar resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals("Producto A", resultado.get(0).getNombre());

        // Imprimir resultado en la consola
        resultado.forEach(producto -> System.out.println(producto));
    }

    @Test
    public void testCreateProducto() {
        // Configurar el usuario autenticado
        Mockito.when(authentication.getName()).thenReturn("prueba@gmail.com");

        // Crear datos de prueba
        ProductoDTO productoDTO = new ProductoDTO(1, "Producto A", "Descripción A", new BigDecimal(10000), 20, "imagen1.png", 1, "Articulo A");
        Producto producto = new Producto();
        producto.setId_producto(1);
        producto.setNombre("Producto A");

        // Simular comportamiento del servicio
        Mockito.when(productoService.createProducto(productoDTO, authentication)).thenReturn(producto);

        // Llamar al método del controlador
        ResponseEntity<?> response = productoController.createProducto(productoDTO, authentication);

        // Verificar resultados
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody() instanceof Producto);
        Assertions.assertEquals("Producto A", ((Producto) response.getBody()).getNombre());
    }

    @Test
    public void testDeleteProducto() {
        // Configurar el usuario autenticado
        Mockito.when(authentication.getName()).thenReturn("prueba@gmail.com");

        // Simular comportamiento del servicio
        Mockito.when(productoService.deleteProducto(1, authentication)).thenReturn("Producto eliminado exitosamente");

        // Llamar al método del controlador
        String mensaje = String.valueOf(productoController.deleteProducto(1, authentication));

        // Verificar resultados
        Assertions.assertEquals("Producto eliminado exitosamente", mensaje);
    }
}
