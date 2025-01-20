package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long>{
	/*    A Classe Postagem, que é a Entidade que será mapeada em nosso Banco de dados (Lembre-se que a Classe Postagem foi quem gerou 
    a nossa tabela tb_postagens).    O Long representa a nossa Chave Primária (Primary Key), que é o Atributo que recebeu a 
    anotação @Id na nossa Classe Postagem (o Atributo também se chama id em nossa Classe Postagem).
    Estes 2 parâmetros são do tipo Java Generics (podem receber qualquer tipo de Objeto <T, T>). Dentro do contexto do JPA, estes
     2 parâmetros é o mínimo necessário para executar os Métodos padrão da Interface Repository, 	 Regras e convenções para os parâmetros 
     genéricos usados na interface JpaRepository. Vamos detalhar:
		    Tipo da Entidade:
		        O primeiro parâmetro genérico é o tipo da entidade que o repositório vai gerenciar. No seu caso, é a classe Postagem.
		        Este parâmetro deve ser uma classe que está anotada com @Entity e mapeada para uma tabela no banco de dados.

		    Tipo da Chave Primária:
		        O segundo parâmetro genérico é o tipo da chave primária da entidade. No seu caso, é Long.
		        Este tipo deve corresponder ao tipo do atributo que está anotado com @Id na classe da entidade. */
	public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
	/*este método busca todas as postagens (Postagem) cujo título (titulo) contém a string fornecida, ignorando a diferença entre maiúsculas
	 *  e minúsculas. O Spring Data JPA gera automaticamente a implementação deste método com base na convenção de nomenclatura.*/
		
}
