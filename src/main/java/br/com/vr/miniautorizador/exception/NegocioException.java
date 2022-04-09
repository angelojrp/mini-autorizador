package br.com.vr.miniautorizador.exception;

import br.com.vr.miniautorizador.enums.TipoRetornoErroEnum;

public class NegocioException extends RuntimeException {

    public TipoRetornoErroEnum getTipoRetornoErroEnum() {
        return TipoRetornoErroEnum.ERRO_GENERICO;
    }
}
