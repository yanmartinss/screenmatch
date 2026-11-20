package br.com.swgdigital.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {

    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rate;
    private LocalDate releaseDate;

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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
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
