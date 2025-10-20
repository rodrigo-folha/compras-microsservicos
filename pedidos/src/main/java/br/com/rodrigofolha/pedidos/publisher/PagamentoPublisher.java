package br.com.rodrigofolha.pedidos.publisher;

import br.com.rodrigofolha.pedidos.model.Pedido;
import br.com.rodrigofolha.pedidos.publisher.representation.DetalhePedidoRepresentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoPublisher {

    private final DetalhePedidoMapper mapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${icompras.config.kafka.topics.pedidos-pagos}")
    private String topico;

    public void publicar(Pedido pedido) {
        log.info("Publicando pedido pago {}", pedido.getCodigo());

        try {
            DetalhePedidoRepresentation representation = mapper.map(pedido);
            String json = objectMapper.writeValueAsString(representation);
            kafkaTemplate.send(topico, "dados", json);
        } catch (JsonProcessingException e) {
            log.error("Erro ao processar o json", e);
        } catch (RuntimeException e) {
            log.error("Erro técnico ao publicar no tópico de pedidos", e);
        }
    }
}
