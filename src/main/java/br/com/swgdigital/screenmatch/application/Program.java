package br.com.swgdigital.screenmatch.application;

import br.com.swgdigital.screenmatch.model.DadosEpisodio;
import br.com.swgdigital.screenmatch.model.DadosSerie;
import br.com.swgdigital.screenmatch.model.DadosTemporada;
import br.com.swgdigital.screenmatch.model.Episodio;
import br.com.swgdigital.screenmatch.service.ConsumoApi;
import br.com.swgdigital.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Program {

    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=85f39112";

    public void showMenu() {

        IO.print("Digite o nome da série para busca: ");
        String serieTitle = sc.nextLine();
        String json = consumo.obterDados(ENDERECO + serieTitle.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        if (dados.totalSeasons() == null) {
            IO.println("Série não encontrada ou resposta inválida da API: " + dados.error());
            sc.close();
            return;
        }

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalSeasons(); i++) {
            json = consumo.obterDados(ENDERECO + serieTitle.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        /* temporadas.forEach(t -> t.episodes().forEach(e -> IO.println(e.title())));

        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");

        nomes.stream().sorted().limit(3).filter(n -> n.startsWith("N")).map(n -> n.toUpperCase()).forEach(IO::println);*/

        List<DadosEpisodio> dadosEpisodios = temporadas.stream().flatMap(t -> t.episodes().stream()).collect(Collectors.toList());

        IO.println("Top 5 episódios:");
        dadosEpisodios.stream()
                .filter(e -> !e.rate().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::rate).reversed())
                .limit(5)
                .forEach(IO::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episodio(t.season(), d)))
                .collect(Collectors.toList());

        episodios.forEach(IO::println);

        IO.print("From which year do you want to see the episodes? ");
        Integer year = sc.nextInt();

        LocalDate searchDate = LocalDate.of(year, 1, 1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> IO.println("Season: " + e.getSeason()
                        + "Episode: " + e.getTitle()
                        + "Release Date: " + e.getReleaseDate().format(dtf)));

        sc.close();
    }

}
