package br.com.swgdigital.screenmatch.application;

import br.com.swgdigital.screenmatch.model.DadosSerie;
import br.com.swgdigital.screenmatch.model.DadosTemporada;
import br.com.swgdigital.screenmatch.model.Serie;
import br.com.swgdigital.screenmatch.repository.SerieRepository;
import br.com.swgdigital.screenmatch.service.ConsumoApi;
import br.com.swgdigital.screenmatch.service.ConverteDados;

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
    private List<DadosSerie>  dadosSerie = new ArrayList<>();

    private SerieRepository serieRepository;

    public Program(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void showMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                
                0 - Sair                                 
                """;

            IO.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 0:
                    IO.println("Saindo...");
                    break;
                default:
                    IO.println("Opção inválida");
            }
        }

    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSerie.add(dados);
        serieRepository.save(serie);
        IO.println(dados);
    }

    private DadosSerie getDadosSerie() {
        IO.println("Digite o nome da série para busca");
        var nomeSerie = sc.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalSeasons(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.title().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(IO::println);
    }

    private void listarSeriesBuscadas() {
        List<Serie> series =  new ArrayList<>();

        series = dadosSerie.stream()
                        .map(d -> new Serie(d))
                                .toList();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(IO::println);
    }

}
