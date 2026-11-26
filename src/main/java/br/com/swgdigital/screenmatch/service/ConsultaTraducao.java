package br.com.swgdigital.screenmatch.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConsultaTraducao {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ENDPOINT = "https://api.mymemory.translated.net/get?langpair=en%7Cpt&q=";
    private static final int LIMITE_BLOCO = 450;

    public static String obterTraducao(String texto) {
        if (texto == null || texto.isBlank() || texto.equals("N/A")) {
            return texto;
        }

        List<String> traduzidos = new ArrayList<>();
        for (String bloco : dividirEmBlocos(texto)) {
            traduzidos.add(traduzirBloco(bloco));
        }
        return String.join(" ", traduzidos);
    }

    private static String traduzirBloco(String bloco) {
        try {
            URI uri = URI.create(ENDPOINT + URLEncoder.encode(bloco, StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro HTTP " + response.statusCode() + " na tradução");
            }

            JsonNode json = MAPPER.readTree(response.body());
            if (json.path("quotaFinished").asBoolean(false)) {
                throw new RuntimeException("Cota diária da API de tradução esgotada");
            }
            if (json.path("responseStatus").asInt(0) != 200) {
                throw new RuntimeException("Erro na tradução: " + json.path("responseDetails").asString(""));
            }
            return json.path("responseData").path("translatedText").asString();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Tradução interrompida", e);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao consultar a API de tradução", e);
        }
    }

    // A API aceita no máximo 500 bytes por consulta, então agrupa frases em blocos menores.
    private static List<String> dividirEmBlocos(String texto) {
        List<String> blocos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();

        for (String frase : texto.split("(?<=[.!?])\\s+")) {
            while (frase.length() > LIMITE_BLOCO) {
                if (!atual.isEmpty()) {
                    blocos.add(atual.toString());
                    atual.setLength(0);
                }
                blocos.add(frase.substring(0, LIMITE_BLOCO));
                frase = frase.substring(LIMITE_BLOCO);
            }
            if (atual.length() + frase.length() + 1 > LIMITE_BLOCO && !atual.isEmpty()) {
                blocos.add(atual.toString());
                atual.setLength(0);
            }
            if (!atual.isEmpty()) {
                atual.append(' ');
            }
            atual.append(frase);
        }
        if (!atual.isEmpty()) {
            blocos.add(atual.toString());
        }
        return blocos;
    }
}
