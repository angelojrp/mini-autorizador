package br.com.vr.miniautorizador.mapper;

import br.com.vr.miniautorizador.dto.RealizarTransacaoRequestDTO;
import br.com.vr.miniautorizador.model.Transacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransacaoMapper {
    TransacaoMapper INSTANCE = Mappers.getMapper(TransacaoMapper.class);

    @Mapping(target = "saldoRemanescente", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Transacao realizarTransacaoRequestDTOToEntity(RealizarTransacaoRequestDTO dto);
}
