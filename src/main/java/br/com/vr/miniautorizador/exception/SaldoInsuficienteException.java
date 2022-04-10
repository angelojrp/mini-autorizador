package br.com.vr.miniautorizador.exception;

import br.com.vr.miniautorizador.enums.TipoRetornoErroEnum;

public class SaldoInsuficienteException extends NegocioException{

    @Override
    public TipoRetornoErroEnum getTipoRetornoErroEnum() {
        return TipoRetornoErroEnum.SALDO_INSUFICIENTE;
    }
}
