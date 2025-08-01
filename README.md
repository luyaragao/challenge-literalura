# Literalura 📖

![Status](https://img.shields.io/badge/status-concluído-green.svg)
![Java](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql&logoColor=white)


Literalura é uma aplicação de catálogo de livros via console desenvolvida em Java com Spring Boot. O projeto consome a API pública [Gutendex](https://gutendex.com/) para buscar livros e autores, permitindo que o usuário salve e gerencie suas leituras em um banco de dados local.

## 📚 Funcionalidades

- **Busca de Livros:** Pesquise livros por título diretamente da API Gutendex.

- **Busca por Autor:** Encontre todos os livros de um determinado autor.

- **Persistência de Dados:** Salve seus livros e autores favoritos em um banco de dados PostgreSQL.

- **Listagem:**
    - Liste todos os livros registrados.
    - Liste todos os autores registrados.
    - Liste autores que estavam vivos em um determinado ano.
    - Liste livros por idioma (inglês, português, espanhol, francês).
- **Ranking:** Exiba um Top 10 dos autores com mais livros baixados.
- **Tradução (Bônus):** Tradução automática de títulos em inglês para português utilizando uma API externa.

## 🛠️ Construído com as seguintes tecnologias:

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA:** Para persistência de dados.
- **PostgreSQL:** Como banco de dados relacional.
- **Maven:** Para gerenciamento de dependências.
- **Jackson:** Para desserialização de JSON.

## 🔧 Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou superior.
- [Maven](https://maven.apache.org/download.cgi).
- Um cliente [PostgreSQL](https://www.postgresql.org/download/) rodando na sua máquina.

## ⚙️ Instalação e Execução

1. **Clone o repositório:**

```bash
git clone https://github.com/luyaragao/challenge-literalura.git
cd literalura
```

2. **Configure a conexão com o banco:**  
   O projeto está configurado para ler as credenciais do banco a partir de variáveis de ambiente do seu sistema. Configure as variáveis com os seguintes nomes:

- `DB_HOST`: O endereço do seu banco (ex: `localhost`)
- `DB_NAME`: O nome do banco de dados (ex: `literalura`)
- `DB_USER`: Seu usuário do PostgreSQL
- `DB_PASSWORD`: Sua senha do PostgreSQL

*Alternativamente, você pode editar o arquivo `src/main/resources/application.properties` e colocar suas credenciais diretamente caso não queira criar as variáveis de ambiente ✨*

3. **Execute a aplicação:**  
   Use o terminal na raiz do projeto e rode o seguinte comando Maven:

```bash
mvn spring-boot:run
```

## 👩‍💻 Como Usar? 

Após a aplicação iniciar, o menu interativo será exibido no seu terminal. Basta digitar o número da opção desejada e pressionar Enter para explorar, adicionar e salvar uma vasta gama de livros e autores! :D

---

[![LinkedIn](https://img.shields.io/badge/LinkedIn-blue?logo=linkedin)](https://www.linkedin.com/in/luy-aragao/)
[![GitHub](https://img.shields.io/badge/GitHub-black?logo=github)](https://github.com/luyaragao)


- Este projeto foi desenvolvido com propósito educacional, um challenge proposto pela alura na formação de desenvolvimento back end java.
