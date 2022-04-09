package br.com.vr.miniautorizador.handler;

import br.com.vr.miniautorizador.controller.CartaoController;
import br.com.vr.miniautorizador.exception.CartaoInexistenteException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {CartaoController.class})
public class CartaoControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CartaoInexistenteException.class})
    public ResponseEntity<?> handleCartaoInexistenteException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
