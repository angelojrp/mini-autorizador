package br.com.vr.miniautorizador.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cartao implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Size(min = 16, max = 16, message = "O cartão deve ser composto por 16 digitos numéricos sem espaço ou traços ex: 1234123412341234")
    private String numeroCartao;

    @NotBlank
    private String senha;

    private BigDecimal saldo;

    @Version
    private Long version;

    @JsonIgnore
    @DBRef
    @Builder.Default
    private List<Transacao> transacoes = new ArrayList<>();

    @CreatedDate
    private Instant dataCadastro;

    @LastModifiedDate
    private Instant dataAtualizacao;
}
