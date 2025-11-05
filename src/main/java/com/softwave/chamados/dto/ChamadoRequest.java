package com.softwave.chamados.dto;

import com.softwave.chamados.model.TipoChamado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoRequest {
    private String descricao;
    private TipoChamado tipo;
}
