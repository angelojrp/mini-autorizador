package br.com.vr.miniautorizador.repository;

import br.com.vr.miniautorizador.model.Cartao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends MongoRepository<Cartao, String> {

    <T> Optional<T> findProjectedByNumeroCartao(String id, Class<T> projection);


}
