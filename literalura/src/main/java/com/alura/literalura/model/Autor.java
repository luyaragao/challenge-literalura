package com.alura.literalura.model;

import com.alura.literalura.dto.DadosAutor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer anoDeNascimento;
    private Integer anoDeFalecimento;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Livro> livros = new ArrayList<>();

    public Autor(DadosAutor dadosAutor) {
        this.nome = dadosAutor.nome();
        if (dadosAutor.anoDeNascimento() != null) {
            this.anoDeNascimento = Integer.valueOf(dadosAutor.anoDeNascimento());
        }

        if(dadosAutor.anoDeFalecimento() != null) {
            this.anoDeFalecimento = Integer.valueOf(dadosAutor.anoDeFalecimento());
        }
    }

    public Autor () {}

    public String getNome() {
        return nome;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        String titulosDosLivros = livros.stream()
                .map(Livro::getTitulo)
                .collect(Collectors.joining(", "));

        String anoNascimentoStr = (anoDeNascimento != null) ? String.valueOf(anoDeNascimento) : "Desconhecido";
        String anoFalecimentoStr = (anoDeFalecimento != null) ? String.valueOf(anoDeFalecimento) : "N/A";

        return """
                ----- AUTOR -----
                Nome: %s
                Ano de Nascimento: %s
                Ano de Falecimento: %s
                Livros: [%s]
                -----------------
                 """.formatted(nome, anoNascimentoStr, anoFalecimentoStr, titulosDosLivros);    }
}


