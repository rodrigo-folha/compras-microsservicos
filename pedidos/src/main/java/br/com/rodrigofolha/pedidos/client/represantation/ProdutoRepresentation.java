package br.com.rodrigofolha.pedidos.client.represantation;

import java.math.BigDecimal;

public record ProdutoRepresentation(
        Long codigo,
        String nome,
        BigDecimal valorUnitario,
        boolean ativo
) {
}
