package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.exception.CartaoExistenteException;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartaoServiceTest {
    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private ParametroRepository parametroRepository;

    private CartaoService service;

    @Nested
    class CriarCartaoTest {

        private Cartao cartao;
        private CartaoProjection cartaoProjecao;

        @BeforeEach
        public void init() {
            service = new CartaoService(cartaoRepository, parametroRepository);
            cartao = Cartao.builder() //
                    .numeroCartao("6549873025634501") //
                    .senha("123456") //
                    .build();

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

            Mockito.lenient()
                    .when(cartaoRepository.insert(Mockito.any(Cartao.class))).then(returnsFirstArg());

            Mockito.lenient()
                    .when(parametroRepository.findById("saldoInicial"))
                    .thenReturn(Optional.of(Parametro.builder().nome("saldoInicial").valor("500").build()));
        }

        @Test
        public void deveriaCriarCartao_quandoDadosValidos() {
            when(cartaoRepository.findById(Mockito.any())).thenReturn(Optional.empty());
            when(cartaoRepository.findProjectedByNumeroCartao(Mockito.any(), eq(CartaoProjection.class)))
                    .thenReturn(Optional.of(cartaoProjecao));

            CartaoProjection cartao = Assertions.assertDoesNotThrow(() -> service.create(this.cartao).orElseThrow());

            assertThat(cartao,
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

            Assertions.assertThrows(CartaoExistenteException.class, () -> service.create(this.cartao));

            verify(cartaoRepository, times(0)).insert(Mockito.any(Cartao.class));
            verify(cartaoRepository, times(1)).findById(Mockito.any());
        }


    }
}
