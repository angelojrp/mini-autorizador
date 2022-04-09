package br.com.vr.miniautorizador.exception;

import br.com.vr.miniautorizador.enums.TipoRetornoErroEnum;

public class CartaoInexistenteException extends NegocioException{

    @Override
    public TipoRetornoErroEnum getTipoRetornoErroEnum() {
        return TipoRetornoErroEnum.CARTAO_INEXISTENTE;
    }

}
