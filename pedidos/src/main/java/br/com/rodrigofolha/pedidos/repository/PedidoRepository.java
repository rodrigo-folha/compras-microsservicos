package br.com.rodrigofolha.pedidos.repository;

import br.com.rodrigofolha.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
