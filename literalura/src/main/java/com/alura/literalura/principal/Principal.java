package com.alura.literalura.principal;

import com.alura.literalura.dto.DadosLivro;
import com.alura.literalura.dto.ResultadoBuscaDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Livro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.IConverteDados;
import com.alura.literalura.service.LivroService;
import com.alura.literalura.service.traducao.ConsultaMyMemory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Principal {
    private final Scanner leitura = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final ConsumoAPI consumoAPI;
    private final IConverteDados conversor;
    private final ConsultaMyMemory consultaMyMemory;
    private final LivroService livroService;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository, ConsumoAPI consumoAPI, IConverteDados conversor, ConsultaMyMemory consultaMyMemory, LivroService livroService) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.consumoAPI = consumoAPI;
        this.conversor = conversor;
        this.consultaMyMemory = consultaMyMemory;
        this.livroService = livroService;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    
                    Escolha o número de sua opção:
                    ☆-----------------------------------☆
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Buscar livros por autor
                    7 - Top 10 autores mais baixados
                    
                    0 - Sair
                    ☆-----------------------------------☆
                    """;
            System.out.println(menu);
            try {
                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao) {
                    case 1:
                        buscarLivroPeloTitulo();
                        break;
                    case 2:
                        listaLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 6:
                        buscarLivrosPorAutor();
                        break;
                    case 7:
                        top10MaisBaixados();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        System.out.println("Obrigado por utilizar <3");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                leitura.nextLine();
            }
        }
    }

    private void top10MaisBaixados() {
        System.out.println("☆---- Top 10 Autores Mais Baixados -----☆");
        Pageable topTen = PageRequest.of(0, 10);
        List<Autor> topAutores = autorRepository.findTop10AutoresMaisBaixados(topTen);

        if (topAutores.isEmpty()) {
            System.out.println("Não há dados de downloads suficientes para gerar um top 10.");
        } else {
            topAutores.forEach(System.out::println);
        }
        System.out.println("☆----------------------------------------☆");
    }

    private void buscarLivrosPorAutor() {
        System.out.println("Insira o nome do autor que deseja procurar: ");
        var nomeAutor = leitura.nextLine();

        String json = consumoAPI.obterDados(URL_BASE + "?search=" + nomeAutor.replace(" ", "+"));
        ResultadoBuscaDTO resultadoBusca = conversor.obterDados(json, ResultadoBuscaDTO.class);

        if (resultadoBusca == null || resultadoBusca.resultados().isEmpty()) {
            System.out.println("Nenhum livro encontrado para este autor na API.");
            return;
        }

        DadosLivro dadosLivro = escolherLivroDaApi(resultadoBusca);
        if (dadosLivro != null) {
            livroService.salvarLivro(dadosLivro);
        } else {
            System.out.println("Busca cancelada ou nenhum livro selecionado.");
        }
    }

    private void buscarLivroPeloTitulo() {
        System.out.println("Insira o nome do livro que deseja procurar: ");
        var tituloLivro = leitura.nextLine();
        Optional<Livro> livroJaSalvo = livroRepository.findByTituloContainingIgnoreCase(tituloLivro);

        if (livroJaSalvo.isPresent()) {
            System.out.println("\nEste livro já está cadastrado no banco de dados:");
            System.out.println(livroJaSalvo.get());
            return;
        }

        String json = consumoAPI.obterDados(URL_BASE + "?search=" + tituloLivro.replace(" ", "+"));
        ResultadoBuscaDTO resultadoBusca = conversor.obterDados(json, ResultadoBuscaDTO.class);

        if (resultadoBusca == null || resultadoBusca.resultados().isEmpty()) {
            System.out.println("Nenhum livro encontrado com esse título na API.");
            return;
        }

        DadosLivro dadosLivro = escolherLivroDaApi(resultadoBusca);
        if (dadosLivro != null) {
            livroService.salvarLivro(dadosLivro);
        } else {
            System.out.println("Busca cancelada ou nenhum livro selecionado.");
        }
    }

    private DadosLivro escolherLivroDaApi(ResultadoBuscaDTO resultadoBusca) {
        System.out.println("\nForam encontrados estes livros. Qual deles você deseja salvar?");
        List<DadosLivro> livros = resultadoBusca.resultados();

        for (int i = 0; i < Math.min(livros.size(), 10); i++) {
            DadosLivro livro = livros.get(i);
            String nomeAutor = (livro.autor() != null && !livro.autor().isEmpty())
                    ? livro.autor().get(0).nome()
                    : "Autor Desconhecido";
            System.out.printf("%d - Título: %s (Autor: %s)\n", i + 1, livro.titulo(), nomeAutor);
        }
        System.out.println("0 - Cancelar");

        try {
            System.out.print("Sua escolha: ");
            var escolha = leitura.nextInt();
            leitura.nextLine();

            if (escolha > 0 && escolha <= Math.min(livros.size(), 10)) {
                return livros.get(escolha - 1);
            }
        } catch (InputMismatchException e) {
            leitura.nextLine();
        }
        return null;
    }

    private void listaLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("☆---- Livros Registrados -----☆");
            livros.stream()
                    .sorted(Comparator.comparing(Livro::getTitulo))
                    .forEach(System.out::println);
            System.out.println("☆------------------------------☆");
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            System.out.println("☆----- Autores Registrados -----☆");
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNome))
                    .forEach(System.out::println);
            System.out.println("☆-------------------- ----------☆");
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Insira o ano para pesquisar os autores vivos:");
        try {
            var ano = leitura.nextInt();
            leitura.nextLine();

            List<Autor> autoresVivos = autorRepository.findAutoresVivosEmAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado para o ano de " + ano + ".");
            } else {
                System.out.println("\n----- Autores vivos no ano de " + ano + " -----");
                autoresVivos.stream()
                        .sorted(Comparator.comparing(Autor::getNome))
                        .forEach(System.out::println);
                System.out.println("---------------------------------------");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um ano válido.");
            leitura.nextLine();
        }
    }

    private void listarLivrosPorIdioma() {
        var menuIdiomas = """
                Insira o idioma para a busca:
                es - espanhol
                en - inglês
                fr - francês
                pt - português
                """;
        System.out.println(menuIdiomas);
        var idioma = leitura.nextLine().trim().toLowerCase();

        List<Livro> livrosPorIdioma = livroRepository.findByIdioma(idioma);

        if (livrosPorIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma '" + idioma + "' no banco de dados.");
        } else {
            System.out.printf("%n----- Livros no idioma: %s -----%n", idioma);
            livrosPorIdioma.forEach(System.out::println);
            System.out.println("---------------------------------");
        }
    }
}