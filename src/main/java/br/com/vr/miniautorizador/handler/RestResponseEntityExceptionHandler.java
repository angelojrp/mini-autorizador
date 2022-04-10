package br.com.vr.miniautorizador.handler;

import br.com.vr.miniautorizador.exception.NegocioException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order() //somente será utilizado caso nenhum outro advice consiga manipualar a exceção
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ContratoPadraoErroHelper contratoPadraoErroHelper;

    public RestResponseEntityExceptionHandler(ContratoPadraoErroHelper contratoPadraoErroHelper) {
        this.contratoPadraoErroHelper = contratoPadraoErroHelper;
    }

    @ExceptionHandler(value = {HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<Object> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex, WebRequest request) {
        return contratoPadraoErroHelper.criarResposta(request, ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, HttpHeaders headers, WebRequest request) {
        return contratoPadraoErroHelper.criarResposta(request, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NegocioException.class})
    public ResponseEntity<Object> handleNegocioException(NegocioException ex, WebRequest request) {
        return contratoPadraoErroHelper.criarResposta(request, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return contratoPadraoErroHelper.criarResposta(request, ex, status);
    }

}
