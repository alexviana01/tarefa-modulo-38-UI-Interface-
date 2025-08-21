package com.alexviana.alexvianaprojeto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; // Ensure this is here
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_CLIENTE")
@NamedQuery(name = "Cliente.findByNome", query = "SELECT c FROM Cliente c WHERE c.nome LIKE :nome")
@Data // This *should* generate getId(), setId(), getCpf(), setCpf()
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente implements Persistente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
    @SequenceGenerator(name = "cliente_seq", sequenceName = "sq_cliente", initialValue = 1, allocationSize = 1)
    private Long id; // This field corresponds to getId() and setId()

    @Column(name = "NOME", nullable = false, length = 50)
    private String nome;

    @Column(name = "CPF", nullable = false, unique = true)
    private Long cpf; // This field corresponds to getCpf() and setCpf()

    // ... (other fields)

    // IF @Data DOESN'T WORK OR Persistente requires abstract methods:
    // @Override
    // public Long getId() {
    //     return this.id;
    // }

    // @Override
    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public Long getCpf() {
    //     return this.cpf;
    // }
    //
    // public void setCpf(Long cpf) {
    //    this.cpf = cpf;
    // }

    // ... (other getters/setters if not using @Data)
}