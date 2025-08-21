package com.alexviana.alexvianaprojeto.exceptions;

/**
 * Exceção genérica para problemas de acesso a dados ou violação de regras de negócio específicas.
 * Estende {@code Exception} para ser uma exceção verificada (checked exception),
 * indicando que deve ser tratada ou declarada nos métodos que a lançam.
 * Em projetos maiores, pode-se optar por RuntimeException para exceções de negócio
 * e usar um {@code @ControllerAdvice} para tratamento global.
 *
 * @author Alex Viana
 */
public class DAOException extends Exception {
    private static final long serialVersionUID = 7054379063290825137L;

    /**
     * Construtor que aceita uma mensagem de erro.
     * @param msg A mensagem descritiva do erro.
     */
    public DAOException(String msg) {
        super(msg);
    }

    /**
     * Construtor que aceita uma mensagem de erro e a causa original da exceção.
     * @param msg A mensagem descritiva do erro.
     * @param ex A exceção original que causou este erro.
     */
    public DAOException(String msg, Throwable ex) {
        super(msg, ex);
    }
}