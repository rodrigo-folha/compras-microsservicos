package br.com.rodrigofolha.pedidos.model.exception;

public class ItemNaoEcontradoException extends RuntimeException{
    public ItemNaoEcontradoException(String message) {
        super(message);
    }
}
