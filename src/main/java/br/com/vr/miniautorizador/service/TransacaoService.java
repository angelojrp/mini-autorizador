package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.Transacao;
import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.TransacaoRepository;
import br.com.vr.miniautorizador.strategy.ValidadorTransacaoFunction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static br.com.vr.miniautorizador.strategy.ValidadorTransacaoFunction.*;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CartaoRepository cartaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, CartaoRepository cartaoRepository) {
        this.transacaoRepository = transacaoRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Transacao realizarTransacao(Transacao transacao) {
        Optional<Cartao> optCartao = cartaoRepository.findById(transacao.getNumeroCartao());
        accept(optCartao, transacao);
        optCartao.ifPresent((cartao -> {
            atualizarSaldo(cartao, transacao);
            Transacao transacaoPersistida = salvarTransacao(transacao, cartao);
            salvarCartao(transacaoPersistida, cartao);
        }));
        return transacao;
    }

    private Transacao salvarTransacao(Transacao transacao, Cartao cartao){
        transacao.setSaldoRemanescente(cartao.getSaldo());
        return transacaoRepository.insert(transacao);
    }

    private void salvarCartao(Transacao transacao, Cartao cartao) {
        cartao.getTransacoes().add(transacao);
        cartaoRepository.save(cartao);
    }

    private void accept(final Optional<Cartao> cartao, final Transacao transacao) {
        final Stream<ValidadorTransacaoFunction> stream = Stream.of(validarCartaoExistente(), validarSenha(), validarSaldo());
        stream.forEach(f -> f.apply(cartao, transacao));
    }

    private Cartao atualizarSaldo(Cartao cartao, Transacao transacao){
        final BigDecimal novoSaldo = cartao.getSaldo().subtract(transacao.getValor());
        cartao.setSaldo(novoSaldo);
        return cartao;
    }
}
