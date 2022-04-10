package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.exception.CartaoInexistenteException;
import br.com.vr.miniautorizador.exception.SaldoInsuficienteException;
import br.com.vr.miniautorizador.exception.SenhaInvalidaException;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.Transacao;
import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.TransacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {
    public static final String VAL_SALDO_INICIAL = "500";
    public static final String NUM_CARTAO = "1234123412341234";
    public static final String VAL_TRANSACAO = "200.55";
    public static final String SENHA = "1234567";

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CartaoRepository cartaoRepository;

    private TransacaoService service;

    private Cartao cartao;

    private Transacao transacao;

    @BeforeEach
    public void configurarCartao() {
        service = new TransacaoService(transacaoRepository, cartaoRepository);
        cartao = Cartao.builder().numeroCartao(NUM_CARTAO)
                .saldo(new BigDecimal(VAL_SALDO_INICIAL))
                .senha(SENHA)
                .version(0L).build();

        lenient().when(cartaoRepository.findById(any())).thenReturn(Optional.of(cartao));
    }

    @Nested
    class RealizarTransacoesTest {
        @BeforeEach
        public void init() {
            transacao = Transacao.builder().numeroCartao(NUM_CARTAO).valor(new BigDecimal(VAL_TRANSACAO)).senha(SENHA).build();
            lenient().when(transacaoRepository.insert(any(Transacao.class))).thenReturn(transacao);
        }

        @Test
        public void deveriaRealizarTransacao_quandoDadosValidos() {
            assertDoesNotThrow(() -> {
                service.realizarTransacao(transacao);
            });

            verify(transacaoRepository, times(1)).insert(any(Transacao.class));
            verify(cartaoRepository, times(1)).save(any(Cartao.class));
        }

        @Test
        public void deveriaRealizarTransacaoEAtualizarSaldo_quandoDadosValidos() {
            final BigDecimal saldoEsperado = new BigDecimal(VAL_SALDO_INICIAL).subtract(new BigDecimal(VAL_TRANSACAO));

            final Transacao transacaoPersistida = assertDoesNotThrow(() -> service.realizarTransacao(transacao));

            final BigDecimal saldoAtual = cartao.getSaldo();
            assertThat(saldoAtual,
                    allOf(
                            equalTo(saldoEsperado),
                            equalTo(transacaoPersistida.getSaldoRemanescente())
                    )
            );
            verify(transacaoRepository, times(1)).insert(any(Transacao.class));
            verify(cartaoRepository, times(1)).save(any(Cartao.class));
        }
    }

    @Nested
    class ValidacoesTransacaoTest {
        @Test
        public void deveriaLancarExcecao_quandoSenhaInvalida() {
            Transacao transacaoSenhaInv = Transacao.builder().numeroCartao(NUM_CARTAO)
                    .valor(new BigDecimal(VAL_TRANSACAO))
                    .senha("81723iowue").build();

            Assertions.assertThrows(SenhaInvalidaException.class, () -> service.realizarTransacao(transacaoSenhaInv));

            verify(transacaoRepository, times(0)).insert(any(Transacao.class));
            verify(cartaoRepository, times(0)).save(any(Cartao.class));
        }

        @Test
        public void deveriaLancarExcecao_quandoSaldoInsuficiente() {
            Transacao transacaoSaldoInv = Transacao.builder().numeroCartao(NUM_CARTAO)
                    .valor(new BigDecimal("1000"))
                    .senha(SENHA).build();

            Assertions.assertThrows(SaldoInsuficienteException.class, () -> service.realizarTransacao(transacaoSaldoInv));

            verify(transacaoRepository, times(0)).insert(any(Transacao.class));
            verify(cartaoRepository, times(0)).save(any(Cartao.class));
        }

        @Test
        public void deveriaLancarExcecao_quandoSaldoCartaoInexistente() {
            Transacao transacaoCartarInexistente = Transacao.builder().numeroCartao("11234567891234567")
                    .valor(new BigDecimal(VAL_TRANSACAO))
                    .senha(SENHA).build();
            when(cartaoRepository.findById(transacaoCartarInexistente.getNumeroCartao())).thenReturn(Optional.empty());
            Assertions.assertThrows(CartaoInexistenteException.class, () -> service.realizarTransacao(transacaoCartarInexistente));

            verify(transacaoRepository, times(0)).insert(any(Transacao.class));
            verify(cartaoRepository, times(0)).save(any(Cartao.class));
        }
    }


}
