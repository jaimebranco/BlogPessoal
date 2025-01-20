package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController /*deﬁne que a Classe é do tipo RestController, que receberá requisições que serão compostas por:
    *URL: Endereço da requisição (endpoint)
    *Verbo: Define qual Método HTTP será acionado na Classe controladora.
    *Corpo da requisição (Request Body): Objeto que contém os dados que serão persistidos no Banco de dadas. Nem
    * toda a requisição enviará dados no Corpo da Requisição.*/

@RequestMapping("/postagens") /*é usada para mapear as solicitações para os Métodos da Classe controladora 
	*PostagemController, ou seja, define a URL (endereço) padrão do Recurso (/postagens).*/ 
@CrossOrigin(origins = "*", allowedHeaders = "*") /*indica que a Classe controladora permitirá o recebimento de 
	*requisições realizadas de fora do domínio (localhost e futuramente da nuvem quando o Deploy da aplicação for 
	*efetivado) ao qual ela pertence. Essa anotação é essencial para que o front-end ou aplicativo mobile, tenha acesso
 	*aos Métodos e Recursos da nossa aplicação (O termo técnico é consumir a API).*/
	/*Anotação libera também os Cabeçalhos das Requisições (parâmetro allowedHeaders), que em alguns casos trazem
	*informações essenciais para o correto funcionamento da aplicação.*/
public class PostagemController {
	
		@Autowired
		private PostagemRepository postagemRepository;
		
