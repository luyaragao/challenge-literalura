package com.alura.literalura.service.traducao;

import com.alura.literalura.service.ConsumoAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ConsultaMyMemory {
    private final ObjectMapper mapper;

    private final ConsumoAPI consumoAPI;

    public ConsultaMyMemory(ObjectMapper mapper, ConsumoAPI consumoAPI) {
        this.mapper = mapper;
        this.consumoAPI = consumoAPI;
    }

    public String obterTraducao(String text) {
        try {
            String textoCodificado = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String langpair = "en%7Cpt-br";

            String url = "https://api.mymemory.translated.net/get?q=" + textoCodificado + "&langpair=" + langpair;

            String json = consumoAPI.obterDados(url);

            if (json == null || json.trim().isEmpty()) {
                System.err.println("A API de tradução retornou uma resposta vazia.");
                return text;
            }

            DadosTraducao traducao = mapper.readValue(json, DadosTraducao.class);

            if (traducao.dadosResposta() != null && traducao.dadosResposta().textoTraduzido() != null) {
                return traducao.dadosResposta().textoTraduzido();
            } else {
                System.err.println("Não foi possível extrair o texto traduzido da resposta da API.");
                return text;
            }

        } catch (JsonProcessingException e) {
            System.err.println("Erro ao processar o JSON da tradução: " + e.getMessage());
            return text;
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado ao tentar traduzir: " + e.getMessage());
            return text;
        }
    }
}