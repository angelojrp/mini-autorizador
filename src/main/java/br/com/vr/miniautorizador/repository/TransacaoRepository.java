package br.com.vr.miniautorizador.repository;

import br.com.vr.miniautorizador.model.Transacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends MongoRepository<Transacao, String> {
}
