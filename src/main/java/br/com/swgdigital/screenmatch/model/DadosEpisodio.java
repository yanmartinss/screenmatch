package br.com.swgdigital.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String title, @JsonAlias("Episode") Integer numberEpisode,
                            @JsonAlias("imdbRating") String rate, @JsonAlias("Released") String dateRelease) {
}
