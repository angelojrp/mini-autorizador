package br.com.vr.miniautorizador.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transacao {

    @Id
    @BsonId
    @EqualsAndHashCode.Include
    private String id;

    @Size(min = 16, max = 16)
    private String numeroCartao;

    private String senha;

    private BigDecimal valor;

    //Informações abaixo foram incluidas para facilitar possiveis auditorias
    private BigDecimal saldoRemanescente;

    @CreatedDate
    private Instant dataCadastro;

    @LastModifiedDate
    private Instant dataAtualizacao;
}
