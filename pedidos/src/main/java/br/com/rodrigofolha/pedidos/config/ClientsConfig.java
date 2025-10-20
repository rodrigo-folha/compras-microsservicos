package br.com.rodrigofolha.pedidos.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "br.com.rodrigofolha.pedidos.client")
public class ClientsConfig {
}
