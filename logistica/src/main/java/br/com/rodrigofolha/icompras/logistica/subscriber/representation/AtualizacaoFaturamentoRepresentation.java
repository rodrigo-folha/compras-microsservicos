package br.com.rodrigofolha.icompras.logistica.subscriber.representation;

import br.com.rodrigofolha.icompras.logistica.model.StatusPedido;

public record AtualizacaoFaturamentoRepresentation(
        Long codigo,
        StatusPedido status,
        String urlNotaFiscal
) {
}
