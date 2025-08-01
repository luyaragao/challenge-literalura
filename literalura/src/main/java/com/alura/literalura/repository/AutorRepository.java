package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository  extends JpaRepository<Autor, Long> {
    List<Autor> findByNome(String nome);

    @Query("SELECT a FROM Autor a WHERE a.anoDeNascimento <= :ano AND (a.anoDeFalecimento IS NULL OR a.anoDeFalecimento >= :ano)")
    List<Autor> findAutoresVivosEmAno(Integer ano);

    @Query("SELECT a FROM Autor a JOIN a.livros l GROUP BY a ORDER BY SUM(l.numeroDeDownloads) DESC")
    List<Autor> findTop10AutoresMaisBaixados(Pageable pageable);

}
