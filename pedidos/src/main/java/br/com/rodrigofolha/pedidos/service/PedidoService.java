package br.com.rodrigofolha.pedidos.service;

import br.com.rodrigofolha.pedidos.model.Pedido;
import br.com.rodrigofolha.pedidos.repository.ItemPedidoRepository;
import br.com.rodrigofolha.pedidos.repository.PedidoRepository;
import br.com.rodrigofolha.pedidos.validator.PedidoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator validator;

    public Pedido criarPedido(Pedido pedido) {
        repository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
        return pedido;
    }

}
