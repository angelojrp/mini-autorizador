package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.dto.CriarCartaoRequestDTO;
import br.com.vr.miniautorizador.dto.CriarCartaoResponseDTO;
import br.com.vr.miniautorizador.exception.CartaoExistenteException;
import br.com.vr.miniautorizador.exception.CartaoInexistenteException;
import br.com.vr.miniautorizador.mapper.CartaoMapper;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.Parametro;
import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.ParametroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;
    private final ParametroRepository parametroRepository;

    public CartaoService(CartaoRepository cartaoRepository, ParametroRepository parametroRepository) {
        this.cartaoRepository = cartaoRepository;
        this.parametroRepository = parametroRepository;
    }

    public Optional<CriarCartaoResponseDTO> create(CriarCartaoRequestDTO dto) {
        final Cartao cartao = CartaoMapper.INSTANCE.criarCartaoRequestDTOToEntity(dto);
        cartaoRepository.findById(cartao.getNumeroCartao()).ifPresentOrElse(
                (c) -> {
                    throw new CartaoExistenteException();
                },
                () -> {
                    Cartao novoCartao = configurarNovoCartao(cartao);
                    cartaoRepository.insert(novoCartao);
                }
        );
        return cartaoRepository.findProjectedByNumeroCartao(cartao.getNumeroCartao(), CriarCartaoResponseDTO.class);
    }

    private Cartao configurarNovoCartao(Cartao cartao) {
        Parametro saldoInicial = parametroRepository
                .findById("saldoInicial")
                .orElseThrow(() -> new RuntimeException("O parametro de saldo inicial não está configurado no repositório"));

        cartao.setSaldo(new BigDecimal(saldoInicial.getValor()));
        return cartao;
    }

    public BigDecimal obterSaldo(String numeroCartao) {
        return cartaoRepository.findById(numeroCartao).orElseThrow(CartaoInexistenteException::new).getSaldo();
    }
}
