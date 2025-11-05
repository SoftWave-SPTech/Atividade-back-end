package com.softwave.chamados.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chamado {
    private Long id;
    private String descricao;
    private TipoChamado tipo;
    private LocalDateTime dataHoraCriacao;
    private LocalDateTime dataHoraAtendimento;
    private StatusChamado status;
}
