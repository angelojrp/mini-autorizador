package br.com.vr.miniautorizador.exception;

import br.com.vr.miniautorizador.enums.TipoRetornoErroEnum;
import lombok.Data;

@Data
public class NegocioException extends RuntimeException {

    private TipoRetornoErroEnum tipoRetornoErroEnum;
}
