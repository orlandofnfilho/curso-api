package br.com.gft.config;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.gft.entities.Perfil;
import br.com.gft.entities.User;
import br.com.gft.repositories.PerfilRepository;
import br.com.gft.repositories.UserRepository;

@Component
@Transactional
public class StartUsers implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PerfilRepository perfilRepository;

	@Override
	public void run(String... args) throws Exception {
		Perfil p1 = new Perfil(null, "ADMIN");
		Perfil p2 = new Perfil(null, "USUARIO");

		if (perfilRepository.findAll().isEmpty()) {
			perfilRepository.save(p1);
			perfilRepository.save(p2);
		}

		User u1 = new User(null, "admin", "admin@gft.com", new BCryptPasswordEncoder().encode("Gft@1234"), p1);
		User u2 = new User(null, "usuario", "usuario@gft.com", new BCryptPasswordEncoder().encode("Gft@1234"), p2);

		if (userRepository.findAll().isEmpty()) {
			userRepository.save(u1);
			userRepository.save(u2);
		}

	}

}
