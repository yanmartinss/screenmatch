package br.com.swgdigital.screenmatch.model;

import br.com.swgdigital.screenmatch.service.ConsultaTraducao;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private Integer totalSeasons;
    private Double rate;
    private String actors;
    private String poster;

    @Enumerated(EnumType.STRING)
    private Category genre;

    private String sinopse;

    @Transient
    private List<Episodio> episodios = new ArrayList<Episodio>();

    public Serie(DadosSerie dadosSerie) {
        this.title = dadosSerie.title();
        this.totalSeasons = dadosSerie.totalSeasons();
        this.rate = OptionalDouble.of(Double.valueOf(dadosSerie.rate())).orElse(0.0);
        this.genre = Category.fromString(dadosSerie.genre().split(",")[0].trim());
        this.actors = dadosSerie.actors();
        this.poster = dadosSerie.poster();
        String sinopseTraduzida;
        try {
            sinopseTraduzida = ConsultaTraducao.obterTraducao(dadosSerie.sinopse()).trim();
        } catch (RuntimeException e) {
            sinopseTraduzida = dadosSerie.sinopse();
        }
        this.sinopse = sinopseTraduzida;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return  "genre=" + genre +
                ", title='" + title + '\'' +
                ", totalSeasons=" + totalSeasons +
                ", rate=" + rate +
                ", actors='" + actors + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'';
    }
}
