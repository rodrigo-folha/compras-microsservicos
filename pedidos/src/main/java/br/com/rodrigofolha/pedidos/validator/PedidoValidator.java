package br.com.rodrigofolha.pedidos.validator;

import br.com.rodrigofolha.pedidos.client.ClientesClient;
import br.com.rodrigofolha.pedidos.client.ProdutosClient;
import br.com.rodrigofolha.pedidos.client.represantation.ClienteRepresentation;
import br.com.rodrigofolha.pedidos.client.represantation.ProdutoRepresentation;
import br.com.rodrigofolha.pedidos.model.ItemPedido;
import br.com.rodrigofolha.pedidos.model.Pedido;
import br.com.rodrigofolha.pedidos.model.exception.ValidationException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validar(Pedido pedido) {
        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);
    }

    private void validarCliente(Long codigoCliente) {
        try {
            ResponseEntity<ClienteRepresentation> response = clientesClient.obterDados(codigoCliente);
            ClienteRepresentation cliente = response.getBody();
            log.info("Cliente de código: {} encontrado: {}", cliente.codigo(), cliente.nome());

            if(!cliente.ativo()) {
                throw new ValidationException("codigoCliente", "Cliente inativo");
            }

        } catch (FeignException.NotFound e) {
            String message = String.format("Cliente de código %d não encontrado.", codigoCliente);
            throw new ValidationException("codigoCliente", message);
        }

    }

    private void validarItem(ItemPedido item) {
        try {
            ResponseEntity<ProdutoRepresentation> response = produtosClient.obterDados(item.getCodigoProduto());
            ProdutoRepresentation produto = response.getBody();
            log.info("Produto de codigo: {} encontrado: {}", produto.codigo(), produto.nome());

            if(!produto.ativo()) {
                throw new ValidationException("codigoProduto", "Produto inativo");
            }

        } catch (FeignException.NotFound e) {
            String message = String.format("Produto de código %d não encontrado.", item.getCodigoProduto());
            throw new ValidationException("codigoProduto", message);
        }
    }
}
