package com.alexviana.alexvianaprojeto.frontend.bean;

import com.alexviana.alexvianaprojeto.domain.Cliente;

import com.alexviana.alexvianaprojeto.exceptions.DAOException;
import com.alexviana.alexvianaprojeto.service.IClienteService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped; // Importe de jakarta.faces.view
import jakarta.inject.Named; // Importe de jakarta.inject

import org.primefaces.PrimeFaces; // Para interações programáticas com PrimeFaces

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Alex Viana
 * Managed Bean JSF para gerenciar a interface de Clientes no Front-End PrimeFaces.
 * Este bean atua como o intermediário entre a View (.xhtml) e a camada de serviço.
 */
@Named // Torna o bean acessível no EL (Expression Language) do JSF (ex: #{clienteBean.cliente})
@ViewScoped // Define o escopo do bean para a duração da view (ideal para formulários e tabelas)
// Requer que a classe implemente Serializable para @ViewScoped e @SessionScoped
public class ClienteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final IClienteService clienteService; // Serviço Spring injetado

    // Propriedades para o formulário de cadastro/edição
    private Cliente cliente;

    // Propriedades para a tabela de listagem
    private List<Cliente> clientes;
    private Cliente selectedCliente; // Para seleção única na tabela

    /**
     * Construtor para injeção de dependência do IClienteService.
     * O Spring injeta a instância de IClienteService automaticamente.
     * É importante ter um construtor público para a injeção.
     */
    public ClienteBean(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Método de inicialização do bean, chamado após a construção e injeção de dependências.
     * Carrega a lista inicial de clientes e inicializa um novo objeto Cliente.
     */
    @PostConstruct
    public void init() {
        clientes = new ArrayList<>();
        cliente = new Cliente();
        loadClientes(); // Carrega a lista de clientes ao iniciar a página
    }

    /**
     * Carrega todos os clientes do serviço para a lista que será exibida na tabela.
     */
    public void loadClientes() {
        try {
            this.clientes = new ArrayList<>(clienteService.buscarTodos());
        } catch (DAOException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar clientes", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método para cadastrar um novo cliente ou atualizar um cliente existente.
     * Chamado quando o botão de salvar no formulário é clicado.
     */
    public void saveCliente() {
        try {
            if (cliente.getId() == null) { // Se o ID for nulo, é um novo cadastro
                clienteService.cadastrar(cliente);
                addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente cadastrado com sucesso!");
            } else { // Se o ID existe, é uma alteração
                clienteService.alterar(cliente);
                addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente atualizado com sucesso!");
            }
            clearForm(); // Limpa o formulário após salvar
            loadClientes(); // Recarrega a lista de clientes para atualizar a tabela
            PrimeFaces.current().executeScript("PF('manageClienteDialog').hide()"); // Fecha o dialog
        } catch (DAOException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar cliente", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prepara o formulário para um novo cadastro.
     * Limpa o objeto cliente e abre o dialog.
     */
    public void newCliente() {
        this.cliente = new Cliente();
    }

    /**
     * Prepara o formulário para edição do cliente selecionado.
     * Se um cliente estiver selecionado na tabela, ele é copiado para o formulário.
     */
    public void editCliente() {
        if (selectedCliente != null) {
            this.cliente = selectedCliente; // Carrega o cliente selecionado para edição
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Selecione um cliente para editar.");
        }
    }

    /**
     * Exclui o cliente selecionado na tabela.
     */
    public void deleteCliente() {
        if (selectedCliente != null) {
            try {
                clienteService.excluir(selectedCliente);
                addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente excluído com sucesso!");
                selectedCliente = null; // Limpa a seleção
                loadClientes(); // Recarrega a lista
            } catch (DAOException e) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir cliente", e.getMessage());
                e.printStackTrace();
            }
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Selecione um cliente para excluir.");
        }
    }

    /**
     * Limpa os campos do formulário.
     */
    public void clearForm() {
        this.cliente = new Cliente();
    }

    /**
     * Adiciona uma mensagem ao FacesContext para ser exibida pelo p:growl.
     * @param severity O nível de severidade da mensagem (INFO, WARN, ERROR, FATAL).
     * @param summary O resumo da mensagem.
     * @param detail O detalhe da mensagem.
     */
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // --- Getters e Setters ---
    // (Gerados pelo Lombok, mas listados para clareza)

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Cliente getSelectedCliente() {
        return selectedCliente;
    }

    public void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente;
    }
}