package br.com.rodrigofolha.pedidos.subscriber.representation;

import br.com.rodrigofolha.pedidos.model.enums.StatusPedido;

public record AtualizacaoStatusPedidoRepresentation(
        Long codigo,
        StatusPedido status,
        String urlNotaFiscal,
        String codigoRastreio
) {
}
