package br.com.rodrigofolha.pedidos.service;

import br.com.rodrigofolha.pedidos.client.ServicoBancarioClient;
import br.com.rodrigofolha.pedidos.model.Pedido;
import br.com.rodrigofolha.pedidos.model.enums.DadosPagamento;
import br.com.rodrigofolha.pedidos.model.enums.StatusPedido;
import br.com.rodrigofolha.pedidos.model.enums.TipoPagamento;
import br.com.rodrigofolha.pedidos.model.exception.ItemNaoEcontradoException;
import br.com.rodrigofolha.pedidos.repository.ItemPedidoRepository;
import br.com.rodrigofolha.pedidos.repository.PedidoRepository;
import br.com.rodrigofolha.pedidos.validator.PedidoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {
    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator validator;
    private final ServicoBancarioClient servicoBancarioClient;

    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        validator.validar(pedido);
        realizarPersistencia(pedido);
        enviarSolicitacaoPagamento(pedido);
        return pedido;
    }

    private void enviarSolicitacaoPagamento(Pedido pedido) {
        String chavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(chavePagamento);
    }

    private void realizarPersistencia(Pedido pedido) {
        repository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
    }

    public void atualizarStatusPagamento(Long codigoPedido, String chavePagamento,
                                         boolean sucesso, String observacoes) {
        Optional<Pedido> pedidoEncontrado =
                repository.findByCodigoAndChavePagamento(codigoPedido, chavePagamento);

        if (pedidoEncontrado.isEmpty()) {
            String msg = String.format("Pedido não encontrado para o código %d e chave pagamento %s",
                    codigoPedido, chavePagamento);
            log.error(msg);
            return;
        }

        Pedido pedido = pedidoEncontrado.get();

        if (sucesso) {
            pedido.setStatus(StatusPedido.PAGO);
        } else {
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }

        repository.save(pedido);
    }

    @Transactional
    public void adicionarNovoPagamento(Long codigoPedido, String dadosCartao, TipoPagamento tipoPagamento) {
        Optional<Pedido> pedidoEncontrado = repository.findById(codigoPedido);
        if (pedidoEncontrado.isEmpty()) {
            throw new ItemNaoEcontradoException("Pedido não encontrado para o código informado.");
        }

        Pedido pedido = pedidoEncontrado.get();

        DadosPagamento dadosPagamento = new DadosPagamento();
        dadosPagamento.setTipoPagamento(tipoPagamento);
        dadosPagamento.setDados(dadosCartao);

        pedido.setDadosPagamento(dadosPagamento);
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setObservacoes("Novo pagamento realizado, aguardando o processamento.");

        String novaChavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(novaChavePagamento);

        repository.save(pedido);
    }
}
