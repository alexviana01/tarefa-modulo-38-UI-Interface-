package com.alexviana.alexvianaprojeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Alex Viana
 * Classe principal da aplicação Spring Boot para o projeto de gerenciamento de clientes.
 * Esta classe inicializa e executa a aplicação web.
 */
@SpringBootApplication // Anotação que combina @Configuration, @EnableAutoConfiguration e @ComponentScan
public class AlexVianaProjetoApplication {

    /**
     * Método principal que inicia a aplicação Spring Boot.
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        SpringApplication.run(AlexVianaProjetoApplication.class, args);
    }
}