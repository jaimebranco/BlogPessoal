package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity // indica que esta Classe define uma entidade, ou seja, ela será utilizada para
		// gerar uma tabela no Banco de dados da aplicação.
@Table(name = "tb_postagens") /*
								 * indica o nome da Tabela no Banco de dados. Caso esta anotação não seja
								 * declarada, o Banco de dados criará a tabela com o mesmo nome da Classe Model
								 * (Postagem).
								 */
public class Postagem {

	@Id // inidica que o Atributo anotado será a Chave Primária (Primary Key - PK) da Tabela tb_postagens.
		@GeneratedValue(strategy = GenerationType.IDENTITY)/* @GeneratedValue indica que a Chave Primária será gerada pelo Banco de dados.
		// O parâmetro strategy indica de que forma esta Chave Primária será gerada. Estratégia GenerationType.IDENTITY indica que a 
		 * Chave Primária será gerada pelo Banco de dados através da opção auto-incremento (auto-increment) do SQL, que gera uma sequência 
		 * numérica iniciando em 1.*/
		private Long id;
	
		@NotBlank(message = " O atributo título texto é obrigatório!") /*@NotBlank não permite que o Atributo seja Nulo ou contenha apenas espaços em 
		branco. Você pode configurar uma mensagem para o usuário através do Atributo message*/
		@Size(min = 5, max = 100, message = "O atributo título deve conter no mínimo 5 e no máximo 100 caracteres") /* A anotação @Size define o valor
		Mínimo (min) e o valor Máximo (max) de caracteres do Atributo. Você pode configurar uma mensagem para o usuário através do Atributo message.*/
		private String titulo;
	
		@NotBlank(message = " O atributo texto é obrigatório!") 
		@Size(min = 10, max = 1000, message = "O atributo texto deve conter no mínimo 10 e no máximo 1000 caracteres") 
		private String texto;
	
		@UpdateTimestamp /*configura o Atributo data como Timestamp, ou seja, o Spring se encarregará de obter a data e a hora do Sistema Operacional
		e inserir no Atributo data toda vez que um Objeto da Classe Postagem for criado ou atualizado.*/
		private LocalDateTime data;
	
	@ManyToOne /*indica que a Classe Postagem será o lado N:1 e terá um Objeto da Classe Tema, que no modelo Relacional será a Chave Estrangeira
	 na Tabela tb_postagens (tema_id).*/ 
		@JsonIgnoreProperties("postagem") //esta anotação é usada para evitar problemas de serialização e desserialização JSON. 
		private Tema tema; /* Este é o campo que representa a relação muitos-para-um com a entidade Tema. Cada Postagem terá um objeto 
		Tema associado a ela.*/
	
	
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

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	
	
}
