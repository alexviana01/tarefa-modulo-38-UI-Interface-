package com.alexviana.alexvianaprojeto.service;

import com.alexviana.alexvianaprojeto.domain.Cliente;
import com.alexviana.alexvianaprojeto.exceptions.DAOException;
import com.alexviana.alexvianaprojeto.repository.ClienteRepository;
import com.alexviana.alexvianaprojeto.service.generic.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Alex Viana
 * Implementação do serviço para operações com a entidade Cliente.
 * Contém a lógica de negócio específica para clientes, como a validação de CPF único.
 */
@Service // Indica que esta classe é um componente de serviço Spring
public class ClienteService extends GenericService<Cliente, Long> implements IClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Construtor para injeção de dependência do ClienteRepository.
     * O {@code super(clienteRepository)} passa o repositório para o construtor da classe pai (GenericService).
     * @param clienteRepository O repositório de clientes injetado pelo Spring.
     */
    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        super(clienteRepository);
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Optional<Cliente> buscarPorCPF(Long cpf) throws DAOException {
        if (cpf == null) {
            throw new DAOException("O CPF para busca não pode ser nulo.");
        }
        try {
            return clienteRepository.findByCpf(cpf);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar cliente por CPF: " + cpf, e);
        }
    }

    @Override
    public List<Cliente> filtrarClientes(String query) {
        // Este método não lança DAOException no original, mantendo a consistência.
        // O repositório já trata a busca, e a lista pode ser vazia sem ser um erro.
        return clienteRepository.filtrarClientes(query);
    }

    @Override
    @Transactional // Garante que a operação de cadastro seja transacional
    public Cliente cadastrar(Cliente cliente) throws DAOException {
        if (cliente == null || cliente.getCpf() == null) {
            throw new DAOException("Dados do cliente ou CPF não podem ser nulos para cadastro.");
        }
        // Regra de negócio: CPF deve ser único no sistema.
        validarCpfUnico(cliente.getCpf());
        // Chama o método cadastrar da classe pai (GenericService) para persistir o cliente.
        try {
            return super.cadastrar(cliente);
        } catch (DAOException e) {
            // Re-lança a exceção se já for uma DAOException específica (ex: ID já existente)
            throw e;
        } catch (Exception e) {
            throw new DAOException("Erro inesperado ao cadastrar o cliente.", e);
        }
    }

    /**
     * Valida se um CPF já está cadastrado no sistema.
     * Este é um método de suporte privado que implementa uma regra de negócio.
     * @param cpf O CPF a ser validado.
     * @throws DAOException Se o CPF já for encontrado no sistema.
     */
    private void validarCpfUnico(Long cpf) throws DAOException {
        // Utiliza o método buscarPorCPF para verificar a existência do CPF.
        if (buscarPorCPF(cpf).isPresent()) {
            throw new DAOException("O CPF '" + cpf + "' já está cadastrado no sistema. Não é possível cadastrar clientes com CPF duplicado.");
        }
    }

    // Métodos de alterar, excluir, consultar (por ID) e buscarTodos são herdados e já implementados
    // na classe pai GenericService. Podem ser sobrescritos aqui se houver regras de negócio
    // específicas para clientes para essas operações.
}