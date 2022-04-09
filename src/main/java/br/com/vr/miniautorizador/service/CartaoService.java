package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.exception.CartaoExistenteException;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.CartaoProjection;
import br.com.vr.miniautorizador.model.Parametro;
import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.ParametroRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartaoService {

    private CartaoRepository cartaoRepository;
    private ParametroRepository parametroRepository;

    public CartaoService(CartaoRepository cartaoRepository, ParametroRepository parametroRepository) {
        this.cartaoRepository = cartaoRepository;
        this.parametroRepository = parametroRepository;
    }

    public Optional<CartaoProjection> create(Cartao cartao) {
        cartaoRepository.findById(cartao.getNumeroCartao()).ifPresentOrElse(
                (c) -> {
                    throw new CartaoExistenteException();
                },
                () -> {
                    Cartao novoCartao = configurarNovoCartao(cartao);
                    cartaoRepository.insert(novoCartao);
                }
        );
        return cartaoRepository.findProjectedByNumeroCartao(cartao.getNumeroCartao(), CartaoProjection.class);
    }

    private Cartao configurarNovoCartao(Cartao cartao) {
        Parametro saldoInicial = parametroRepository
                .findById("saldoInicial")
                .orElseThrow(() -> new RuntimeException("O parametro de saldo inicial não está configurado no repositório"));

        cartao.setSaldo(new BigDecimal(saldoInicial.getValor()));
        return cartao;
    }
}
