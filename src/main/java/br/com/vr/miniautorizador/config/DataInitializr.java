package br.com.vr.miniautorizador.config;

import br.com.vr.miniautorizador.model.Parametro;
import br.com.vr.miniautorizador.repository.ParametroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    private ParametroRepository parametroRepository;

    @Value("${vr.autorizador.cartao.saldo-inicial}")
    private String saldoInicial;

    public DataInitializr(ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        parametroRepository.findById("saldoInicial").ifPresentOrElse(
                v -> {
                    log.debug("Parametro [saldoInicial] já existe no repositório");
                },
                () -> criarParametroSaldoInicial());
    }

    private void criarParametroSaldoInicial() {
        final Parametro saldoInicial = Parametro.builder()
                .nome("saldoInicial")
                .valor(this.saldoInicial).build();
        parametroRepository.insert(saldoInicial);
    }
}
