package br.com.rodrigofolha.icompras.clientes.repository;

import br.com.rodrigofolha.icompras.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
