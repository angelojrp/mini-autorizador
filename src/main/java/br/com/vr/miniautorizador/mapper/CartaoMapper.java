package br.com.vr.miniautorizador.mapper;

import br.com.vr.miniautorizador.dto.CriarCartaoRequestDTO;
import br.com.vr.miniautorizador.dto.CriarCartaoResponseDTO;
import br.com.vr.miniautorizador.model.Cartao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartaoMapper {
    CartaoMapper INSTANCE = Mappers.getMapper(CartaoMapper.class);

    CriarCartaoResponseDTO entityToCriarCartaoResponseDTO(Cartao cartao);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "transacoes", ignore = true)
    @Mapping(target = "saldo", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Cartao criarCartaoRequestDTOToEntity(CriarCartaoRequestDTO dto);
}
