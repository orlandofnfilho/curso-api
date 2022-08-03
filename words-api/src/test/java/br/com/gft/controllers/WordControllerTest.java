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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.gft.dto.word.QueryWordDTO;
import br.com.gft.dto.word.RegisterWordDTO;
import br.com.gft.entities.Tag;
import br.com.gft.entities.Word;
import br.com.gft.services.WordService;

@ExtendWith(MockitoExtension.class)
class WordControllerTest {

	private static final String CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY = "Check if not null, HttpStatus and response body";
	private static final Long ID = 1L;
	private static final String NAME = "Api";
	private static final Integer INDEX = 0;

	@Mock
	private WordService wordService;

	@Mock
	private ModelMapper mapper;

	@InjectMocks
	private WordController wordController;

	private Word word;
	private RegisterWordDTO registerWordDTO;
	private QueryWordDTO queryWordDTO;
	private Page<Word> page;
	private Pageable pageable;
	private Set<Tag> tags = new HashSet<>();

	@BeforeEach
	void setUp() {
		startWord();
	}
	
	@Test
	@DisplayName("Should create a new word")
	void whenCreateThenReturnCreated() throws URISyntaxException {
		when(wordService.create(any())).thenReturn(word);
		when(mapper.map(any(), any())).thenReturn(queryWordDTO);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request)); 
		
	    ResponseEntity<QueryWordDTO> response  = wordController.create(registerWordDTO);
		
	    assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
	    		, () -> assertNotNull(response)
	    		, () -> assertEquals(ResponseEntity.class, response.getClass())
	    		, () -> assertEquals(HttpStatus.CREATED, response.getStatusCode())
	    		, () -> assertEquals(response.getBody(), queryWordDTO));	
		
	}
	
	
	@Test
	@DisplayName("Should return a page of words")
	void whenFindAllThenReturnAPageOfWordDTO() {
		when(wordService.findAll(pageable)).thenReturn(page);
		when(mapper.map(any(), any())).thenReturn(queryWordDTO);
		
		ResponseEntity<Page<QueryWordDTO>> response = wordController.findAll(pageable);
		
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertNotNull(response.getBody())
				, () -> assertEquals(HttpStatus.OK, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass())
				, () -> assertEquals(PageImpl.class, response.getBody().getClass())
				, () -> assertEquals(QueryWordDTO.class, response.getBody().getContent().get(INDEX).getClass())
				, () -> assertEquals(ID, response.getBody().getContent().get(INDEX).getId())
				, () -> assertEquals(NAME, response.getBody().getContent().get(INDEX).getName()));
	}
	
	
	@Test
	@DisplayName("Should return a word found by id")
	void whenFindByIdThenReturnSuccess() {
		when(wordService.findById(ID)).thenReturn(word);
		when(mapper.map(any(), any())).thenReturn(queryWordDTO);
		
		ResponseEntity<QueryWordDTO> response = wordController.findById(ID);
		
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertNotNull(response.getBody())
				, () -> assertEquals(HttpStatus.OK, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass())
				, () -> assertEquals(ID, response.getBody().getId())
				, () -> assertEquals(NAME, response.getBody().getName()));
	}
	
	
	@Test
	@DisplayName("Should delete a word by id")
	void whenDeleteThenReturnNoContent() {
		doNothing().when(wordService).delete(anyLong());
		
		ResponseEntity<QueryWordDTO> response = wordController.delete(ID);
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass()));
		verify(wordService, times(1)).delete(anyLong());
	}
	
	@Test
	@DisplayName("Should return update then return success")
	void whenUpdateThenReturnSuccess() {
		when(wordService.update(anyLong(), any(RegisterWordDTO.class))).thenReturn(word);
		when(mapper.map(any(Word.class), any())).thenReturn(queryWordDTO);
		
		ResponseEntity<QueryWordDTO> response = wordController.update(ID, registerWordDTO);
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertNotNull(response.getBody())
				, () -> assertEquals(HttpStatus.OK, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass())
				, () -> assertEquals(ID, response.getBody().getId())
				, () -> assertEquals(NAME, response.getBody().getName()));
		
	}
	
	
	// Inits Word's instances 
	private void startWord() {
		word = new Word(ID, NAME, tags);
		registerWordDTO = new RegisterWordDTO(NAME, tags);
		queryWordDTO = new QueryWordDTO(ID, NAME, tags);
		pageable = PageRequest.of(0, 10);
		page = new PageImpl<>(List.of(word), pageable, 10);
	}

}
