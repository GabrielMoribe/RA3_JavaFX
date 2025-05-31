package org.example.projeto_javafx.entidades.user;

import jdk.jfr.DataAmount;
import lombok.*;

import java.io.Serializable;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    // definido um seriaVersionUID
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String telefone;

}
