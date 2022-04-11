package br.com.vr.miniautorizador.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RealizarTransacaoRequestDTO {

    @EqualsAndHashCode.Include
    @Size(min = 16, max = 16, message = "O cartão deve ser composto por 16 digitos numéricos sem espaço ou traços ex: 1234123412341234")
    private String numeroCartao;

    @NotBlank
    private String senha;

    @Min(0)
    private BigDecimal valor;
}
