package br.com.rodrigofolha.pedidos.controller;

import br.com.rodrigofolha.pedidos.controller.dto.NovoPedidoDTO;
import br.com.rodrigofolha.pedidos.controller.mappers.PedidoMapper;
import br.com.rodrigofolha.pedidos.model.Pedido;
import br.com.rodrigofolha.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
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
        Pedido pedido = mapper.map(dto);
        Pedido novoPedido = service.criarPedido(pedido);
        return ResponseEntity.ok(novoPedido.getCodigo());
    }
}
