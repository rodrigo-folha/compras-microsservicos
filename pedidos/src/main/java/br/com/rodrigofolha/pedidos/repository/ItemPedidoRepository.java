package br.com.rodrigofolha.pedidos.repository;

import br.com.rodrigofolha.pedidos.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
