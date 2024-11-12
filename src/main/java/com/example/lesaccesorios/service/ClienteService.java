package com.example.lesaccesorios.service;

import com.example.lesaccesorios.model.Cliente;
import com.example.lesaccesorios.model.Usuario;
import com.example.lesaccesorios.repository.ClienteRepository;
import com.example.lesaccesorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    // Traer clientes del usuario
    public List<Cliente> getCliente(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Cliente> clientes   = usuario.getCliente();
        return clientes;
    }

    // Crear cliente
    public Cliente createCliente(Cliente cliente, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cliente createCliente = new Cliente();
        createCliente.setNombre(cliente.getNombre());
        createCliente.setApellido(cliente.getApellido());
        createCliente.setCliente_email(cliente.getCliente_email());
        createCliente.setCliente_direccion(cliente.getCliente_direccion());
        createCliente.setTelefono(cliente.getTelefono());

        createCliente.setUsuario(usuario);

        return clienteRepository.save(createCliente);
    }

    // Editar cliente
    public Cliente updateCliente(int id_cliente, Cliente cliente, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cliente updateCliente = clienteRepository.findClienteById(id_cliente);

        // Verifica si el producto tiene un usuario asociado y si coincide con el usuario autenticado
        if (updateCliente.getUsuario() == null || !updateCliente.getUsuario().equals(usuario)) {
            throw new RuntimeException("No tienes permisos para editar este producto");
        }

        updateCliente.setNombre(cliente.getNombre());
        updateCliente.setApellido(cliente.getApellido());
        updateCliente.setCliente_email(cliente.getCliente_email());
        updateCliente.setCliente_direccion(cliente.getCliente_direccion());
        updateCliente.setTelefono(cliente.getTelefono());

        return clienteRepository.save(updateCliente);
    }

    // Eliminar cliente
    public String deleteCliente(int id_cliente, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Cliente cliente = clienteRepository.findClienteById(id_cliente);

        if(!cliente.getUsuario().equals(usuario)) {
            throw new RuntimeException("Producto no encontrado");
        }

        clienteRepository.delete(cliente);
        return "Producto eliminado con éxito";
    }
}
