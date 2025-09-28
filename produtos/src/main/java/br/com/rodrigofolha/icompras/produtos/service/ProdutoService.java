package br.com.rodrigofolha.icompras.produtos.service;

import br.com.rodrigofolha.icompras.produtos.model.Produto;
import br.com.rodrigofolha.icompras.produtos.repository.ProdutoRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRespository respository;

    public Produto salvar(Produto produto) {
        return respository.save(produto);
    }

    public Optional<Produto> obterPorCodigo(Long codigo) {
        return respository.findById(codigo);
    }

}
