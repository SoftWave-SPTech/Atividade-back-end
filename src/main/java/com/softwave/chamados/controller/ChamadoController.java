package com.softwave.chamados.controller;

import com.softwave.chamados.dto.ChamadoRequest;
import com.softwave.chamados.model.Chamado;
import com.softwave.chamados.service.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {
    
    @Autowired
    private ChamadoService chamadoService;
    
    @PostMapping
    public ResponseEntity<Chamado> criarChamado(@RequestBody ChamadoRequest request) {
        Chamado chamado = chamadoService.criarChamado(request.getDescricao(), request.getTipo());
        return ResponseEntity.status(HttpStatus.CREATED).body(chamado);
    }
    
    @GetMapping("/fila")
    public ResponseEntity<List<Chamado>> listarFilaComuns() {
        return ResponseEntity.ok(chamadoService.listarFilaComuns());
    }
    
    @GetMapping("/pilha")
    public ResponseEntity<List<Chamado>> listarPilhaEmergencia() {
        return ResponseEntity.ok(chamadoService.listarPilhaEmergencia());
    }
    
    @GetMapping("/em-espera")
    public ResponseEntity<List<Chamado>> listarChamadosEmEspera() {
        return ResponseEntity.ok(chamadoService.listarChamadosEmEspera());
    }
    
    @DeleteMapping("/atender/comum")
    public ResponseEntity<Chamado> atenderChamadoComum() {
        Chamado chamado = chamadoService.atenderChamadoComum();
        if (chamado == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(chamado);
    }
    
    @DeleteMapping("/atender/emergencia")
    public ResponseEntity<Chamado> atenderChamadoEmergencia() {
        Chamado chamado = chamadoService.atenderChamadoEmergencia();
        if (chamado == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(chamado);
    }
    
    @GetMapping("/historico")
    public ResponseEntity<List<Chamado>> consultarHistorico() {
        return ResponseEntity.ok(chamadoService.consultarHistorico());
    }
}
