package br.com.gft.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.ArrayList;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.gft.dto.RegUserDTO;
import br.com.gft.dto.UserDTO;
import br.com.gft.dto.word.QueryWordDTO;
import br.com.gft.entities.Perfil;
import br.com.gft.entities.User;
import br.com.gft.services.UserService;



@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	private static final long ID = 1L;
	private static final Integer INDEX = 0;
	private static final String SENHA = "Gft@1234";
	private static final String EMAIL = "admin@gft.com";
	private static final String NOME = "Admin";
	private static final String NOME_PERFIL = "ADMIN";
	private static final String CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY = "Check if not null, HttpStatus and response body";
	
	@Mock
	private UserService userService;
	
	@Mock
	private ModelMapper mapper;
	
	@InjectMocks
	private UserController userController;
	
	private User user;
	private UserDTO userDTO;
	private RegUserDTO regUserDTO;
	private Optional<User> optionalUser; 
	private Perfil perfil;
	
	@Test
	@DisplayName("Should create a new User")
	void whenCreateThenReturnCreated() throws URISyntaxException {
		when(userService.create(any())).thenReturn(user);
		when(mapper.map(any(), any())).thenReturn(userDTO);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request)); 
		

	    ResponseEntity<UserDTO> response  = userController.create(regUserDTO);
		
		
	    assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
	    		, () -> assertNotNull(response)
	    		, () -> assertEquals(ResponseEntity.class, response.getClass())
	    		, () -> assertEquals(HttpStatus.CREATED, response.getStatusCode())
	    		, () -> assertEquals(response.getBody(), userDTO));	
		
	}
	
	@Test
	@DisplayName("Should return a word found by id")
	void whenFindByIdThenReturnSuccess() {
		when(userService.findById(ID)).thenReturn(user);
		when(mapper.map(any(), any())).thenReturn(userDTO);
		
		ResponseEntity<UserDTO> response = userController.findById(ID);
		
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertNotNull(response.getBody())
				, () -> assertEquals(HttpStatus.OK, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass())
				, () -> assertEquals(ID, response.getBody().getId())
				, () -> assertEquals(NOME, response.getBody().getNome())
				, () -> assertEquals(EMAIL, response.getBody().getEmail())
				, () -> assertEquals(NOME_PERFIL, response.getBody().getNomePerfil()));
	}
	
	@Test
	@DisplayName("Should return a list of Users")
    void whenFindAllThenReturnAListOfUserDTO() {
        when(userService.findAll()).thenReturn(List.of(user));
        when(mapper.map(any(), any())).thenReturn(userDTO);

        ResponseEntity<List<UserDTO>> response = userController.findAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ArrayList.class, response.getBody().getClass());
        assertEquals(UserDTO.class, response.getBody().get(INDEX).getClass());

        assertEquals(ID, response.getBody().get(INDEX).getId());
        assertEquals(NOME, response.getBody().get(INDEX).getNome());
        assertEquals(EMAIL, response.getBody().get(INDEX).getEmail());
    }
	
	@Test
	@DisplayName("Should delete a word by id")
	void whenDeleteThenReturnNoContent() {
		doNothing().when(userService).delete(anyLong());
		
		ResponseEntity<UserDTO> response = userController.delete(ID);
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass()));
		verify(userService, times(1)).delete(anyLong());
	}
	
	@Test
	@DisplayName("Should return update then return success")
	void whenUpdateThenReturnSuccess() {
		when(userService.update(anyLong(), any())).thenReturn(user);
		
		ResponseEntity<User> response = userController.update(ID, user);
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertNotNull(response.getBody())
				, () -> assertEquals(HttpStatus.OK, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass())
				, () -> assertEquals(ID, response.getBody().getId())
				, () -> assertEquals(NOME, response.getBody().getNome())
				, () -> assertEquals(NOME_PERFIL, response.getBody().getPerfil().getNome()));
		
	}
	

	@BeforeEach
	void setUp() throws Exception {
		perfil =  new Perfil(ID, NOME_PERFIL);
		user = new User(1L, NOME, EMAIL, SENHA, perfil);
		userDTO = new UserDTO(ID, NOME, EMAIL, NOME_PERFIL);
		regUserDTO = new RegUserDTO(NOME, EMAIL, SENHA, ID);
		optionalUser = Optional.of(user);
		
		
	}



}
