package com.generation.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration //indica que a Classe é do tipo configuração, ou seja, define uma Classe como fonte de definições de Beans.
public class SwaggerConfig {

	@Bean /*indica ao Spring que ele deve invocar aquele Método e gerenciar o objeto retornado por ele, ou seja, agora este 
	objeto pode ser injetado em qualquer ponto da sua aplicação.
	Bean: No Spring, os objetos que formam a espinha dorsal da sua aplicação e que são gerenciados pelo Spring são chamados de Beans. 
	Um Bean é um objeto que é instanciado, montado e gerenciado pelo Spring.
    Existem diversas formas de se criar Beans no Spring. Você pode criar Classes anotadas com @Configuration ou @Service para serem 
    gerenciadas pelo Spring, assim como pode usar a anotação @Bean em um Método, e transformar a instância retornada pelo Método em 
    um Objeto gerenciado pelo Spring (seja de uma Classe própria ou de terceiros).
    Estas Classes, que na visão do Spring são os Beans, para você nada mais são do que Classes que você irá escrever as regras de 
    funcionamento da sua aplicação, que poderão ser utilizadas em qualquer Classe, diferente da Injeção de Dependência criada pela 
    anotação @Autowired, que só permite o uso dentro da Classe em que foi criada.
*/
    OpenAPI springBlogPessoalOpenAPI() {
        return new OpenAPI() /*Cria um Objeto da Classe OpenAPI, que gera a documentação no Swagger utilizando a especificação OpenAPI.*/
            .info(new Info()
                .title("Projeto Blog Pessoal")
                .description("Projeto Blog Pessoal - Generation Brasil")
                .version("v0.0.1")
                .license(new License()
                    .name("Blog Pessoal")
                    .url("https://brazil.generation.org/"))
                .contact(new Contact()
                    .name("Jaime Filho")
                    .url("https://github.com/jaimebranco")
                    .email("jaimebrancofilho@gmail.com")))
            .externalDocs(new ExternalDocumentation()
                .description("Github")
                .url("https://github.com/jaimebranco"));
    }


	@Bean
	OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
		//permite personalizar o Swagger, baseado na Especificação OpenAPI.
		return openApi -> {
			//Cria um Objeto da Classe OpenAPI, que gera a documentação no Swagger utilizando a especificação OpenAPI.
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
				/*Cria um primeiro looping que fará a leitura de todos os recursos (Paths) através do Método getPaths(), 
				 * que retorna o caminho de cada endpoint. Na sequência, cria um segundo looping que Identificará qual Método 
				 * HTTP (Operations), está sendo executado em cada endpoint através do Método readOperations(). Para cada Método, 
				 * todas as mensagens serão lidas e substituídas pelas novas mensagens.*/
				

				ApiResponses apiResponses = operation.getResponses();
				/*Cria um Objeto da Classe ApiResponses, que receberá as Respostas HTTP de cada endpoint (Paths) através do Método getResponses().*/

				apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
				apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
				apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
				apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido!"));
				apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));
				
				/* Adiciona as novas Respostas no endpoint, substituindo as atuais e acrescentando as demais, através do Método addApiResponse(),
				 *  identificadas pelo HTTP Status Code (200, 201 e etc).*/
			}));
		};
	}

	private ApiResponse createApiResponse(String message) {
		//O Método createApiResponse() adiciona uma descrição (Mensagem), em cada Resposta HTTP.

		return new ApiResponse().description(message);

	}
}