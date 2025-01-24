package com.generation.blogpessoal.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration /*
				 * indica que a Classe é do tipo configuração, ou seja, define uma Classe como
				 * fonte de definições de Beans
				 */
@EnableWebSecurity /*
					 * habilita a segurança de forma Global (toda a aplicação) e sobrescreve os
					 * Métodos que irão redefinir as regras de Segurança da sua aplicação.
					 */
public class BasicSecurityConfig {

	@Autowired
	private JwtAuthFilter authFilter;

	@Bean /*
			 * Bean: No Spring, os objetos que formam a espinha dorsal da sua aplicação e
			 * que são gerenciados pelo Spring são chamados de Beans. Um Bean é um objeto
			 * que é instanciado, montado e gerenciado pelo Spring.
			 */
	UserDetailsService userDetailsService() {

		return new UserDetailsServiceImpl();
		/*o Método userDetailsService, que retornará uma instância da Classe UserDetailsServiceImpl, que implementa a Interface 
		 * userDetailsService. */
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		/*Nós utilizaremos este Método para Criptografar e Validar a senha do usuário.*/
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		/*o Método authenticationProvider, que retornará uma instância da Classe AuthenticationProvider, informando o Método de 
		 * autenticação que será utilizado.*/
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		/* Cria um Objeto da Classe DaoAuthenticationProvider, chamado authenticationProvider. A Classe DaoAuthenticationProvider 
		 * é utilizada para autenticar um Objeto da Classe Usuario através do usuario (e-mail) e a senha, validando os dados no 
		 * Banco de dados de aplicação, através da Classe UserDetailsServiceImpl.*/
		authenticationProvider.setUserDetailsService(userDetailsService());
		/*rá utilizado para validar o usuario (e-mail) do Objeto da Classe Usuario.*/
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		/* Adiciona um Objeto da Classe PasswordEncoder através do Método setPasswordEncoder(), que será utilizado para validar a senha do Usuário.*/
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
	/*O Método authenticationManager(AuthenticationConfiguration authenticationConfiguration), implementa a confguração de autenticação.*/
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*estamos informando ao Spring que a configuração padrão da Spring Security será substituída por uma nova configuração. Nesta configuração
		 *  iremos customizar a autenticação da aplicação desabilitando o formulário de login e habilitando a autenticação via HTTP.*/

		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		/*definiremos que o nosso sistema não guardará sessões para o cliente. Quando o cliente fizer uma Requisição HTTP, ela deverá ter todas as
		 *  informações necessárias para o servidor atender à Requisição e a mesma será finalizada com a Resposta HTTP do servidor. O servidor 
		 *  nunca dependerá de informações de Requisições HTTP anteriores para processar uma nova Requisição HTTP.*/
				.csrf(csrf -> csrf.disable()).cors(withDefaults());
		/*remos desabilitar a proteção que vem ativa contra ataques do tipo CSRF (Cross-Site-Request-Forgery), que seria uma interceptação dos 
		 * dados de autenticação antes da Requisição chegar ao servidor. Esta configuração foi desabilitada porquê o Spring Security fica 
		 * procurando por um parâmetro oculto adicional em qualquer requisição do tipo POST / PUT / DELETE, chamado Token CSRF. Como ele não 
		 * vai encontrar, todas as requisições diferentes de GET seriam bloqueadas.*/
		/*vamos liberar o acesso de outras origens (Requisições de outros servidores HTTP), desta forma nossa aplicação poderá ser acessada
		 *  por outros domínios, ou seja, de outros endereços, além do endereço onde a aplicação está hospedada. O Método .withDefaults() 
		 *  inicializa o CORS seguindo os padrões fornecidos pela Spring Security*/

		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/usuarios/logar").permitAll()
				/* Através do Método .authorizeHttpRequests((auth) -> auth), vamos implementar a Expressão Lambda para definir quais
				 *  endpoints poderão acessar o sistema sem precisar de autenticação. O Objeto auth recebe o endereço (URI) da Requisição 
				 *  e checa se o endpoint necessita ou não de autenticação.*/
				.requestMatchers("/usuarios/cadastrar").permitAll().requestMatchers("/error/**").permitAll()
				/*indicaremos os endereços (URI) dos endpoints, que estarão acessíveis sem autentica.
				 * No projeto Blog Pessoal foi definido que apenas os endpoints logar e cadastrar serão livres de autenticação.*/
				.requestMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated())
				/* permite que o cliente (front-end), possa descobrir quais são as opções permitidas e/ou obrigatórias no cabeçalho da
				 *  Requisição HTTP. Se o parâmetro HttpMethod.OPTIONS não for liberado, a aplicação não receberá o Token JWT através do
				*	Cabeçalho da Requisição (Header), impedindo a aplicação de responder as Requisições protegidas.*/
				.authenticationProvider(authenticationProvider())
				/*Através do Método .anyRequest().authenticated(), informamos ao sistema que todos os endpoints que não estiverem
				 *  especificados na lista acima, a autenticação será obrigatória.*/
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).httpBasic(withDefaults());
				/*informamos ao sistema que o servidor irá receber requisições que devem ter o esquema HTTP Basic de autenticação. O 
				 * Método .withDefaults() inicializa o esquema de autenticação HTTP Basic seguindo os padrões fornecidos pela Spring 
				 * Security.*/

		return http.build();
		/*: Através do Método return http.build(), o Objeto http com as configurações implementadas será instanciado.*/
	}

}