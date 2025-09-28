package br.com.rodrigofolha.pedidos.model.enums;

import lombok.Data;

@Data
public class DadosPagamento {
    private String dados;
    private TipoPagamento tipoPagamento;
}
