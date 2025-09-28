package br.com.rodrigofolha.icompras.produtos.repository;

import br.com.rodrigofolha.icompras.produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRespository extends JpaRepository<Produto, Long> {

}
