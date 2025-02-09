package com.example.restauranteapi.controller;

import com.example.restauranteapi.entity.Cliente;
import com.example.restauranteapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClienteAPIController {
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getListClientes(){
        var clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/clientes")
    public ResponseEntity<Cliente> saveCliente(@RequestBody Cliente cliente){
        var clienteGuardado = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteEditado){
        return clienteRepository.findById(id)
                .map(cliente -> {
                    if(clienteEditado.getNombre() != null)
                        cliente.setNombre(clienteEditado.getNombre());
                    if(clienteEditado.getEmail() != null)
                        cliente.setEmail(clienteEditado.getEmail());
                    if(clienteEditado.getTelefono() != null)
                        cliente.setTelefono(clienteEditado.getTelefono());
                    return ResponseEntity.ok(clienteRepository.save(cliente));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id){
        return clienteRepository.findById(id)
                .map(cliente ->{
                    clienteRepository.delete(cliente);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
