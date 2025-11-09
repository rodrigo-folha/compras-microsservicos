package br.com.rodrigofolha.icompras.logistica.service;

import br.com.rodrigofolha.icompras.logistica.model.AtualizacaoEnvioPedido;
import br.com.rodrigofolha.icompras.logistica.model.StatusPedido;
import br.com.rodrigofolha.icompras.logistica.publisher.EnvioPedidoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EnvioPedidoService {

    private final EnvioPedidoPublisher publisher;

    public void enviar(Long codigoPedido, String urlNotaFiscal) {

        String codigoRastreio = gerarCodigoRastreio();

        AtualizacaoEnvioPedido atualizacaoRepresentation =
                new AtualizacaoEnvioPedido(codigoPedido, StatusPedido.ENVIADO, codigoRastreio);

        publisher.enviar(atualizacaoRepresentation);
    }

    private String gerarCodigoRastreio() {
        Random random = new Random();
        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));

        int numeros = 100000000 + random.nextInt(900000000);

        return "" + letra1 + letra2 + numeros + "BR";
    }
}
