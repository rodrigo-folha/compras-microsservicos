package br.com.rodrigofolha.pedidos.publisher.representation;

import br.com.rodrigofolha.pedidos.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.util.List;

public record DetalhePedidoRepresentation(
        Long codigo,
        Long codigoCliente,
        String nome,
        String cpf,
        String logradouro,
        String numero,
        String bairro,
        String email,
        String telefone,
        String dataPedido,
        BigDecimal total,
        StatusPedido status,
        List<DetalheItemPedidoRepresentation> itens
) {
}
