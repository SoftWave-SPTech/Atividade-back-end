package com.softwave.chamados.service;

import com.softwave.chamados.model.Chamado;
import com.softwave.chamados.model.StatusChamado;
import com.softwave.chamados.model.TipoChamado;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ChamadoService {
    
    private final AtomicLong proximoId = new AtomicLong(1);
    
    // Fila (Queue) FIFO para chamados comuns - thread-safe
    private final Queue<Chamado> filaChamadosComuns = new ConcurrentLinkedQueue<>();
    
    // Pilha (Stack) LIFO para chamados de emergência - synchronized
    private final Stack<Chamado> pilhaChamadosEmergencia = new Stack<>();
    
    // Lista para histórico completo - synchronized
    private final List<Chamado> historicoCompleto = Collections.synchronizedList(new ArrayList<>());
    
    public Chamado criarChamado(String descricao, TipoChamado tipo) {
        Chamado chamado = new Chamado();
        chamado.setId(proximoId.getAndIncrement());
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
            synchronized (pilhaChamadosEmergencia) {
                pilhaChamadosEmergencia.push(chamado);
            }
        }
        
        return chamado;
    }
    
    public List<Chamado> listarChamadosEmEspera() {
        List<Chamado> emEspera = new ArrayList<>();
        emEspera.addAll(filaChamadosComuns);
        synchronized (pilhaChamadosEmergencia) {
            emEspera.addAll(pilhaChamadosEmergencia);
        }
        return emEspera;
    }
    
    public List<Chamado> listarFilaComuns() {
        return new ArrayList<>(filaChamadosComuns);
    }
    
    public List<Chamado> listarPilhaEmergencia() {
        synchronized (pilhaChamadosEmergencia) {
            return new ArrayList<>(pilhaChamadosEmergencia);
        }
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
        synchronized (pilhaChamadosEmergencia) {
            if (!pilhaChamadosEmergencia.isEmpty()) {
                Chamado chamado = pilhaChamadosEmergencia.pop();
                chamado.setStatus(StatusChamado.ATENDIDO);
                chamado.setDataHoraAtendimento(LocalDateTime.now());
                return chamado;
            }
        }
        return null;
    }
    
    public List<Chamado> consultarHistorico() {
        synchronized (historicoCompleto) {
            return new ArrayList<>(historicoCompleto);
        }
    }
}
