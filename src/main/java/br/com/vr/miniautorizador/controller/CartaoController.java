package br.com.vr.miniautorizador.controller;

import br.com.vr.miniautorizador.exception.CartaoExistenteException;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.CartaoProjection;
import br.com.vr.miniautorizador.service.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Tag(name = "cartoes", description = "Endpoint para manutenção de cartões")
@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private final CartaoService service;

    public CartaoController(CartaoService service) {
        this.service = service;
    }

    @Operation(summary = "Adiciona um novo cartão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caso o cartão seja inserido com sucesso",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CartaoProjection.class)),
                    }),
            @ApiResponse(responseCode = "422", description = "Caso o cartão já esteja cadastrado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CartaoProjection.class)),
                    })
    }
    )
    @PostMapping
    public ResponseEntity<?> criarCartao(@Valid @RequestBody Cartao cartao) {
        try {
            return ResponseEntity.ok(service.create(cartao));
        } catch (CartaoExistenteException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
        }
    }

    @Operation(summary = "Consultar o saldo do cartão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se a operação for realizada com sucesso",
                    content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = BigDecimal.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Caso o cartão não exista"
            )
    }
    )
    @GetMapping("{numeroCartao}")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable String numeroCartao) {
        BigDecimal saldo = service.obterSaldo(numeroCartao);
        return ResponseEntity.ok(saldo);
    }
}
