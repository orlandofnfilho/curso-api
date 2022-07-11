package br.com.gft.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.gft.dto.TokenDTO;
import br.com.gft.entities.AuthenticationForm;
import br.com.gft.entities.User;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationManager authManager;

	@Value("${words-api.jwt.secret}")
	private String secret;

	@Value("${words-api.jwt.expiration}")
	private String expiration;

	@Value("${words-api.jwt.issuer}")
	private String issuer;

	public TokenDTO autenticar(AuthenticationForm authForm) throws AuthenticationException {

		Authentication authenticate = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(authForm.getEmail(), authForm.getPassword()));
		String token = gerarToken(authenticate);
		return new TokenDTO(token);

	}

	private Algorithm criarAlgoritimo() {
		return Algorithm.HMAC256(secret);
	}

	private String gerarToken(Authentication authenticate) {
		User principal = (User) authenticate.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

		return JWT.create().withIssuer(issuer).withExpiresAt(dataExpiracao).withSubject(principal.getId().toString())
				.sign(this.criarAlgoritimo());
	}

	public boolean verificaToken(String token) {
		try {
			if (token == null)
				return false;
			
			JWT.require(this.criarAlgoritimo()).withIssuer(issuer).build().verify(token);
			return true;
		} catch (JWTVerificationException e) {
			return false;
		}

	}

	public Long retornarIdUser(String token) {
		String subject = JWT.require(this.criarAlgoritimo()).withIssuer(issuer).build().verify(token).getSubject();
		return Long.parseLong(subject);
		
	}

}
