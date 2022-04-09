package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.exception.CartaoExistenteException;
import br.com.vr.miniautorizador.exception.CartaoInexistenteException;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.CartaoProjection;
import br.com.vr.miniautorizador.model.Parametro;
import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.ParametroRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartaoServiceTest {
    public static final String VAL_SALDO_INICIAL = "500";
    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private ParametroRepository parametroRepository;
    private CartaoService service;

    private Cartao cartao;
    private CartaoProjection cartaoProjecao;

    @BeforeEach
    public void init() {
        service = new CartaoService(cartaoRepository, parametroRepository);

        cartaoProjecao = new CartaoProjection() {
            @Override
            public String getNumeroCartao() {
                return "6549873025634501";
            }

            @Override
            public String getSenha() {
                return "123456";
            }
        };

        cartao = Cartao.builder() //
                .numeroCartao("6549873025634501") //
                .senha("123456") //
                .build();
    }

    @Nested
    class CriarCartaoTest {

        @BeforeEach
        public void init() {
            Mockito.lenient()
                    .when(cartaoRepository.insert(Mockito.any(Cartao.class))).then(returnsFirstArg());

            Mockito.lenient()
                    .when(parametroRepository.findById("saldoInicial"))
                    .thenReturn(Optional.of(Parametro.builder().nome("saldoInicial").valor(VAL_SALDO_INICIAL).build()));
        }

        @Test
        public void deveriaCriarCartao_quandoDadosValidos() {
            when(cartaoRepository.findById(Mockito.any())).thenReturn(Optional.empty());
            when(cartaoRepository.findProjectedByNumeroCartao(Mockito.any(), eq(CartaoProjection.class)))
                    .thenReturn(Optional.of(cartaoProjecao));

            CartaoProjection cartaoRetorno = Assertions.assertDoesNotThrow(() -> service.create(cartao).orElseThrow());

            assertThat(cartaoRetorno,
                    allOf(
                            hasProperty("numeroCartao", equalTo("6549873025634501")),
                            hasProperty("senha", equalTo("123456"))
                    )
            );

            verify(cartaoRepository, times(1)).insert(Mockito.any(Cartao.class));
            verify(cartaoRepository, times(1)).findById(Mockito.any());
            verify(cartaoRepository, times(1)).findProjectedByNumeroCartao(Mockito.any(), eq(CartaoProjection.class));
        }

        @Test
        public void deveriaLancarExcecao_quandoCartaoExistente() {
            when(cartaoRepository.findById(any())).thenReturn(Optional.of(cartao));

            Assertions.assertThrows(CartaoExistenteException.class, () -> service.create(cartao));

            verify(cartaoRepository, times(0)).insert(Mockito.any(Cartao.class));
            verify(cartaoRepository, times(1)).findById(Mockito.any());
        }
    }

    @Nested
    class ConsultarSaldoTest{

        @BeforeEach
        public void init() {
            Mockito.lenient()
                    .when(cartaoRepository.insert(Mockito.any(Cartao.class))).then(returnsFirstArg());

            Mockito.lenient()
                    .when(parametroRepository.findById("saldoInicial"))
                    .thenReturn(Optional.of(Parametro.builder().nome("saldoInicial").valor(VAL_SALDO_INICIAL).build()));
        }

        @Test
        public void deveriaTerSaldoInicial_quandoCartaoRecemCriado(){
            when(cartaoRepository.findById(Mockito.any()))
                    .thenReturn(Optional.empty())
                    .thenReturn(Optional.of(cartao));

            when(cartaoRepository.findProjectedByNumeroCartao(Mockito.any(), eq(CartaoProjection.class)))
                    .thenReturn(Optional.of(cartaoProjecao));

            Assertions.assertDoesNotThrow(() -> service.create(cartao).orElseThrow());

            BigDecimal saldo = Assertions.assertDoesNotThrow(() -> service.obterSaldo(cartao.getNumeroCartao()));

            BigDecimal saldoEsperado = new BigDecimal(VAL_SALDO_INICIAL);
            assertThat(saldo, equalTo(saldoEsperado));

            verify(cartaoRepository, times(1)).insert(Mockito.any(Cartao.class));
            verify(cartaoRepository, times(2)).findById(Mockito.any());
            verify(cartaoRepository, times(1)).findProjectedByNumeroCartao(Mockito.any(), Mockito.any());
        }

        @Test
        public void deveriaLancarExcecao_quandoCartaoNaoExiste(){
            when(cartaoRepository.findById(Mockito.any()))
                    .thenReturn(Optional.empty());

            Assertions.assertThrows(CartaoInexistenteException.class, () -> service.obterSaldo(cartao.getNumeroCartao()));

            verify(cartaoRepository, times(1)).findById(Mockito.any());
        }
    }
}
