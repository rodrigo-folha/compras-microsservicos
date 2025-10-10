package br.com.rodrigofolha.pedidos.controller;

import br.com.rodrigofolha.pedidos.controller.dto.AdicaoNovoPagamentoDTO;
import br.com.rodrigofolha.pedidos.controller.dto.NovoPedidoDTO;
import br.com.rodrigofolha.pedidos.controller.mappers.PedidoMapper;
import br.com.rodrigofolha.pedidos.model.ErroResposta;
import br.com.rodrigofolha.pedidos.model.Pedido;
import br.com.rodrigofolha.pedidos.model.exception.ItemNaoEcontradoException;
import br.com.rodrigofolha.pedidos.model.exception.ValidationException;
import br.com.rodrigofolha.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService service;
    private final PedidoMapper mapper;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody NovoPedidoDTO dto) {
        try {
            Pedido pedido = mapper.map(dto);
            Pedido novoPedido = service.criarPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido.getCodigo());
        } catch (ValidationException e) {
            ErroResposta erro = new ErroResposta("Erro de Validação", e.getField(), e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @PostMapping("/pagamentos")
    public ResponseEntity<Object> adicionarNovoPagamento(@RequestBody AdicaoNovoPagamentoDTO dto) {
        try {
            service.adicionarNovoPagamento(dto.codigoPedido(), dto.dados(), dto.tipoPagamento());
            return ResponseEntity.noContent().build();
        } catch (ItemNaoEcontradoException e) {
            ErroResposta erro = new ErroResposta("Item não encontrado", "codigoPedido", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}
