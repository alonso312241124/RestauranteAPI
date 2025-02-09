package com.example.restauranteapi.controller;

import com.example.restauranteapi.entity.Reserva;
import com.example.restauranteapi.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class ReservaAPIController {
    @Autowired
    private ReservaRepository reservaRepository;

    public int convertirHorasStringAMinutos(String hora){ //XX:YY
        String[] partesHora = hora.split(":");
        int horas = Integer.parseInt(partesHora[0]); //XX
        int minutos = Integer.parseInt(partesHora[1]); //YY
        return horas * 60 + minutos;
    }

    // Verifica si existen reservas en un rango de +-1 hora (en minutos)
    // para una fecha especificada de una mesa
    public boolean sePuedeReservar(Date fecha, String hora, Long idMesa) {
        List<Reserva> reservas = reservaRepository.findByFechaAndMesa_Id(fecha, idMesa);

        int minutosReserva = convertirHorasStringAMinutos(hora);

        for (Reserva reserva : reservas) {
            int minutosExistenteTotal = convertirHorasStringAMinutos(reserva.getHora());

            // Compara 59 minutos en vez de 60 para que las reservas estén en horas con números redondos
            // 14:00, en lugar de reservar a las 14:01
            if (minutosReserva >= (minutosExistenteTotal - 59) && minutosReserva <= (minutosExistenteTotal + 59)) {
                return false;
            }
        }
        return true;
    }


    /*
    Ejemplo de datos enviados por POST, las fechas deben ser futuras
    {
      "fecha": "2025-03-18",
      "hora": "18:00",
      "npersonas": 52,
      "cliente": {
        "id": 1
      },
      "mesa": {
        "id": 4
      }
    }
    */

    @PostMapping("/reservas")
    public ResponseEntity<Reserva> insertReserva(@RequestBody Reserva reserva){
        if(sePuedeReservar(reserva.getFecha(), reserva.getHora(), reserva.getMesa().getId())){
            var reservaGuardada = reservaRepository.save(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaGuardada);
        }else{
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id){
        return reservaRepository.findById(id)
                .map(reserva ->{
                    reservaRepository.delete(reserva);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
