package br.com.gft.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.gft.dto.RegUserDTO;
import br.com.gft.entities.Perfil;
import br.com.gft.entities.User;
import br.com.gft.exceptions.DataIntegrityViolationException;
import br.com.gft.exceptions.ObjectNotFoundException;
import br.com.gft.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	private static final long ID = 1L;
	private static final Integer INDEX = 0;
	private static final String SENHA = "Gft@1234";
	private static final String EMAIL = "admin@gft.com";
	private static final String NOME = "Admin";
	private static final String NOME_PERFIL = "ADMIN";
	private static final String CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES = "Check if not null, object class and attributes values";
	private static final String E_MAIL_JA_CADASTRADO = "E-mail já cadastrado";
	private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
	
	private User user;
	private RegUserDTO regUserDTO;
	private Optional<User> optionalUser;
	private Perfil perfil;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ModelMapper mapper;
	
	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp(){
		startUser();
	}
	
	@Test
	@DisplayName("Should return an User found by email")
	void whenFindByEmailThenReturnSuccess() {
		when(userRepository.findByEmail(anyString())).thenReturn(optionalUser);
		
		User response = userService.findByEmail(EMAIL);
		assertAll(CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
				, () -> assertNotNull(response)
				, () -> assertEquals(User.class, response.getClass())
				, () -> assertEquals(EMAIL, response.getEmail()));
	}
	
	
	@Test
	@DisplayName("Should return a DataIntegrityException")
	void whenValidEmailThenReturnTrue() {
		when(userRepository.findByEmail(anyString())).thenThrow(DataIntegrityViolationException.class);
		assertThrows(DataIntegrityViolationException.class, () -> userService.emailValid(EMAIL));
	}
	

	@Test
	@DisplayName("Should return a UsernameNotFoundException")
	void whenFindByEmailThenReturnAnException() {
		when(userRepository.findByEmail(anyString())).thenThrow(UsernameNotFoundException.class);
		
		assertThrows(UsernameNotFoundException.class
				, () -> userService.findByEmail(EMAIL));
	}
	
	@Test
	@DisplayName("Should find an User by Id")
	void whenFindByIdThenReturnSuccess(){
		when(userRepository.findById(anyLong())).thenReturn(optionalUser);
		
		User response = userService.findById(ID);
		
		assertAll(CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
				, () -> assertNotNull(response)
				, () -> assertEquals(User.class, response.getClass())
				, () -> assertEquals(ID, response.getId())
				, () -> assertEquals(NOME, response.getNome())
				, () -> assertEquals(EMAIL, response.getEmail())
				, () -> assertEquals(NOME_PERFIL, response.getPerfil().getNome()));
		
	}

	
	@Test
	@DisplayName("Should return a ObjectNotFoundException")
	void whenFindByIdThenReturnAnException() {
		when(userRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);
		
		assertThrows(ObjectNotFoundException.class
				, () -> userService.findById(ID));
	}
	
	@Test
	@DisplayName("Should return a list of User")
	void whenFindAllThenReturnAListOfUser() {
		when(userRepository.findAll()).thenReturn(List.of(user));
		
		List<User> response = userService.findAll();
		assertAll(CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
				, () -> assertNotNull(response)
				, () -> assertEquals(User.class, response.get(INDEX).getClass())
				, () -> assertEquals(1, response.size())
				, () -> assertEquals(ID, response.get(INDEX).getId())
				, () -> assertEquals(NOME, response.get(INDEX).getNome())
				, () -> assertEquals(EMAIL, response.get(INDEX).getEmail())
				, () -> assertEquals(NOME_PERFIL, response.get(INDEX).getPerfil().getNome()));
	}
	
	@Test
	@DisplayName("Should create create an User")
	void whenCreateThenReturnSuccess() {
		when(userRepository.save(any())).thenReturn(user);
		when(mapper.map(any(), any())).thenReturn(user);
		
		User response = userService.create(regUserDTO);
		assertAll(CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
		         , () -> assertNotNull(response)
		         , () -> assertEquals(User.class, response.getClass())
		         , () -> assertEquals(ID, response.getId())
		         , () -> assertEquals(NOME, response.getNome())
		         , () -> assertEquals(EMAIL, response.getEmail())
		         , () -> assertEquals(NOME_PERFIL, response.getPerfil().getNome()));
	}
	
	@Test
	@DisplayName("Should return a DataInegrityViolationException")
	void whenCreateThenReturnADataIntegrityException() {
		when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
		when(mapper.map(any(), any())).thenReturn(user);
		assertThrows(DataIntegrityViolationException.class, () -> userService.create(regUserDTO));
		
	}
	
	@Test
	@DisplayName("Should delete an User by Id")
	void whenDeleteThenRemoveAnUser() {
		when(userRepository.findById(anyLong())).thenReturn(optionalUser);
		doNothing().when(userRepository).delete(any());
		userService.delete(ID);
		verify(userRepository, times(1)).delete(any());
	}
	
	
	@Test
	@DisplayName("Should throw an ObjectNotFoundException")
	void whenDeleteThenThrowAnObjectNotFoundException() {
		when(userRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);
		
		assertThrows(ObjectNotFoundException.class
				, () -> userService.delete(ID));
	}
	
	
	@Test
	@DisplayName("Should update an User")
	void whenUpdateThenReturnSuccess() {
		when(userRepository.save(any())).thenReturn(user);
		when(userRepository.findById(anyLong())).thenReturn(optionalUser);
		
		User response = userService.update(ID, user);
		assertAll(CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
				, () -> assertNotNull(response)
				, () -> assertEquals(User.class, response.getClass())
				, () -> assertEquals(ID, response.getId())
				, () -> assertEquals(NOME, response.getNome())
				, () -> assertEquals(EMAIL, response.getEmail()));
	}
	
	
	@Test
	@DisplayName("Should throws a DataIntegrityViolationException")
	void whenUpdateThenThrowsADataIntegrityException() {
		when(userRepository.findByEmail(anyString())).thenReturn(optionalUser);
		
		try {
			optionalUser.get().setId(2L);
			userService.update(ID, user);	
		}catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
			assertEquals(E_MAIL_JA_CADASTRADO, e.getMessage());
		}
	} 
	
	@Test
	@DisplayName("Should throws an ObjectNotFoundException")
	void whenUpdateThenThrowsAnObjectNotFoundException() {
		when(userRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);
		
		assertThrows(ObjectNotFoundException.class
				, () -> userService.update(2L, user)
				, "Check if throws ObjectNotFoundException");
	} 
	
	private void startUser() {
		perfil =  new Perfil(ID, NOME_PERFIL);
		user = new User(1L, NOME, EMAIL, SENHA, perfil);
		regUserDTO = new RegUserDTO(NOME, EMAIL, SENHA, ID);
		optionalUser = Optional.of(user);
		
	}
}
