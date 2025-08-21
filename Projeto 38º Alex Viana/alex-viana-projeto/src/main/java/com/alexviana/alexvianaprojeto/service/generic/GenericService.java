package com.alexviana.alexvianaprojeto.service.generic;

import com.alexviana.alexvianaprojeto.domain.Persistente;
import com.alexviana.alexvianaprojeto.exceptions.DAOException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Alex Viana
 * Implementação abstrata e genérica de um serviço para operações CRUD.
 * Esta classe fornece a lógica básica de persistência que pode ser estendida
 * por serviços específicos para cada entidade.
 *
 * @param <T> O tipo da entidade persistente.
 * @param <E> O tipo do identificador (ID) da entidade.
 */
public abstract class GenericService<T extends Persistente, E extends Serializable> implements IGenericService<T, E> {

    protected JpaRepository<T, E> repository;

    /**
     * Construtor do serviço genérico.
     * @param repository O repositório JPA que será utilizado para as operações de persistência.
     */
    public GenericService(JpaRepository<T, E> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional // Garante que a operação seja executada dentro de uma transação de banco de dados
    public T cadastrar(T entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Erro ao cadastrar: Entidade não pode ser nula.");
        }
        // Validação se já existe uma entidade com o mesmo ID.
        // Em um cenário real, o ID é geralmente gerado pelo banco para novas entidades.
        // Se a entidade já tiver um ID, é mais provável que seja uma tentativa de alteração.
        if (entity.getId() != null && repository.existsById((E) entity.getId())) {
            throw new DAOException("Erro ao cadastrar: Entidade com ID " + entity.getId() + " já existe no sistema.");
        }
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new DAOException("Erro inesperado ao cadastrar a entidade.", e);
        }
    }

    @Override
    @Transactional
    public void excluir(T entity) throws DAOException {
        if (entity == null || entity.getId() == null) {
            throw new DAOException("Erro ao excluir: Entidade ou ID da entidade não podem ser nulos.");
        }
        // Verifica se a entidade realmente existe antes de tentar excluir.
        if (!repository.existsById((E) entity.getId())) {
            throw new DAOException("Erro ao excluir: Entidade com ID " + entity.getId() + " não encontrada.");
        }
        try {
            repository.delete(entity);
        } catch (Exception e) {
            throw new DAOException("Erro inesperado ao excluir a entidade com ID " + entity.getId(), e);
        }
    }

    @Override
    @Transactional
    public T alterar(T entity) throws DAOException {
        if (entity == null || entity.getId() == null) {
            throw new DAOException("Erro ao alterar: Entidade ou ID da entidade não podem ser nulos.");
        }
        // Verifica se a entidade existe para ser alterada.
        if (!repository.existsById((E) entity.getId())) {
            throw new DAOException("Erro ao alterar: Entidade com ID " + entity.getId() + " não encontrada para atualização.");
        }
        try {
            return repository.save(entity); // save() funciona como update se o ID já existe
        } catch (Exception e) {
            throw new DAOException("Erro inesperado ao alterar a entidade com ID " + entity.getId(), e);
        }
    }

    @Override
    public Optional<T> consultar(E valor) throws DAOException {
        if (valor == null) {
            throw new DAOException("Erro ao consultar: O ID não pode ser nulo.");
        }
        try {
            return repository.findById(valor);
        } catch (Exception e) {
            throw new DAOException("Erro inesperado ao consultar registro com ID: " + valor, e);
        }
    }

    @Override
    public Collection<T> buscarTodos() throws DAOException {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new DAOException("Erro inesperado ao buscar todos os registros.", e);
        }
    }
}