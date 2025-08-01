package com.alura.literalura.service;

import com.alura.literalura.dto.DadosLivro;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Livro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.traducao.ConsultaMyMemory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final ConsultaMyMemory consultaMyMemory;

    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository, ConsultaMyMemory consultaMyMemory) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.consultaMyMemory = consultaMyMemory;
    }

    @Transactional
    public void salvarLivro(DadosLivro dadosLivro) {
        Optional<Livro> livroExistente = livroRepository.findByTitulo(dadosLivro.titulo());
        if (livroExistente.isPresent()) {
            System.out.println("\nO livro '" + livroExistente.get().getTitulo() + "' já foi salvo anteriormente.");
            return;
        }

        if (dadosLivro.titulo() == null || dadosLivro.titulo().trim().isEmpty()) {
            System.out.println("O livro encontrado não possui título e não pode ser salvo.");
            return;
        }

        if (dadosLivro.autor() == null || dadosLivro.autor().isEmpty()) {
            System.out.println("O livro encontrado não possui autor e não pode ser salvo.");
            return;
        }


        var dadosAutor = dadosLivro.autor().get(0);
        List<Autor> autoresEncontrados = autorRepository.findByNome(dadosAutor.nome());
        Autor autor;
        if (autoresEncontrados.isEmpty()) {
            autor = new Autor(dadosAutor);
        } else {
            autor = autoresEncontrados.get(0);
        }

        Livro livro = new Livro(dadosLivro);
        livro.setAutor(autor);

        livroRepository.save(livro);

        if (livro.getIdioma().equalsIgnoreCase("en")) {
            System.out.println("Traduzindo título...");
            String traducao = consultaMyMemory.obterTraducao(livro.getTitulo());
            livro.setTituloTraduzido(traducao);
        }

        System.out.println("\nLivro salvo com sucesso :D");
        System.out.println(livro);
    }
}