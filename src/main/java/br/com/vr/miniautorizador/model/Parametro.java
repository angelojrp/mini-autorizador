package br.com.vr.miniautorizador.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Parametro {

    @Id
    private String nome;

    private String valor; //definido como string para não precisar criar mecanismo de tratamento/conversão de tipos.

}
