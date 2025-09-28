package br.com.rodrigofolha.icompras.produtos.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Data
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal valorUnitario;

    private boolean ativo;
}
