package br.com.vr.miniautorizador.strategy;

import br.com.vr.miniautorizador.exception.CartaoInexistenteException;
import br.com.vr.miniautorizador.exception.NegocioException;
import br.com.vr.miniautorizador.exception.SaldoInsuficienteException;
import br.com.vr.miniautorizador.exception.SenhaInvalidaException;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.Transacao;

import java.util.Optional;
import java.util.function.BiFunction;


@FunctionalInterface
public interface ValidadorTransacaoFunction extends BiFunction<Optional<Cartao>, Transacao,Optional<Cartao>> {

    @Override
    Optional<Cartao> apply(Optional<Cartao> cartao, Transacao transacao);

    static ValidadorTransacaoFunction validarCartaoExistente() {
        return (optCartao, transacao) -> Optional.ofNullable(optCartao.orElseThrow(CartaoInexistenteException::new));
    }

    static ValidadorTransacaoFunction validarSenha() {
        return  (optCartao, transacao) -> {
            final Cartao resposta = optCartao.filter(cartao -> cartao.getSenha().equals(transacao.getSenha()))
                    .orElseThrow(SenhaInvalidaException::new);
            return Optional.ofNullable(resposta);
        };
    }

    static ValidadorTransacaoFunction validarSaldo() throws NegocioException {
        return  (optCartao, transacao) -> {
            final Cartao resposta = optCartao.filter(c -> transacao.getValor().compareTo(c.getSaldo()) < 0)
                    .orElseThrow(SaldoInsuficienteException::new);
            return Optional.ofNullable(resposta);
        };
    }

}