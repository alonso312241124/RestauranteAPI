package com.example.restauranteapi.controller;

import com.example.restauranteapi.DTO.ReservasConClientesDTO;
import com.example.restauranteapi.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservasConClientesController {
    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/reservas/{fecha}")
    public ResponseEntity<List<ReservasConClientesDTO>> getReservasConClientesPorFecha(@PathVariable LocalDate fecha) {

        List<ReservasConClientesDTO> reservasDTO = new ArrayList<>();

        reservaRepository.findByFecha(fecha).forEach(reserva -> {
            reservasDTO.add(
                    ReservasConClientesDTO.builder()
                            // Cliente
                            .nombre(reserva.getCliente().getNombre())
                            .email(reserva.getCliente().getEmail())
                            .telefono(reserva.getCliente().getTelefono())
                            // Mesa
                            .numeroMesa(reserva.getMesa().getNMesa())
                            .descripcionMesa(reserva.getMesa().getDescripcion())
                            // Reserva
                            .fechaReserva(reserva.getFecha())
                            .hora(reserva.getHora())
                            .nPersonas(reserva.getNPersonas())
                            .build()
            );
        });
        return ResponseEntity.ok(reservasDTO);
    }
}
