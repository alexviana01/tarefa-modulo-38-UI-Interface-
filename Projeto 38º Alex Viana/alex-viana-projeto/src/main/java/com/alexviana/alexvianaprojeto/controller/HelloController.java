package com.alexviana.alexvianaprojeto.controller; // Ajuste o pacote se for diferente

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indica que esta classe é um controlador REST
public class HelloController {

    @GetMapping("/hello") // Mapeia requisições GET para a URL /hello
    public String sayHello() {
        return "Olá Alex Viana! Seu projeto Spring Boot está funcionando!";
    }

    @GetMapping("/") // Mapeia requisições GET para a URL raiz
    public String homePage() {
        return "Bem-vindo à página inicial do seu projeto!";
    }
}