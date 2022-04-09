package br.com.vr.miniautorizador.handler;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@Component
public class ContratoPadraoErroHelper {

    public ResponseEntity<Object> criarResposta(WebRequest request, Throwable ex, HttpStatus status) {
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
