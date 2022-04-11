package br.com.vr.miniautorizador.controller;

import br.com.vr.miniautorizador.dto.CriarCartaoRequestDTO;
import br.com.vr.miniautorizador.dto.CriarCartaoResponseDTO;
import br.com.vr.miniautorizador.exception.CartaoExistenteException;
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
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CriarCartaoResponseDTO.class)),
                    }),
            @ApiResponse(responseCode = "422", description = "Caso o cartão já esteja cadastrado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CriarCartaoResponseDTO.class)),
                    })
    }
    )
    @PostMapping
    public ResponseEntity<?> criarCartao(@Valid @RequestBody CriarCartaoRequestDTO dto) {
        try {
            return ResponseEntity.ok(service.create(dto));
        } catch (CartaoExistenteException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(dto);
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
