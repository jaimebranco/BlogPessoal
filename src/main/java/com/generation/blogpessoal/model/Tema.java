package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_temas")
public class Tema {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O Atributo Descrição é obrigatório")
	private String descricao;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "tema", cascade = CascadeType.REMOVE)
	/*fetch: A propriedade fetch define a estratégia de busca e carregamento dos dados das entidades relacionadas durante uma busca. 
	 * Ao trabalhar com um ORM como o Hibernate, a busca e carregamento de dados pode ser classificada em dois tipos: Eager (ansiosa) 
	 * e Lazy (preguiçosa). FetchType.LAZY: No projeto Blog Pessoal utilizaremos o tipo LAZY (preguiçosa), ou seja, ao carregarmos os 
	 * dados de uma Postagem, ele não carregará os dados do Tema associado a cada Postagem até que os dados sejam solicitados.*/
	@JsonIgnoreProperties("tema")
	private List<Postagem> postagem;
	/*mappedBy = "tema": Esta propriedade indica que a relação é bidirecional e que o lado inverso da relação é gerenciado pela
	 *  propriedade tema na entidade Postagem. Isso significa que a chave estrangeira que mapeia a relação está na tabela tb_postagens.
	*cascade = CascadeType.REMOVE: A propriedade cascade define as operações em cascata que devem ser aplicadas às entidades relacionadas. 
	*CascadeType.REMOVE significa que, quando um Tema for removido, todas as Postagem associadas a ele também serão removidas automaticamente.
	*@JsonIgnoreProperties("tema"): Esta anotação é usada para evitar problemas de serialização e desserialização JSON. Ela diz ao Jackson
	* para ignorar a propriedade tema ao serializar um objeto Postagem, evitando loops infinitos.private List postagem: Este é o campo que 
	* representa a lista de postagens associadas a um tema. Cada Tema pode ter várias Postagem associadas a ele.*/
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

	
}
