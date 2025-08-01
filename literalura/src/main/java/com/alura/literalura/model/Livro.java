package com.alura.literalura.model;

import com.alura.literalura.dto.DadosLivro;
import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Autor autor;

    private String idioma;
    private Integer numeroDeDownloads;

    @Transient
    private String tituloTraduzido;

    public Livro() {}

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        if (dadosLivro.idiomas() != null && !dadosLivro.idiomas().isEmpty()) {
            this.idioma = dadosLivro.idiomas().get(0);
        } else {
            this.idioma = "Desconhecido";
        }
        if (dadosLivro.numeroDeDownloads() != null) {
            this.numeroDeDownloads = dadosLivro.numeroDeDownloads().intValue();
        }
    }

    public String getTituloTraduzido() {
        return tituloTraduzido;
    }

    public void setTituloTraduzido(String tituloTraduzido) {
        this.tituloTraduzido = tituloTraduzido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDeDownloads() {
        return numeroDeDownloads;
    }

    public void setNumeroDeDownloads(Integer numeroDeDownloads) {
        this.numeroDeDownloads = numeroDeDownloads;
    }


    @Override
    public String toString() {
        String nomeAutor = (autor != null) ? autor.getNome() : "Autor desconhecido";
        String tituloExibido = titulo;
        if (tituloTraduzido != null && !tituloTraduzido.isEmpty()) {
            tituloExibido += " (Tradução: " + tituloTraduzido + ")";
        }

        return """
                ---------- LIVRO ----------
                Título: %s
                Autor: %s
                Idioma: %s
                Downloads: %d
                ---------------------------
                """.formatted(tituloExibido, nomeAutor, idioma, numeroDeDownloads);
    }
}