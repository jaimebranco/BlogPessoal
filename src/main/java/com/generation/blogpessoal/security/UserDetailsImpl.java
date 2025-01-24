package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

public class UserDetailsImpl implements UserDetails {
	
		private static final long serialVersionUID = 1L; 
		//serialVersionUID: Usado para garantir a compatibilidade durante a serialização.
		
		private String userName;
		private String password;
		//userName e password: Armazenam o nome de usuário e a senha.
		private List<GrantedAuthority> authorities;
		//authorities: Lista de autoridades (permissões) do usuário.
		/*Define o Atributo authorities como uma Collection List do tipo GrantedAuthority. O atributo authorities é responsável
		 * por receber os Direitos de Acesso do Usuário (Autorizações ou Roles), que são Objetos de uma Classe (a Classe poderia
		 *  se chamar Roles, por exemplo), que herdará a Interface GrantedAuthority. O atributo authorities deve ser obrigatoriamente
		 *   inserido na lista de Atributos da Classe, devido ao Método getAuthorities(), assinado na Interface UserDetails, 
		 *   que retornará uma coleção (lista) com os valores do atributo com o mesmo nome.*/
		
		public UserDetailsImpl(Usuario user) {
			this.userName = user.getUsuario();
			this.password = user.getSenha();
			/*O primeiro construtor inicializa userName e password com os valores do 
			 * objeto Usuario.*/
			
		}
		public UserDetailsImpl() {} 
		//O segundo construtor é um construtor padrão sem parâmetros*/
		
		@Override /*indica que este Método está sendo Sobrescrito (Polimorfismo de Sobrescrita), ou seja, é um Método da Interface
		 UserDetails, que obrigatoriamente deve ser implementado.*/
		public Collection<? extends GrantedAuthority> getAuthorities (){ /* ? Este sinal significa que o Método pode receber um
		 Objeto de qualquer Classe. Se os Direitos de Acesso fossem implementados, a interrogação seria substituída pelo nome da 
		 Classe responsável por definir os roles do usuário*/
			return authorities;
		}
		@Override
		public String getPassword () {
			/*getUsername() e getPassword(), que retornarão os valores do atributos username e password. Observe que os dois
			 *  Métodos estão anotados com a anotação @Override, o que indica que estes Métodos estão sendo Sobrescritos 
			 *  (Polimorfismo de Sobrescrita), ou seja, são Métodos da Interface UserDetails, que obrigatoriamente devem ser 
			 *  implementados.*/
			return password;
		}
		@Override
		public String getUsername() {
			return userName;
		}
		@Override
		public boolean isAccountNonExpired() {
			// 	Indica se o acesso do usuário expirou (tempo de acesso). Uma conta expirada não pode ser autenticada (return false).
			return true;
		}
		@Override
		public boolean isAccountNonLocked () {
			// 	Indica se o usuário está bloqueado ou desbloqueado. Um usuário bloqueado não pode ser autenticado (return false).
			return true;
		}
		@Override 
		public boolean isCredentialsNonExpired() {
			/*Indica se as credenciais do usuário (senha) expiraram (precisa ser trocada). Senha expirada impede a autenticação
			(return false).*/
			return true;
		}
		@Override
		public boolean isEnabled () {
			//Indica se o usuário está habilitado ou desabilitado. Um usuário desabilitado não pode ser autenticado (return false).
			return true;
		}
}
