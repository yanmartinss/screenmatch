package br.com.swgdigital.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rate;
    private LocalDate releaseDate;

    @ManyToOne
    private Serie serie;

    public Episodio() {
    }

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.season = numeroTemporada;
        this.title = dadosEpisodio.title();
        this.episodeNumber = dadosEpisodio.numberEpisode();
        try {
            this.rate = Double.valueOf(dadosEpisodio.rate());
        } catch (NumberFormatException e) {
            this.rate = 0.0;
        }
        try {
            this.releaseDate = LocalDate.parse(dadosEpisodio.dateRelease());
        } catch (DateTimeParseException e) {
            this.releaseDate = null;
        }
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title='" + title + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", rate=" + rate +
                ", releaseDate=" + releaseDate;
    }
}
