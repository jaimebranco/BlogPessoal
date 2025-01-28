package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
/*A anotação @SpringBootTest cria e inicializa o nosso ambiente de testes.*/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/*A anotação @TestInstance permite modificar o ciclo de vida da Classe de testes.
A instância de um teste possui dois tipos de ciclo de vida:
1) O LifeCycle.PER_METHOD: ciclo de vida padrão, onde para cada Método de teste é criada uma nova instância da Classe de teste. 
Quando utilizamos as anotações @BeforeEach e @AfterEach é necessário utilizar esta anotação.
2) O LifeCycle.PER_CLASS: uma única instância da Classe de teste é criada e reutilizada entre todos os Métodos de teste da Classe. 
Quando utilizamos as anotações @BeforeAll e @AfterAll é necessário utilizar esta anotação. */

public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	/*É um cliente para escrever testes criando um modelo de comunicação com as APIs HTTP. Ele fornece os mesmos Métodos, cabeçalhos 
	 * e outras construções do protocolo HTTP.*/
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
	/*A anotação @BeforeAll indica que o Método deve ser executado uma única vez antes de todos os Métodos da Classe, para criar
	 *  algumas pré-condições necessárias para todos os testes (criar objetos, por exemplo).*/
	void start(){

		usuarioRepository.deleteAll();

		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Root", "root@root.com", "rootroot", "-"));

	}

	@Test //A anotação @Test indica que o Método deve ser executado como um teste.
	@DisplayName("Cadastrar Um Usuário") //Personaliza o nome do teste permitindo inserir um Emoji (tecla Windows + . ) e texto.
	public void deveCriarUmUsuario() {

		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, 
				/*Representa uma solicitação HTTP ou uma entidade de resposta, composta pelo status da resposta (2XX, 4XX ou 5XX), o corpo
				 *  (Body) e os cabeçalhos (Headers).*/
			"Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "-"));

		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				/*Extensão de HttpEntity que adiciona um código de status (http Status)*/
			.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		/*AssertEquals(expected value, actual value) 	Afirma que dois valores são iguais.*/
	
	}

	@Test //A anotação @Test indica que o Método deve ser executado como um teste.
	@DisplayName("Não deve permitir duplicação do Usuário")//Personaliza o nome do teste permitindo inserir um Emoji (tecla Windows + . ) e texto.
	public void naoDeveDuplicarUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "-"));

		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "-"));

		ResponseEntity<Usuario> corpoResposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
		/*AssertEquals(expected value, actual value) 	Afirma que dois valores são iguais.*/
	}

	@Test// A anotação @Test indica que o Método deve ser executado como um teste.
	@DisplayName("Atualizar um Usuário")//Personaliza o nome do teste permitindo inserir um Emoji (tecla Windows + . ) e texto.
	public void deveAtualizarUmUsuario() {

		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "-"));

		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), 
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123" , "-");
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> corpoResposta = testRestTemplate
			.withBasicAuth("root@root.com", "rootroot")
			.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		/*AssertEquals(expected value, actual value) 	Afirma que dois valores são iguais.*/
		
	}

	@Test //A anotação @Test indica que o Método deve ser executado como um teste.
	@DisplayName("Listar todos os Usuários")//Personaliza o nome do teste permitindo inserir um Emoji (tecla Windows + . ) e texto.
	public void deveMostrarTodosUsuarios() {

		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "-"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "-"));

		ResponseEntity<String> resposta = testRestTemplate
		.withBasicAuth("root@root.com", "rootroot")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);

		
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		/*AassertEquals(expected value, actual value) 	Afirma que dois valores são iguais.*/

	}

}