		@Autowired
		private TemaRepository temaRepository;
		
		
		@GetMapping //mapeia todas as Requisições HTTP GET, enviadas para um endereço específico, chamado endpoint, dentro do Recurso Postagem
		public ResponseEntity<List<Postagem>> getAll(){ 
			/* O método ResponseEntity é uma classe do Spring Framework que representa toda a 
		resposta HTTP, incluindo o status, os cabeçalhos e o corpo da resposta. Ele é muito útil para controlar de forma detalhada o que 
		será retornado ao cliente em uma aplicação web.*/
			/* public ResponseEntity<List<Postagem>> getAll(): Este é o método que será executado quando uma requisição GET for enviada para o
			 *  endpoint mapeado.
		     * ResponseEntity<List<Postagem>>: O método retorna um ResponseEntity contendo uma lista de objetos Postagem. Isso permite controlar
		     *  a resposta HTTP de forma detalhada.
		     * getAll(): Nome do método, que indica que ele retorna todas as postagens.*/

			return ResponseEntity.ok(postagemRepository.findAll());

		    /* postagemRepository.findAll(): Este método do repositório (postagemRepository) é chamado para buscar todas as postagens no banco
		     * de dados. Ele retorna uma lista de objetos Postagem.		     
		     * ResponseEntity.ok(...): Cria uma resposta HTTP com status 200 OK e inclui a lista de postagens no corpo da resposta.*/

		}
		@GetMapping("/{id}") /*Esta anotação indica que o método getById será chamado quando uma requisição HTTP GET for feita para a URL que 
		contém um parâmetro {id}. Por exemplo, se a URL for /postagens/1, o valor 1 será passado como parâmetro id.*/
		public ResponseEntity<Postagem> getById(@PathVariable Long id) { /*Método getById: Este método recebe um parâmetro id do tipo Long, que
		 é extraído da URL graças à anotação @PathVariable.*/
			return postagemRepository.findById(id) //Este método retorna um Optional<Postagem>, que pode ou não conter uma postagem.
					.map(resposta -> ResponseEntity.ok(resposta)) /*Se uma postagem for encontrada, ela é mapeada para uma resposta HTTP 200 OK 
					contendo a postagem.*/
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); /*Se nenhuma postagem for encontrada, retorna uma resposta HTTP
					 404 NOT FOUND.*/
		}
		@GetMapping("/titulo/{titulo}")
		public ResponseEntity<List<Postagem>> GetByTitulo(@PathVariable String titulo){
			return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
			/*ResponseEntity.ok(postagens): Se a busca for bem-sucedida, o método retorna uma resposta HTTP 200 OK contendo a lista de postagens 
			 * encontradas.*/
		}
		@PostMapping //Esta anotação indica que o método post será chamado quando uma requisição HTTP POST for feita para o endpoint correspondente
		public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
			if(temaRepository.existsById(postagem.getTema().getId()))
				return ResponseEntity.status(HttpStatus.CREATED)
			/*return ResponseEntity.status(HttpStatus.CREATED)
					/*O método recebe um parâmetro postagem do tipo Postagem, que é extraído do corpo da requisição HTTP graças à anotação 
					 * @RequestBody. A anotação @Valid é usada para validar o objeto postagem com base nas anotações de validação presentes na 
					 * classe Postagem (como @NotBlank e @Size).*/

					.body(postagemRepository.save(postagem));
			/*postagemRepository.save(postagem): Este método salva a nova postagem no banco de dados. O repositório postagemRepository é
			 *  responsável por realizar a operação de salvamento.*/
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
		}
		@PutMapping //indica que o Método put(Postagem postagem), responderá a todas as requisições do tipo HTTP PUT,
		public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
			/* O método recebe um parâmetro postagem do tipo Postagem, que é extraído do corpo da requisição HTTP graças à anotação @RequestBody.
		    A anotação @Valid é usada para validar o objeto postagem com base nas anotações de validação presentes na classe Postagem.
			return postagemRepository.findById(postagem.getId())
					/*postagemRepository.findById(postagem.getId()): Este método tenta encontrar uma postagem no repositório usando o ID 
					 * da postagem fornecida. Ele retorna um Optional<Postagem>.
					.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem))) Se uma postagem for 
					encontrada, o método mapeia a resposta para um status HTTP 200 Ok
						.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
						//Se nenhuma postagem for encontrada, retorna uma resposta HTTP 404 NOT FOUND.*/
			if (postagemRepository.existsById(postagem.getId())) {
				
				if(temaRepository.existsById(postagem.getTema().getId()))
					return ResponseEntity.status(HttpStatus.OK)
							.body(postagemRepository.save(postagem));
				
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe", null);
				
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		@ResponseStatus(HttpStatus.NO_CONTENT)/* indica que o Método delete(Long id), terá um Status HTTP específico quando a Requisição for 
		bem sucedida, ou seja, será retornado o HTTP Status NO_CONTENT 🡪 204, ao invés do HTTP Status OK 🡪 200 como resposta padrão do Método.*/
		@DeleteMapping("/{id}")/*mapeia todas as Requisições HTTP DELETE, enviadas para um endereço específico (Endpoint), dentro do Recurso 
		Postagem, para um Método específico que responderá as requisições, ou seja, ele indica que o Método delete( Long id ), responderá a todas 
		as requisições do tipo HTTP DELETE, enviadas no endereço http://localhost:8080/postagens/id, onde id é uma Variável de Caminho 
		(Path Variable), que receberá o id da Postagem que será Deletada.*/
		public void delete(@PathVariable Long id) { /*O método recebe um parâmetro id do tipo Long, que é extraído da URL graças à anotação
		 @PathVariable.*/
			Optional<Postagem> postagem = postagemRepository.findById(id); /*Optional<Postagem> postagem = postagemRepository.findById(id): 
			O método tenta encontrar uma postagem no repositório usando o ID fornecido. Ele retorna um Optional<Postagem>.*/
			
			if(postagem.isEmpty()) /*if(postagem.isEmpty()): Se a postagem não for encontrada (ou seja, o Optional está vazio), o método lança
			 uma exceção ResponseStatusException com o status HTTP 404 NOT FOUND.*/
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			/*throw new ResponseStatusException(HttpStatus.NOT_FOUND): Esta linha lança uma exceção que resulta em uma resposta HTTP 404
			 *  NOT FOUND se a postagem não for encontrada.*/
			postagemRepository.deleteById(id);
			//Esta linha de código deleta a postagem do banco de dados usando o ID fornecido.
		}
		

}
