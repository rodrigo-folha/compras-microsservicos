package br.com.rodrigofolha.pedidos.client.represantation;

public record ClienteRepresentation(
        Long codigo,
        String nome,
        String cpf,
        String logradouro,
        String numero,
        String bairro,
        String email,
        String telefone,
        boolean ativo
) {
}
