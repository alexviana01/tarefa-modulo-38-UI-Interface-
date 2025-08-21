package com.alexviana.alexvianaprojeto.controller;

import com.alexviana.alexvianaprojeto.domain.Cliente;
import com.alexviana.alexvianaprojeto.exceptions.DAOException;
import com.alexviana.alexvianaprojeto.service.IClienteService;
import jakarta.validation.Valid; // Anotação para ativar a validação do Bean Validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Enum para códigos de status HTTP
import org.springframework.http.ResponseEntity; // Classe para encapsular a resposta HTTP
import org.springframework.web.bind.annotation.*; // Anotações para mapeamento de requisições web

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Alex Viana
 * Controlador RESTful para a gestão de Clientes.
 * Expõe uma API para operações CRUD e consultas específicas de clientes.
 * Cada método é mapeado para um endpoint HTTP específico.
 */
@RestController // Indica que esta classe é um controlador REST
@RequestMapping("/api/clientes") // Define o caminho base para todos os endpoints deste controlador
public class ClienteController {

    private final IClienteService clienteService;

    /**
     * Construtor para injeção de dependência do serviço de cliente.
     * @param clienteService O serviço de cliente injetado pelo Spring.
     */
    @Autowired
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Endpoint para cadastrar um novo cliente.
     * Recebe um objeto Cliente no corpo da requisição e o valida.
     * POST /api/clientes
     * @param cliente Objeto Cliente a ser cadastrado.
     * @return ResponseEntity com o cliente cadastrado e status 201 Created, ou 400/500 em caso de erro.
     */
    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = clienteService.cadastrar(cliente);
            return new ResponseEntity<>(novoCliente, HttpStatus.CREATED); // Retorna 201 Created com o cliente
        } catch (DAOException e) {
            // Em cenários de produção, considere usar um @ControllerAdvice com @ExceptionHandler
            // para um tratamento global e mais robusto de exceções, retornando objetos de erro padronizados.
            System.err.println("Erro de DAO ao cadastrar cliente: " + e.getMessage()); // Log do erro
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Retorna 400 Bad Request
        } catch (Exception e) {
            System.err.println("Erro interno do servidor ao cadastrar cliente: " + e.getMessage()); // Log do erro
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 Internal Server Error
        }
    }

    /**
     * Endpoint para buscar todos os clientes.
     * GET /api/clientes
     * @return ResponseEntity com uma coleção de clientes e status 200 OK, ou 500 em caso de erro.
     */
    @GetMapping
    public ResponseEntity<Collection<Cliente>> buscarTodosClientes() {
        try {
            Collection<Cliente> clientes = clienteService.buscarTodos();
            return new ResponseEntity<>(clientes, HttpStatus.OK); // Retorna 200 OK com a lista de clientes
        } catch (DAOException e) {
            System.err.println("Erro de DAO ao buscar todos os clientes: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 Internal Server Error
        }
    }

    /**
     * Endpoint para buscar um cliente pelo ID.
     * GET /api/clientes/{id}
     * @param id ID do cliente a ser buscado.
     * @return ResponseEntity com o cliente encontrado e status 200 OK, ou 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        try {
            // O serviço agora retorna Optional, facilitando o tratamento de "não encontrado".
            Optional<Cliente> cliente = clienteService.consultar(id);
            return cliente.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Se presente, retorna 200 OK
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Se não, retorna 404 Not Found
        } catch (DAOException e) {
            System.err.println("Erro de DAO ao buscar cliente por ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 Internal Server Error
        }
    }

    /**
     * Endpoint para buscar um cliente pelo CPF.
     * GET /api/clientes/cpf/{cpf}
     * @param cpf CPF do cliente a ser buscado.
     * @return ResponseEntity com o cliente encontrado e status 200 OK, ou 404 Not Found.
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> buscarClientePorCpf(@PathVariable Long cpf) {
        try {
            Optional<Cliente> cliente = clienteService.buscarPorCPF(cpf);
            return cliente.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Se presente, retorna 200 OK
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Se não, retorna 404 Not Found
        } catch (DAOException e) {
            System.err.println("Erro de DAO ao buscar cliente por CPF: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 Internal Server Error
        }
    }

    /**
     * Endpoint para filtrar clientes por parte do nome.
     * GET /api/clientes/filtrar?nome={nome}
     * @param nome Parte do nome do cliente para filtro.
     * @return ResponseEntity com uma lista de clientes filtrados e status 200 OK.
     */
    @GetMapping("/filtrar")
    public ResponseEntity<List<Cliente>> filtrarClientes(@RequestParam("nome") String nome) {
        List<Cliente> clientes = clienteService.filtrarClientes(nome);
        return new ResponseEntity<>(clientes, HttpStatus.OK); // Retorna 200 OK com a lista filtrada (pode ser vazia)
    }

    /**
     * Endpoint para atualizar um cliente existente.
     * Recebe o ID na URL e os dados atualizados no corpo da requisição.
     * PUT /api/clientes/{id}
     * @param id ID do cliente a ser atualizado.
     * @param cliente Objeto Cliente com os dados atualizados.
     * @return ResponseEntity com o cliente atualizado e status 200 OK, 404 Not Found, ou 400/500 em caso de erro.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        try {
            cliente.setId(id); // Garante que o ID da URL seja usado para a atualização
            Cliente clienteAtualizado = clienteService.alterar(cliente);
            return new ResponseEntity<>(clienteAtualizado, HttpStatus.OK); // Retorna 200 OK com o cliente atualizado
        } catch (DAOException e) {
            System.err.println("Erro de DAO ao atualizar cliente: " + e.getMessage());
            // Se a mensagem indicar que a entidade não foi encontrada, retorna 404.
            // Em uma arquitetura real, uma exceção mais específica (ex: ResourceNotFoundException) seria lançada.
            if (e.getMessage() != null && e.getMessage().contains("não encontrada para atualização")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 Not Found
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Retorna 400 Bad Request para outras DAOExceptions
        } catch (Exception e) {
            System.err.println("Erro interno do servidor ao atualizar cliente: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 Internal Server Error
        }
    }

    /**
     * Endpoint para excluir um cliente.
     * DELETE /api/clientes/{id}
     * @param id ID do cliente a ser excluído.
     * @return ResponseEntity com status 204 No Content se sucesso, 404 Not Found, ou 400/500 em caso de erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        try {
            // Consulta o cliente primeiro para garantir que ele existe antes de tentar excluir.
            Optional<Cliente> clienteOptional = clienteService.consultar(id);
            if (clienteOptional.isPresent()) {
                clienteService.excluir(clienteOptional.get());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 No Content (sucesso sem corpo)
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 Not Found se o cliente não existir
            }
        } catch (DAOException e) {
            System.err.println("Erro de DAO ao excluir cliente: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Retorna 400 Bad Request para erros de DAO
        } catch (Exception e) {
            System.err.println("Erro interno do servidor ao excluir cliente: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 Internal Server Error
        }
    }
}