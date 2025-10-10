package br.com.rodrigofolha.pedidos.controller.dto;

import br.com.rodrigofolha.pedidos.model.enums.TipoPagamento;

public record AdicaoNovoPagamentoDTO(
        Long codigoPedido,
        String dados,
        TipoPagamento tipoPagamento
) {
}
