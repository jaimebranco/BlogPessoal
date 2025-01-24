package com.generation.blogpessoal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service /* indica que esta Classe é uma Classe de Serviço. Classe de Serviço é uma Classe responsável por implementar as regras
 de negócio e as tratativa de dados de uma parte do ou recurso do sistema.*/
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired //Injeção de Dependência
	private UsuarioRepository usuarioRepository;
	/*Cria um Objeto da Classe Optional do tipo Usuario, que receberá o retorno da Query Method findByUsuario(String usuario),
	implementada na Interface UsuarioRepository, para checar se o usuário digitado está persistido no Banco de dados,*/
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(userName);
		
		if(usuario.isPresent())
			return new UserDetailsImpl(usuario.get());
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		   	/*HTTP Status 403 - FORBIDDEN (Acesso Proibido - você está tentando alcançar um endereço ou um site ao qual está 
		   	 * proibido de acessar*/
		
	}

}
