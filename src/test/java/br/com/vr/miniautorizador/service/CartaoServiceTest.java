package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.dto.CriarCartaoRequestDTO;
import br.com.vr.miniautorizador.dto.CriarCartaoResponseDTO;
import br.com.vr.miniautorizador.exception.CartaoExistenteException;
import br.com.vr.miniautorizador.exception.CartaoInexistenteException;
import br.com.vr.miniautorizador.model.Cartao;
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
    public static final String NUM_CARTAO = "1234123412341234";
    public static final String SENHA = "1234567";

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private ParametroRepository parametroRepository;
    private CartaoService service;

    private Cartao cartao;
    private CriarCartaoRequestDTO requestDTO;
    private CriarCartaoResponseDTO responseDTO;

    @BeforeEach
    public void init() {
        service = new CartaoService(cartaoRepository, parametroRepository);

        responseDTO = CriarCartaoResponseDTO.builder().numeroCartao(NUM_CARTAO).senha(SENHA).build();

        cartao = Cartao.builder() //
                .numeroCartao(NUM_CARTAO) //
                .senha(SENHA) //
                .build();
        requestDTO = CriarCartaoRequestDTO.builder()
                .numeroCartao(NUM_CARTAO)
                .senha(SENHA).build();
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
            when(cartaoRepository.findProjectedByNumeroCartao(Mockito.any(), eq(CriarCartaoResponseDTO.class)))
                    .thenReturn(Optional.of(responseDTO));

            CriarCartaoResponseDTO cartaoRetorno = Assertions.assertDoesNotThrow(() -> service.create(requestDTO).orElseThrow());

            assertThat(cartaoRetorno,
                    allOf(
                            hasProperty("numeroCartao", equalTo(NUM_CARTAO)),
                            hasProperty("senha", equalTo(SENHA))
                    )
            );

            verify(cartaoRepository, times(1)).insert(Mockito.any(Cartao.class));
            verify(cartaoRepository, times(1)).findById(Mockito.any());
            verify(cartaoRepository, times(1)).findProjectedByNumeroCartao(Mockito.any(), eq(CriarCartaoResponseDTO.class));
        }

        @Test
        public void deveriaLancarExcecao_quandoCartaoExistente() {
            when(cartaoRepository.findById(any())).thenReturn(Optional.of(cartao));

            Assertions.assertThrows(CartaoExistenteException.class, () -> service.create(requestDTO));

            verify(cartaoRepository, times(0)).insert(Mockito.any(Cartao.class));
            verify(cartaoRepository, times(1)).findById(Mockito.any());
        }
    }

    @Nested
    class ConsultarSaldoTest {

        @BeforeEach
        public void init() {
            Mockito.lenient()
                    .when(cartaoRepository.insert(Mockito.any(Cartao.class))).then(returnsFirstArg());

            Mockito.lenient()
                    .when(parametroRepository.findById("saldoInicial"))
                    .thenReturn(Optional.of(Parametro.builder().nome("saldoInicial").valor(VAL_SALDO_INICIAL).build()));
        }

        @Test
        public void deveriaTerSaldoInicial_quandoCartaoRecemCriado() {
            final Cartao cartao = Cartao.builder().numeroCartao(NUM_CARTAO)
                    .senha(SENHA)
                    .saldo( new BigDecimal(VAL_SALDO_INICIAL)).build();
            when(cartaoRepository.findById(Mockito.any()))
                    .thenReturn(Optional.of(cartao));

            BigDecimal saldo = Assertions.assertDoesNotThrow(() -> service.obterSaldo(cartao.getNumeroCartao()));

            BigDecimal saldoEsperado = new BigDecimal(VAL_SALDO_INICIAL);
            assertThat(saldo, equalTo(saldoEsperado));

            verify(cartaoRepository, times(1)).findById(Mockito.any());
        }

        @Test
        public void deveriaLancarExcecao_quandoCartaoNaoExiste() {
            when(cartaoRepository.findById(Mockito.any()))
                    .thenReturn(Optional.empty());

            Assertions.assertThrows(CartaoInexistenteException.class, () -> service.obterSaldo(cartao.getNumeroCartao()));

            verify(cartaoRepository, times(1)).findById(Mockito.any());
        }
    }
}
