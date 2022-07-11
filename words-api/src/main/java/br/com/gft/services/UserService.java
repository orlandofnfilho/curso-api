package br.com.gft.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gft.entities.User;
import br.com.gft.repositories.UserRepository;
import br.com.gft.services.exceptions.DataIntegratyViolationException;
import br.com.gft.services.exceptions.ObjectNotFoundException;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final ModelMapper mapper;
	private final PasswordEncoder encoder;

	public UserService(UserRepository userRepository, ModelMapper mapper, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.encoder = encoder;

	}

	public User findByEmail(String email) {
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isEmpty()) {
			throw new UsernameNotFoundException("Usuário não encontrado");
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
			throw new ObjectNotFoundException("Usuário não encontrado");
		}
		return user.get();
	}

	public User create(User obj) {
		emailValid(obj.getUsername());
		User user = mapper.map(obj, User.class);
		return userRepository.save(user);
	}

	public void delete(Long id) {
		User user = this.findById(id);
		userRepository.delete(user);
	}

	public User update(Long id, User obj) {
		validUpdate(id, obj);
		obj.setId(id);
		obj.setPassword(encoder.encode(obj.getPassword()));
		return userRepository.save(obj);

	}

	private void emailValid(String email) {
		Optional<User> userFound = userRepository.findByEmail(email);
		if (email.length() < 10 && userFound.isPresent()) {
			throw new DataIntegratyViolationException("Email já cadastrado");
		}
	}

	private void validUpdate(Long id, User obj) {
		Optional<User> userSaved = userRepository.findByEmail(obj.getEmail());
		Optional<User> userFoundById = userRepository.findById(id);
		if (userSaved.isPresent()) {
			throw new DataIntegratyViolationException("E-mail já cadastrado");
		}
		if (userFoundById.isEmpty()) {
			throw new ObjectNotFoundException("Usuário não encontrado");
		}
	}

}
