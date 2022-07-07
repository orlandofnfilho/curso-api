package gft.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gft.dto.usuario.ConsultaUsuarioDTO;
import gft.dto.usuario.RegistroUsuarioDTO;
import gft.dto.usuario.UsuarioMapper;
import gft.entities.Usuario;
import gft.services.UsuarioService;

@RestController
@RequestMapping("v1/usuarios")
public class UsuarioController {
	
	private final UsuarioService usuarioService;
	
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@PostMapping
	public ResponseEntity<ConsultaUsuarioDTO> salvarUsuario(@RequestBody RegistroUsuarioDTO dto){
			
			Usuario usuario = usuarioService.salvarUsuario(UsuarioMapper.fromDTO(dto));
			
			return ResponseEntity.ok(UsuarioMapper.fromEntity(usuario));
			
	}

	
	
}
