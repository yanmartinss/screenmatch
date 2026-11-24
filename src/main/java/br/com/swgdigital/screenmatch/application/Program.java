package br.com.swgdigital.screenmatch.application;

import br.com.swgdigital.screenmatch.model.DadosEpisodio;
import br.com.swgdigital.screenmatch.model.DadosSerie;
import br.com.swgdigital.screenmatch.model.DadosTemporada;
import br.com.swgdigital.screenmatch.model.Episodio;
import br.com.swgdigital.screenmatch.service.ConsumoApi;
import br.com.swgdigital.screenmatch.service.ConverteDados;

import java.util.*;
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

      /*  IO.println("Top 10 episódios:");
        dadosEpisodios.stream()
                .filter(e -> !e.rate().equalsIgnoreCase("N/A"))
                .peek(e -> IO.println("Primeiro filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::rate).reversed())
                .peek(e -> IO.println("Ordenação " + e))
                .limit(10)
                .peek(e -> IO.println("Limite " + e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> IO.println("Mapeamento " + e))
                .forEach(IO::println); */

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episodio(t.season(), d)))
                .collect(Collectors.toList());

        episodios.forEach(IO::println);

         /*IO.print("Digite um trecho do título do episódio: ");
        String trechoTitulo = sc.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            IO.println("Episódio encontrado!");
            IO.println("Temporada: " + episodioBuscado.get().getSeason());
        } else {
            IO.println("Episódio não encontrado");
        } */

                /* IO.print("From which year do you want to see the episodes? ");
                 Integer year = sc.nextInt();

                 LocalDate searchDate = LocalDate.of(year, 1, 1);

                 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                 episodios.stream()
                         .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                         .forEach(e -> IO.println("Season: " + e.getSeason()
                                 + "Episode: " + e.getTitle()
                                 + "Release Date: " + e.getReleaseDate().format(dtf))); */

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getRate() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getSeason, Collectors.averagingDouble(Episodio::getRate)));
        IO.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getRate() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getRate));
        IO.println("Média: " + String.format("%.2f", est.getAverage()));
        IO.println("Melhor  episódio: " + est.getMax());
        IO.println("Pior episódio: " + est.getMin());
        IO.println("Quantidade de episódios: " + est.getCount());

        sc.close();
    }

}
