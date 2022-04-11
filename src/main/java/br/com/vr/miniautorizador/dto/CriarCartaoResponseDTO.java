package br.com.vr.miniautorizador.dto;

import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriarCartaoResponseDTO {

    @EqualsAndHashCode.Include
    private String numeroCartao;

    private String senha;
}
