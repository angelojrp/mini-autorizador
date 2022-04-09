package br.com.vr.miniautorizador.exception;

import br.com.vr.miniautorizador.enums.TipoRetornoErroEnum;

public class SenhaInvalidaException extends NegocioException{

    @Override
    public TipoRetornoErroEnum getTipoRetornoErroEnum() {
        return TipoRetornoErroEnum.SENHA_INVALIDA;
    }
}
