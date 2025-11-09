package br.com.rodrigofolha.icompras.logistica.model;

public record AtualizacaoEnvioPedido(
        Long codigo,
        StatusPedido status,
        String codigoRastreio
) {
}
