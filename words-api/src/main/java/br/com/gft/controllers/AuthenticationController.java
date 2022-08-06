package br.com.gft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gft.controllers.docs.AuthenticationControllerDocs;
import br.com.gft.dto.TokenDTO;
import br.com.gft.entities.AuthenticationForm;
import br.com.gft.services.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController implements AuthenticationControllerDocs {

	@Autowired
	AuthenticationService authenticationService;

	@PostMapping
	public ResponseEntity<TokenDTO> authenticate(@RequestBody AuthenticationForm authForm) {
		try {
			return ResponseEntity.ok(authenticationService.autenticar(authForm));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

}
