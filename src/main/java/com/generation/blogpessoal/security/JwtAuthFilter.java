package com.generation.blogpessoal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, /* responsável por implementar um Filtro de Servlet personalizado.*/
			HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		/* Esta linha está obtendo o valor do cabeçalho "Authorization" da requisição HTTP. Este cabeçalho geralmente contém o token de autenticação.*/
		String token = null;
		String username = null;
		/*Estas linhas estão declarando duas variáveis, token e username, e inicializando-as com null. Provavelmente, estas variáveis serão usadas 
		 * posteriormente no método para armazenar o token de autenticação e o nome de usuário extraído do token.*/
		
		  try{
	            if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                token = authHeader.substring(7);
	                username = jwtService.extractUsername(token);
	            }

	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	                    
	                if (jwtService.validateToken(token, userDetails)) {
	                    UsernamePasswordAuthenticationToken authToken = 
	                    		new UsernamePasswordAuthenticationToken(userDetails, null, 
	                    				userDetails.getAuthorities());
	                    
	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authToken);
	                }
	            
	            }
	            
	            filterChain.doFilter(request, response);

	        }catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException 
	                | SignatureException | ResponseStatusException e){
	            response.setStatus(HttpStatus.FORBIDDEN.value());
	            return;
	        }
	    }
	}