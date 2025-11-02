package br.com.rodrigofolha.icompras.faturamento;

import br.com.rodrigofolha.icompras.faturamento.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GeradorNotaFiscalService {

    public void gerar(Pedido pedido) {
        log.info("Gerada a Nota Fiscal para o pedido: {} ", pedido.codigo());
    }
}
