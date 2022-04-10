package br.com.vr.miniautorizador.controller;

import br.com.vr.miniautorizador.enums.TipoRetornoErroEnum;
import br.com.vr.miniautorizador.model.CartaoProjection;
import br.com.vr.miniautorizador.model.Transacao;
import br.com.vr.miniautorizador.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "transacoes", description = "Endpoint para as operações de transação")
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @Operation(summary = "Realiza uma operação no cartão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caso a operação seja realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Caso o cartão não seja encontrado"),
            @ApiResponse(responseCode = "422", description = "Caso ocorra algum erro na transação",
                    content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = TipoRetornoErroEnum.class)),
                    })
    }
    )
    @PostMapping
    public ResponseEntity<?> realizarTransacao(@Valid @RequestBody Transacao transacao) {
        service.realizarTransacao(transacao);
        return ResponseEntity.ok().build();
    }

}
