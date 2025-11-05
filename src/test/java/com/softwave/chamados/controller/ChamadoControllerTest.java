package com.softwave.chamados.controller;

import com.softwave.chamados.model.TipoChamado;
import com.softwave.chamados.service.ChamadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChamadoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ChamadoService chamadoService;
    
    @Test
    void deveCriarChamadoComum() throws Exception {
        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descricao\":\"Teste\",\"tipo\":\"COMUM\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.descricao").value("Teste"))
                .andExpect(jsonPath("$.tipo").value("COMUM"))
                .andExpect(jsonPath("$.status").value("AGUARDANDO"));
    }
    
    @Test
    void deveCriarChamadoEmergencia() throws Exception {
        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descricao\":\"Emergência\",\"tipo\":\"EMERGENCIA\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("EMERGENCIA"));
    }
    
    @Test
    void deveListarFilaComuns() throws Exception {
        mockMvc.perform(get("/chamados/fila"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void deveListarPilhaEmergencia() throws Exception {
        mockMvc.perform(get("/chamados/pilha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void deveListarChamadosEmEspera() throws Exception {
        mockMvc.perform(get("/chamados/em-espera"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void deveAtenderChamadoComum() throws Exception {
        // Primeiro cria um chamado
        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descricao\":\"Atender comum\",\"tipo\":\"COMUM\"}"))
                .andExpect(status().isCreated());
        
        // Depois atende
        mockMvc.perform(delete("/chamados/atender/comum"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ATENDIDO"));
    }
    
    @Test
    void deveAtenderChamadoEmergencia() throws Exception {
        // Primeiro cria um chamado
        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descricao\":\"Atender emergência\",\"tipo\":\"EMERGENCIA\"}"))
                .andExpect(status().isCreated());
        
        // Depois atende
        mockMvc.perform(delete("/chamados/atender/emergencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ATENDIDO"));
    }
    
    @Test
    void deveRetornarNoContentQuandoNaoHaChamadoComumParaAtender() throws Exception {
        mockMvc.perform(delete("/chamados/atender/comum"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deveRetornarNoContentQuandoNaoHaChamadoEmergenciaParaAtender() throws Exception {
        mockMvc.perform(delete("/chamados/atender/emergencia"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deveConsultarHistorico() throws Exception {
        mockMvc.perform(get("/chamados/historico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void deveRetornarBadRequestQuandoDescricaoVazia() throws Exception {
        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descricao\":\"\",\"tipo\":\"COMUM\"}"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveRetornarBadRequestQuandoTipoNulo() throws Exception {
        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descricao\":\"Teste\"}"))
                .andExpect(status().isBadRequest());
    }
}
