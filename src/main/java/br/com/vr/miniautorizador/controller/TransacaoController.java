package br.com.vr.miniautorizador.controller;

import br.com.vr.miniautorizador.model.Transacao;
import br.com.vr.miniautorizador.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> realizarTransacao(@Valid @RequestBody Transacao transacao) {
        service.realizarTransacao(transacao);
        return ResponseEntity.ok().build();
    }

}
