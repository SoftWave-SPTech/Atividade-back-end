package com.softwave.chamados.dto;

import com.softwave.chamados.model.TipoChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoRequest {
    @NotBlank(message = "A descrição do chamado é obrigatória")
    private String descricao;
    
    @NotNull(message = "O tipo do chamado é obrigatório")
    private TipoChamado tipo;
}
