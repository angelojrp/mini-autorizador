package br.com.vr.miniautorizador.controller;

import br.com.vr.miniautorizador.exception.CartaoExistenteException;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.service.CartaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private CartaoService service;

    public CartaoController(CartaoService service){

        this.service = service;
    }
    @PostMapping
    public ResponseEntity<?> criarCartao(@RequestBody Cartao cartao){
        try{
            return ResponseEntity.ok(service.create(cartao));
        }catch (CartaoExistenteException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
        }
    }
}
