package com.alexviana.alexvianaprojeto.service.generic;

import com.alexviana.alexvianaprojeto.domain.Persistente;
import com.alexviana.alexvianaprojeto.exceptions.DAOException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional; // Importado para o retorno de Optional

/**
 * @author Alex Viana
 * Interface genérica para operações CRUD (Create, Read, Update e Delete) em entidades persistentes.
 * Define o contrato básico que todos os serviços genéricos devem seguir.
 *
 * @param <T> O tipo da entidade persistente.
 * @param <E> O tipo do identificador (ID) da entidade, que deve ser serializável.
 */
public interface IGenericService<T extends Persistente, E extends Serializable> {

    /**
     * Cadastra uma nova entidade no banco de dados.
     * @param entity A entidade a ser cadastrada.
     * @return A entidade cadastrada, que pode ter o ID preenchido após a persistência.
     * @throws DAOException Se ocorrer uma violação de regra de negócio (ex: ID já existente) ou erro de persistência.
     */
    T cadastrar(T entity) throws DAOException;

    /**
     * Exclui uma entidade existente do banco de dados.
     * @param entity A entidade a ser excluída. O ID da entidade é usado para identificação.
     * @throws DAOException Se a entidade não for encontrada ou ocorrer um erro durante a exclusão.
     */
    void excluir(T entity) throws DAOException;

    /**
     * Altera uma entidade existente no banco de dados.
     * @param entity A entidade com os dados atualizados. O ID da entidade é usado para identificação.
     * @return A entidade atualizada.
     * @throws DAOException Se a entidade não for encontrada ou ocorrer um erro durante a alteração.
     */
    T alterar(T entity) throws DAOException;

    /**
     * Consulta uma entidade pelo seu identificador (ID).
     * @param valor O ID da entidade a ser consultada.
     * @return Um {@link Optional} contendo a entidade se ela for encontrada, ou um {@code Optional.empty()}
     *         caso contrário.
     * @throws DAOException Se ocorrer um erro inesperado durante a consulta ao banco de dados.
     */
    Optional<T> consultar(E valor) throws DAOException; // Retorna Optional para melhor tratamento de "não encontrado"

    /**
     * Busca todas as entidades de um determinado tipo no banco de dados.
     * @return Uma {@link Collection} de todas as entidades encontradas.
     * @throws DAOException Se ocorrer um erro durante a busca de todas as entidades.
     */
    Collection<T> buscarTodos() throws DAOException;
}