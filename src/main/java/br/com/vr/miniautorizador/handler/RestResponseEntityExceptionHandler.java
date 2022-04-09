package br.com.vr.miniautorizador.handler;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<Object> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex, WebRequest request) {
        return contratoPadraoErro(request, ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, HttpHeaders headers, WebRequest request) {
        return contratoPadraoErro(request, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return contratoPadraoErro(request, ex, status);
    }

    private ResponseEntity<Object> contratoPadraoErro(WebRequest request, Throwable ex, HttpStatus status) {
        ServletWebRequest swr = (ServletWebRequest) request;

        ErrorModel body = ErrorModel.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .message(ex.getMessage())
                .method(swr.getRequest().getMethod())
                .path(swr.getRequest().getRequestURI()).build();

        return ResponseEntity.status(status).body(body);
    }

    @Builder
    @Data
    private static class ErrorModel{
        private int status;
        private String message;
        private Instant timestamp;
        private String path;
        private String method;
    }

}
