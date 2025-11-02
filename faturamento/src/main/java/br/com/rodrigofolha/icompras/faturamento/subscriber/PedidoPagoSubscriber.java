package br.com.rodrigofolha.icompras.faturamento.subscriber;

import br.com.rodrigofolha.icompras.faturamento.GeradorNotaFiscalService;
import br.com.rodrigofolha.icompras.faturamento.mapper.PedidoMapper;
import br.com.rodrigofolha.icompras.faturamento.model.Pedido;
import br.com.rodrigofolha.icompras.faturamento.subscriber.representation.DetalhePedidoRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoPagoSubscriber {

    private final ObjectMapper mapper;
    private final GeradorNotaFiscalService service;
    private final PedidoMapper pedidoMapper;

    @KafkaListener(groupId = "icompras-faturamento", topics = "${icompras.config.kafka.topics.pedidos-pagos}")
    public void listener(String json) {
        try {
            log.info("Recebendo pedido para faturamento: {}", json);
            DetalhePedidoRepresentation representation = mapper.readValue(json, DetalhePedidoRepresentation.class);
            Pedido pedido = pedidoMapper.map(representation);
            service.gerar(pedido);
        } catch (Exception e) {
            log.error("Erro na consumação do tópico de pedidos pagos: {}", e.getMessage());
        }
    }
}
