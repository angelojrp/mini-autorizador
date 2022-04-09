package br.com.vr.miniautorizador.controller;

import br.com.vr.miniautorizador.exception.CartaoExistenteException;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.service.CartaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private final CartaoService service;

    public CartaoController(CartaoService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criarCartao(@Valid @RequestBody Cartao cartao){
        try{
            return ResponseEntity.ok(service.create(cartao));
        }catch (CartaoExistenteException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
        }
    }

    @GetMapping("{numeroCartao}")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable String numeroCartao){
        BigDecimal saldo = service.obterSaldo(numeroCartao);
        return ResponseEntity.ok(saldo);
    }
}
