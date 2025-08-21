package com.alexviana.alexvianaprojeto.service;

import com.alexviana.alexvianaprojeto.domain.Cliente;
import com.alexviana.alexvianaprojeto.exceptions.DAOException;
import com.alexviana.alexvianaprojeto.service.generic.IGenericService;

import java.util.List;
import java.util.Optional;

/**
 * @author Alex Viana
 * Interface de serviço específica para operações com a entidade Cliente.
 * Estende a interface genérica {@link IGenericService} para herdar as operações CRUD básicas.
 */
public interface IClienteService extends IGenericService<Cliente, Long> {

    /**
     * Busca um cliente pelo seu número de CPF.
     * @param cpf O CPF do cliente a ser buscado.
     * @return Um {@link Optional} contendo o cliente se encontrado, ou um {@code Optional.empty()} caso contrário.
     * @throws DAOException Se ocorrer um erro durante o acesso a dados.
     */
    Optional<Cliente> buscarPorCPF(Long cpf) throws DAOException;

    /**
     * Filtra clientes com base em uma parte do nome.
     * @param query A string de busca para o nome.
     * @return Uma {@link List} de clientes que correspondem à query.
     */
    List<Cliente> filtrarClientes(String query);

    // O método validarCpfUnico foi removido da interface, pois é uma regra de negócio
    // interna da implementação do serviço e não um método público do contrato.
}