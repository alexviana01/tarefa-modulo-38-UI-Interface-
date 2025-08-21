package com.alexviana.alexvianaprojeto.repository;

import com.alexviana.alexvianaprojeto.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alex Viana
 * Repositório Spring Data JPA para a entidade Cliente.
 * Fornece métodos CRUD (Create, Read, Update, Delete) e de consulta personalizados.
 * O Spring Data JPA automaticamente gera as implementações para os métodos declarados.
 */
@Repository // Indica que esta interface é um componente de repositório Spring
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca um cliente pelo CPF.
     * O Spring Data JPA infere a query automaticamente pelo nome do método.
     * @param cpf O CPF do cliente a ser buscado.
     * @return Um {@link Optional} contendo o cliente, se encontrado, ou um {@code Optional.empty()} caso contrário.
     */
    Optional<Cliente> findByCpf(Long cpf);

    /**
     * Filtra clientes por parte do nome, realizando uma busca case-insensitive.
     * Utiliza uma JPQL (Java Persistence Query Language) para uma consulta personalizada.
     * @param nome Parte do nome a ser pesquisada.
     * @return Uma {@link List} de clientes que correspondem ao critério de busca.
     */
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Cliente> filtrarClientes(@Param("nome") String nome);
}