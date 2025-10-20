package br.com.rodrigofolha.pedidos.service;

import br.com.rodrigofolha.pedidos.client.ClientesClient;
import br.com.rodrigofolha.pedidos.client.ProdutosClient;
import br.com.rodrigofolha.pedidos.client.ServicoBancarioClient;
import br.com.rodrigofolha.pedidos.client.represantation.ClienteRepresentation;
import br.com.rodrigofolha.pedidos.client.represantation.ProdutoRepresentation;
import br.com.rodrigofolha.pedidos.model.ItemPedido;
import br.com.rodrigofolha.pedidos.model.Pedido;
import br.com.rodrigofolha.pedidos.model.enums.DadosPagamento;
import br.com.rodrigofolha.pedidos.model.enums.StatusPedido;
import br.com.rodrigofolha.pedidos.model.enums.TipoPagamento;
import br.com.rodrigofolha.pedidos.model.exception.ItemNaoEcontradoException;
import br.com.rodrigofolha.pedidos.publisher.PagamentoPublisher;
import br.com.rodrigofolha.pedidos.repository.ItemPedidoRepository;
import br.com.rodrigofolha.pedidos.repository.PedidoRepository;
import br.com.rodrigofolha.pedidos.validator.PedidoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {
    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator validator;
    private final ServicoBancarioClient servicoBancarioClient;
    private final ClientesClient apiClientes;
    private final ProdutosClient apiProdutos;
    private final PagamentoPublisher pagamentoPublisher;

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
            prepararEPublicarPedidoPago(pedido);
        } else {
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }

        repository.save(pedido);
    }

    private void prepararEPublicarPedidoPago(Pedido pedido) {
        pedido.setStatus(StatusPedido.PAGO);
        carregarDadosCliente(pedido);
        carregarItensPedido(pedido);
        pagamentoPublisher.publicar(pedido);
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

    public Optional<Pedido> carregarDadosCompletosPedido(Long codigo) {
        Optional<Pedido> pedido = repository.findById(codigo);
        pedido.ifPresent(this::carregarDadosCliente);
        pedido.ifPresent(this::carregarItensPedido);
        return pedido;
    }

    private void carregarDadosCliente(Pedido pedido) {
        Long codigoCliente = pedido.getCodigoCliente();
        ResponseEntity<ClienteRepresentation> response = apiClientes.obterDados(codigoCliente);
        pedido.setDadosCliente(response.getBody());

    }

    private void carregarItensPedido(Pedido pedido) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
        pedido.setItens(itens);
        pedido.getItens().forEach(this::carregarDadosProduto);
    }

    private void carregarDadosProduto(ItemPedido item) {
        Long codigoProduto = item.getCodigoProduto();
        ResponseEntity<ProdutoRepresentation> response = apiProdutos.obterDados(codigoProduto);
        item.setNome(response.getBody().nome());
    }
}
