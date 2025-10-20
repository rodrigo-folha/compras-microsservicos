package br.com.rodrigofolha.pedidos.repository;

import br.com.rodrigofolha.pedidos.model.ItemPedido;
import br.com.rodrigofolha.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedido(Pedido pedido);
}
