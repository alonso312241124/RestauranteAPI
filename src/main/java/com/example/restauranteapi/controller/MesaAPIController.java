package com.example.restauranteapi.controller;

import com.example.restauranteapi.entity.Mesa;
import com.example.restauranteapi.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MesaAPIController {
    @Autowired
    private MesaRepository mesaRepository;

    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> getListMesas(){
        var mesas = mesaRepository.findAll();
        return ResponseEntity.ok(mesas);
    }

    @PostMapping("/mesas")
    public ResponseEntity<Mesa> insertMesa(@RequestBody Mesa mesa){
        var mesaGuardada =  mesaRepository.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaGuardada);
    }

    @PutMapping("/mesas/{id}")
    public ResponseEntity<Mesa> updateMesa(@PathVariable Long id, @RequestBody Mesa mesaEditada){
        return mesaRepository.findById(id)
                .map(mesa -> {
                    if(mesaEditada.getDescripcion() != null)
                        mesa.setDescripcion(mesaEditada.getDescripcion());
                    if(mesaEditada.getNMesa() != null)
                        mesa.setNMesa(mesaEditada.getNMesa());
                    return ResponseEntity.ok(mesaRepository.save(mesa));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/mesas/{id}")
    public ResponseEntity<?> deleteMesa(@PathVariable Long id){
        return mesaRepository.findById(id)
                .map(mesa ->{
                    mesaRepository.delete(mesa);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
