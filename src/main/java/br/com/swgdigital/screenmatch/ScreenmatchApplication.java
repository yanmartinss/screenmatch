package br.com.swgdigital.screenmatch;

import br.com.swgdigital.screenmatch.model.DadosSerie;
import br.com.swgdigital.screenmatch.service.ConsumoApi;
import br.com.swgdigital.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConsumoApi consumoApi = new ConsumoApi();
        String json = consumoApi.obterDados("http://www.omdbapi.com/?apikey=85f39112&t=gilmore+girls");
        IO.println(json);
        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        IO.println(dados);
    }
}
