package br.com.gft.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gft.dto.RegUserDTO;
import br.com.gft.entities.User;
import br.com.gft.exceptions.DataIntegrityViolationException;
import br.com.gft.exceptions.ObjectNotFoundException;
import br.com.gft.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private static final String E_MAIL_JA_CADASTRADO = "E-mail já cadastrado";
	private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
	private final UserRepository userRepository;
	private final ModelMapper mapper;

	public UserService(UserRepository userRepository, ModelMapper mapper) {
		this.userRepository = userRepository;
		this.mapper = mapper;

	}

	public User findByEmail(String email) {
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isEmpty()) {
			throw new UsernameNotFoundException(USUARIO_NAO_ENCONTRADO);
		}
		return optional.get();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return findByEmail(username);
	}

	public User findById(Long idUser) {
		Optional<User> user = userRepository.findById(idUser);
		if (user.isEmpty()) {
			throw new ObjectNotFoundException(USUARIO_NAO_ENCONTRADO);
		}
		return user.get();
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User create(RegUserDTO obj) {
		emailValid(obj.getEmail());
		User user = mapper.map(obj, User.class);
		user.getPerfil().setId(obj.getPerfilId());
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		return userRepository.save(user);
	}

	public void delete(Long id) {
		User user = this.findById(id);
		userRepository.delete(user);
	}

	public User update(Long id, User obj) {
		validUpdate(id, obj);
		obj.setId(id);
		obj.setPassword(new BCryptPasswordEncoder().encode(obj.getPassword()));
		return userRepository.save(obj);
	}

	protected void emailValid(String email) {
		Optional<User> userFound = userRepository.findByEmail(email);
		if (email.length() < 12 && userFound.isPresent()) {
			throw new DataIntegrityViolationException(E_MAIL_JA_CADASTRADO);
		}
	}

	protected void validUpdate(Long id, User obj) {
		Optional<User> userSaved = userRepository.findByEmail(obj.getEmail());
		Optional<User> userFoundById = userRepository.findById(id);
		if (userSaved.isPresent()) {
			throw new DataIntegrityViolationException(E_MAIL_JA_CADASTRADO);
		}
		if (userFoundById.isEmpty()) {
			throw new ObjectNotFoundException(USUARIO_NAO_ENCONTRADO);
		}
	}

}
