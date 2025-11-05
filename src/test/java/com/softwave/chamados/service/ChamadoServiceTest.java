package com.softwave.chamados.service;

import com.softwave.chamados.model.Chamado;
import com.softwave.chamados.model.StatusChamado;
import com.softwave.chamados.model.TipoChamado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChamadoServiceTest {
    
    private ChamadoService chamadoService;
    
    @BeforeEach
    void setUp() {
        chamadoService = new ChamadoService();
    }
    
    @Test
    void deveCriarChamadoComum() {
        Chamado chamado = chamadoService.criarChamado("Problema no sistema", TipoChamado.COMUM);
        
        assertNotNull(chamado);
        assertNotNull(chamado.getId());
        assertEquals("Problema no sistema", chamado.getDescricao());
        assertEquals(TipoChamado.COMUM, chamado.getTipo());
        assertEquals(StatusChamado.AGUARDANDO, chamado.getStatus());
        assertNotNull(chamado.getDataHoraCriacao());
    }
    
    @Test
    void deveCriarChamadoEmergencia() {
        Chamado chamado = chamadoService.criarChamado("Sistema fora do ar", TipoChamado.EMERGENCIA);
        
        assertNotNull(chamado);
        assertEquals(TipoChamado.EMERGENCIA, chamado.getTipo());
        assertEquals(StatusChamado.AGUARDANDO, chamado.getStatus());
    }
    
    @Test
    void deveAdicionarChamadoComumNaFila() {
        chamadoService.criarChamado("Chamado 1", TipoChamado.COMUM);
        chamadoService.criarChamado("Chamado 2", TipoChamado.COMUM);
        
        List<Chamado> fila = chamadoService.listarFilaComuns();
        assertEquals(2, fila.size());
    }
    
    @Test
    void deveAdicionarChamadoEmergenciaNaPilha() {
        chamadoService.criarChamado("Emergência 1", TipoChamado.EMERGENCIA);
        chamadoService.criarChamado("Emergência 2", TipoChamado.EMERGENCIA);
        
        List<Chamado> pilha = chamadoService.listarPilhaEmergencia();
        assertEquals(2, pilha.size());
    }
    
    @Test
    void deveAtenderChamadoComumNaOrdemFIFO() {
        Chamado primeiro = chamadoService.criarChamado("Primeiro", TipoChamado.COMUM);
        Chamado segundo = chamadoService.criarChamado("Segundo", TipoChamado.COMUM);
        
        Chamado atendido = chamadoService.atenderChamadoComum();
        
        assertEquals(primeiro.getId(), atendido.getId());
        assertEquals(StatusChamado.ATENDIDO, atendido.getStatus());
        assertNotNull(atendido.getDataHoraAtendimento());
    }
    
    @Test
    void deveAtenderChamadoEmergenciaNaOrdemLIFO() {
        Chamado primeiro = chamadoService.criarChamado("Emergência 1", TipoChamado.EMERGENCIA);
        Chamado segundo = chamadoService.criarChamado("Emergência 2", TipoChamado.EMERGENCIA);
        
        Chamado atendido = chamadoService.atenderChamadoEmergencia();
        
        // O último a entrar deve ser o primeiro a sair (LIFO)
        assertEquals(segundo.getId(), atendido.getId());
        assertEquals(StatusChamado.ATENDIDO, atendido.getStatus());
        assertNotNull(atendido.getDataHoraAtendimento());
    }
    
    @Test
    void deveRetornarNullAoAtenderFilaVazia() {
        Chamado atendido = chamadoService.atenderChamadoComum();
        assertNull(atendido);
    }
    
    @Test
    void deveRetornarNullAoAtenderPilhaVazia() {
        Chamado atendido = chamadoService.atenderChamadoEmergencia();
        assertNull(atendido);
    }
    
    @Test
    void deveManterHistoricoCompleto() {
        chamadoService.criarChamado("Chamado Comum 1", TipoChamado.COMUM);
        chamadoService.criarChamado("Emergência 1", TipoChamado.EMERGENCIA);
        chamadoService.criarChamado("Chamado Comum 2", TipoChamado.COMUM);
        
        List<Chamado> historico = chamadoService.consultarHistorico();
        
        assertEquals(3, historico.size());
    }
    
    @Test
    void deveManterHistoricoAposAtendimento() {
        chamadoService.criarChamado("Chamado 1", TipoChamado.COMUM);
        chamadoService.criarChamado("Chamado 2", TipoChamado.COMUM);
        
        chamadoService.atenderChamadoComum();
        
        List<Chamado> historico = chamadoService.consultarHistorico();
        assertEquals(2, historico.size());
        
        // Verifica que um dos chamados está atendido
        long atendidos = historico.stream()
                .filter(c -> c.getStatus() == StatusChamado.ATENDIDO)
                .count();
        assertEquals(1, atendidos);
    }
    
    @Test
    void deveListarTodosChamadosEmEspera() {
        chamadoService.criarChamado("Comum 1", TipoChamado.COMUM);
        chamadoService.criarChamado("Emergência 1", TipoChamado.EMERGENCIA);
        chamadoService.criarChamado("Comum 2", TipoChamado.COMUM);
        
        List<Chamado> emEspera = chamadoService.listarChamadosEmEspera();
        
        assertEquals(3, emEspera.size());
    }
}
