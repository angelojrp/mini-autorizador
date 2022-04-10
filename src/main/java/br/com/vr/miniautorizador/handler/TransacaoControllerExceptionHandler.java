package br.com.vr.miniautorizador.handler;

import br.com.vr.miniautorizador.controller.TransacaoController;
import br.com.vr.miniautorizador.enums.TipoRetornoErroEnum;
import br.com.vr.miniautorizador.exception.CartaoInexistenteException;
import br.com.vr.miniautorizador.exception.NegocioException;
import br.com.vr.miniautorizador.exception.SaldoInsuficienteException;
import br.com.vr.miniautorizador.exception.SenhaInvalidaException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {TransacaoController.class})
public class TransacaoControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CartaoInexistenteException.class, SenhaInvalidaException.class, SaldoInsuficienteException.class})
    public ResponseEntity<TipoRetornoErroEnum> handleTransacaoException(NegocioException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getTipoRetornoErroEnum());
    }
}
