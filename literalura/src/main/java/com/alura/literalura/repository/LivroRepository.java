package com.alura.literalura.repository;

import com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByIdioma(String idioma);

    Optional<Livro> findByTitulo(String titulo);
}
