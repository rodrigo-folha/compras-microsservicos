package br.com.rodrigofolha.pedidos.controller.dto;

import java.math.BigDecimal;

public record ItemPedidoDTO(
        Long codigoProduto,
        Integer quantidade,
        BigDecimal valorUnitario
) {
}
