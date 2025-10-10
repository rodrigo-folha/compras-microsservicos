package br.com.rodrigofolha.pedidos.model;

public record ErroResposta(
        String mensagem,
        String campo,
        String erro
) {
}
