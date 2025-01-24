package com.generation.blogpessoal.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component /* @Component, o que indica que esta Classe é uma Classe de Componente. Classe de Componente é uma Classe gerenciada 
pelo Spring, que permite Injetar e Instanciar qualquer Dependência especificada na implementação da Classe, em qualquer outra 
Classe, sempre que necessário.*/
public class JwtService {
	public static final String SECRET	= "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	/*O atributo SECRET. Este atributo armazenará a Chave de assinatura do Token JWT (secret). Este Atributo foi definido com o 
	 * modificador final, porque este valor será constante, ou seja, nunca será modificado. Foi definido também o modificador static,
	 *  porque o atributo deve estar associado apenas e exclusivamente a esta Classe, ou seja, é uma variável de Classe e não do 
	 *  Objeto*/
	/*O valor atribuído ao atributo SECRET é uma chave encriptada aleatória, gerada através do algoritmo de Criptografia AES.
    O Advanced Encryption Standard (AES) é uma especificação para a criptografia de dados eletrônicos estabelecida pelo Instituto 
    Nacional de Padrões e Tecnologia (NIST) dos EUA em 2001. AES é amplamente utilizado hoje em dia, pois é um algoritmo que geram
     chaves muito fortes, difíceis de serem quebradas.
     Para gerar esta chave, utilizamos o site Key Generator (https://generate-random.org/encryption-key-generator),*/
	
	private Key getSignKey() {
		/*getSignKey(), retornará um Objeto da Interface Key, que é responsável por definir as 3 características compartilhadas 
		 * por todos os objetos do tipo key (Chave de assinatura): Algoritmo, Codificação e Formato.*/
		byte [] keyBytes = Decoders.BASE64.decode(SECRET);
		/*Linha 24: Foi criado um vetor (array), chamado keyBytes, tipo Byte para receber o resultado da codificação em Base 64.
		 *  O vetor (array) keyBytes foi definido com o tipo Byte, porque durante o processo de codificação é necessário trabalhar 
		 *  diretamente com os bits (0 e 1) da String. Para codificar a SECRET, foi utilizado o Método decode(), da Classe Decoders.*/
		return Keys.hmacShaKeyFor(keyBytes);
		/*Base64 é um método para codificação de dados para transferência de conteúdo na Internet. É utilizado frequentemente para
		transmitir dados binários por meios de transmissão que lidam apenas com texto, como por exemplo para enviar, arquivos 
		anexos por e-mail.
    	HMAC SHA256 (HS256) é um tipo de algoritmo de hash, com chave, que é construído a partir da função hash SHA-256 e usado 
    	como um código de autenticação de mensagem baseado em hash (HMAC).
    	HS256 é um método de assinatura simétrica. Isso significa que a mesma chave secreta é usada para criar e verificar a 
    	própria assinatura. O emissor anexa o cabeçalho JWT e a carga com a chave secreta e faz o hash do resultado usando SHA256,
    	 criando uma assinatura. O destinatário usa suas cópias da chave secreta, cabeçalho JWT e carga útil da mesma maneira
    	  para reproduzir a assinatura, verificando se eles correspondem.*/
	}
	
