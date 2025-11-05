package com.softwave.chamados.service;

import com.softwave.chamados.model.Chamado;
import com.softwave.chamados.model.StatusChamado;
import com.softwave.chamados.model.TipoChamado;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChamadoService {
    
    private Long proximoId = 1L;
    
    // Fila (Queue) FIFO para chamados comuns
    private final Queue<Chamado> filaChamadosComuns = new LinkedList<>();
    
    // Pilha (Stack) LIFO para chamados de emergência
    private final Stack<Chamado> pilhaChamadosEmergencia = new Stack<>();
    
    // Lista para histórico completo
    private final List<Chamado> historicoCompleto = new ArrayList<>();
    
    public Chamado criarChamado(String descricao, TipoChamado tipo) {
        Chamado chamado = new Chamado();
        chamado.setId(proximoId++);
        chamado.setDescricao(descricao);
        chamado.setTipo(tipo);
        chamado.setDataHoraCriacao(LocalDateTime.now());
        chamado.setStatus(StatusChamado.AGUARDANDO);
        
        // Adiciona no histórico
        historicoCompleto.add(chamado);
        
        // Adiciona na estrutura apropriada
        if (tipo == TipoChamado.COMUM) {
            filaChamadosComuns.offer(chamado);
        } else {
            pilhaChamadosEmergencia.push(chamado);
        }
        
        return chamado;
    }
    
    public List<Chamado> listarChamadosEmEspera() {
        List<Chamado> emEspera = new ArrayList<>();
        emEspera.addAll(filaChamadosComuns);
        emEspera.addAll(pilhaChamadosEmergencia);
        return emEspera;
    }
    
    public List<Chamado> listarFilaComuns() {
        return new ArrayList<>(filaChamadosComuns);
    }
    
    public List<Chamado> listarPilhaEmergencia() {
        return new ArrayList<>(pilhaChamadosEmergencia);
    }
    
    public Chamado atenderChamadoComum() {
        Chamado chamado = filaChamadosComuns.poll();
        if (chamado != null) {
            chamado.setStatus(StatusChamado.ATENDIDO);
            chamado.setDataHoraAtendimento(LocalDateTime.now());
        }
        return chamado;
    }
    
    public Chamado atenderChamadoEmergencia() {
        if (!pilhaChamadosEmergencia.isEmpty()) {
            Chamado chamado = pilhaChamadosEmergencia.pop();
            chamado.setStatus(StatusChamado.ATENDIDO);
            chamado.setDataHoraAtendimento(LocalDateTime.now());
            return chamado;
        }
        return null;
    }
    
    public List<Chamado> consultarHistorico() {
        return new ArrayList<>(historicoCompleto);
    }
}
