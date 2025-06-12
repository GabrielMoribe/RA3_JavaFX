package com.example.demo.entidades;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String telefone;
    private String senha;

    // Construtor para compatibilidade (sem senha)
    public User(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = ""; // Senha vazia por padrão
    }

    @Override
    public String toString() {
        return nome + " - " + email + " - " + telefone ;
    }
}