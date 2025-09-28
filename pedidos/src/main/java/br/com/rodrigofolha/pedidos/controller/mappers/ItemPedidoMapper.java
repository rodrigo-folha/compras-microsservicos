package br.com.rodrigofolha.pedidos.controller.mappers;

import br.com.rodrigofolha.pedidos.controller.dto.ItemPedidoDTO;
import br.com.rodrigofolha.pedidos.model.ItemPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {
    ItemPedido map(ItemPedidoDTO dto);
}