	private Claims extractAllClaims(String token) {
		/*O Método extractAllClaims(String token) retorna todas as claims, inseridas no Payload do Token JWT.
		 * Claims são declarações inseridas no payload do Token JWT, ou seja, são informações declaradas sobre um assunto.
		 * "sub": "admin@email.com.br"
		 * sub é uma claim, que contém o e-mail do usuário.*/
		return Jwts.parserBuilder()
				/*Através do Método parserBuilder(), da Classe Jwts, será criada uma nova instância da Interface JWT. */
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
		/*setSigningKey(getSignKey()).build(), da Interface JwtParserBuilder, verifica se a assinatura do Token JWT é válida. 
		 * Caso seja válida, o Método parseClaimsJws(token).getBody(), da Interface JwtParser, extrai todas as claims do corpo 
		 * do Token e retorna todas as claims encontradas, através do comando return.*/
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		/*O Método extractClaim(String token, Function< Claims, T > claimsResolver) retorna uma claim específica, inserida no
		Payload do Token JWT.*/
		final Claims claims = extractAllClaims(token);
		/*Cria um Objeto final, da Interface Claims, que receberá a execução do Método extractAllClaims(String token), que retornará 
		 * todas as claims que forem encontradas no corpo do token enviado no parâmetro do Método.*/
		return claimsResolver.apply(claims);
		/* O Método get enviado no parâmetro da Interface Funcional Function< Claims, T > claimsResolver, será executado, através 
		 * do Método claimsResolver.apply(claims), da Interface Funcional Function. Como parâmetro do Método apply(), será utilizado
		 *  o Objeto claims, que contém todas as claims obtidas através do Método extractAllClaims(String token).*/
	}
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	public Date extractExpiration(String token) {
		/*O Método extractExpiration(String token) recupera os dados da Claim exp, onde se encontra a data e o horário de expiração 
		 * do Token JWT, através do Método extractClaim(String token, Function< Claims, T > claimsResolver).*/
		return extractClaim(token, Claims::getExpiration);
		/*A Interface Funcional Function recebeu como entrada a Classe Claims e na saída, receberá a execução do Método getExpiration(),
		que está sendo chamado através do operador de referência de métodos (::), que retorna o valor da claim exp.*/
	}
	private Boolean isTokenExpired(String token) {
		/*O Método isTokenExpired(String token) recupera os dados da Claim exp, onde se encontra a data e o horário de expiração do Token
		 *  JWT, através do Método extractExpiration(String token) */
		return extractExpiration(token).before(new Date());
		/*erifica através do Método before(), da Classe Date, se o token está ou não expirado (fora da data e hora de validade). Se a data
		 *  e a hora do token for anterior a data e hora atual, o Token JWT estará expirado, o Método retornará true e será necessário
		*autenticar novamente para gerar um novo Token JWT válido.*/
	}
	public Boolean validateToken(String token, UserDetails userDetails) {
		/*O Método validateToken(String token, UserDetails userDetails) valida se o Token JWT pertence ao usuário que enviou o token através
		 *  do Cabeçalho de uma requisição HTTP, na propriedade Authorization. */
		final String username = extractUsername(token);
		/*Cria um Objeto final, do tipo String, chamado username, que receberá a execução do Método extractUsername(String token), que
		 *  retornará a claim sub (subject), que contém o usuario autenticado (e-mail), que foi inserido no corpo do token, que foi enviado
		 *   no parâmetro do Método.*/
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		/* O Método retornará true se o usuario que foi extraído do token (claim sub), for igual ao usuario autenticado (atributo username 
		 * do Objeto da Classe UserDetails) e o token não estiver expirado (!isTokenExpired(token)). Para checar o usuario, foi utilizado 
		 * método equals() da Classe String. Para checar se o token expirou, foi utilizado o Método isTokenExpired(String token).*/
	}
	private String createToken(Map<String, Object>claims, String userName) {
		/*O Método createToken(Map<String, Object> claims, String userName) cria o Token JWT. O Método recebe 2 parâmetros: uma 
		 * Collection Map, chamada claims, que será utilizada para receber Claims personalizadas e um Objeto da Classe String, chamado 
		 * userName, contendo o usuário autenticado (e-mail).*/
		return Jwts.builder()
				/*O Método builder(), da Classe Jwts é responsável por criar o Token, a partir dos Métodos inseridos logo abaixo, que 
				 * contém os detalhes da construção do Token JWT.*/
				.setClaims(claims)
				/* O Método .setClaims(claims), da Classe Jwts é responsável por inserir as claims personalizadas no Payload do Token JWT.*/
				.setSubject(userName)
				/*O Método .setSubject(userName), da Classe Jwts é responsável por inserir a claim sub (subject), preenchida com o usuario 
				 * (e-mail), no Payload do Token JWT.*/
				.setIssuedAt(new Date(System.currentTimeMillis()))
				/* Método .setIssuedAt(new Date(System.currentTimeMillis())), da Classe Jwts é responsável por inserir a claim iat
				 *  (issued at - data e hora da criação), preenchida com a data e a hora (incluindo os milissegundos da hora) exata do 
				 *  momento da criação do token, no Payload do Token JWT.*/
				.setExpiration(new Date(System.currentTimeMillis()+1000 * 60 * 60))
				/*O Método .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)), da Classe Jwts é responsável por inserir 
				 * a claim exp (expiration - data e hora da expiração), preenchida com a data e a hora (incluindo os milissegundos da hora) 
				 * exata do momento da criação do token, somada ao tempo limite do token, no Payload do Token JWT. Em nosso exemplo, o limite 
				 * de expiração do Token é de 60 minutos 🡪 1 hora./ 60.000 milisegundos =1 minuto/1000 * 60 * 60 = 3.600.000 milissegundos */
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
				/*O Método .signWith(getSignKey(), SignatureAlgorithm.HS256).compact(), da Classe Jwts, é responsável por inserir a assinatura
				 *  do Token (Método getSignKey()) e o Algoritmo de Encriptação do Token JWT (HMAC SHA256 - HS256) do Token JWT. O 
				 *  Método .compact() finaliza a criação do Token JWT e o serializa em uma String compacta e segura para URL, de acordo 
				 *  com as regras do JWT. / O Algoritmo de Encriptação do Token JWT será inserido no Header (Cabeçalho do Token JWT), na forma 
				 *  de um Objeto JSON, */
	}
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}
	

}
