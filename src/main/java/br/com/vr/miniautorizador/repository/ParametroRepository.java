package br.com.vr.miniautorizador.repository;

import br.com.vr.miniautorizador.model.Parametro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroRepository extends MongoRepository<Parametro, String> {
}
