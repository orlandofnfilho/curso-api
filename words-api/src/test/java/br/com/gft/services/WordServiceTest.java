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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

import br.com.gft.dto.word.RegisterWordDTO;
import br.com.gft.entities.Tag;
import br.com.gft.entities.Word;
import br.com.gft.exceptions.DataIntegrityViolationException;
import br.com.gft.exceptions.ObjectNotFoundException;
import br.com.gft.repositories.WordRepository;


@ExtendWith(MockitoExtension.class)
class WordServiceTest {

	private static final String CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES = "Check if not null, object class and attributes values";
	private static final String CHECK_IF_THROWS_OBJECT_NOT_FOUND_EXCEPTION = "Check if throws ObjectNotFoundException";
	private static final String PALAVRA_JA_CADASTRADA = "Palavra já cadastrada";
	private static final Long ID = 1L;
	private static final String NAME = "Api";
	private static final String NAME_UPDATE = "REST";

	@Mock
	private WordRepository wordRepository;

	@Mock
	private ModelMapper mapper;

	@InjectMocks
	private WordService wordService;

	private Word word;
	private RegisterWordDTO registerWordDTO;
	private Optional<Word> optionalWord;
	private Page<Word> page;
	private Pageable pageable;
	private Set<Tag> tags = new HashSet<>();
	
	@BeforeEach
	void setUp() {
		startWord();
	}
	
	@Test
	@DisplayName("Should creates a new word")
	void whenCreateThenReturnSuccess() {
		when(wordRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
		when(mapper.map(any(RegisterWordDTO.class), any())).thenReturn(word);
		when(wordRepository.save(any())).thenReturn(word);
		
		Word response = wordService.create(registerWordDTO);
		
		assertAll("Check if saves a new word"
				, () -> assertNotNull(response)
				, () -> assertEquals(Word.class, response.getClass())
				, () -> assertEquals(ID, response.getId())
				, () -> assertEquals(NAME, response.getName()));	
		
	}
	
	@Test
	@DisplayName("Should create then return a DataIntegrityViolationException")
	void whenCreateThenReturnAnDataIntegrityViolationException() {
		when(wordRepository.findByNameIgnoreCase(anyString())).thenReturn(optionalWord);
		try {
			wordService.create(registerWordDTO);
		}catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
			assertEquals(PALAVRA_JA_CADASTRADA +": "+NAME, e.getMessage());
		}
		
		
	}
	
	@Test
	@DisplayName("Should return a page of Word")
	void whenFindAllThenReturnAPageOfWords() {
		when(wordRepository.findAll(pageable)).thenReturn(page);
		Page<Word> response = wordService.findAll(pageable);
		
		assertAll("Check if page not null and content"
				, () -> assertNotNull(response)
				, () -> assertEquals(List.of(word), response.getContent())
				, () -> assertEquals(10, response.getSize())
				, () -> assertEquals(10, response.getTotalElements())
				, () -> assertEquals(1, response.getTotalPages()));
	}
	

	@Test
	@DisplayName("Should return an Word instance found by id")
	void whenFindByIdThenReturnAnWordInstance() {
		when(wordRepository.findById(anyLong())).thenReturn(optionalWord);

		Word response = wordService.findById(ID);
		
		assertAll(CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
				, () -> assertNotNull(response)
				, () -> assertEquals(Word.class, response.getClass())
				, () -> assertEquals(ID, response.getId())
				, () -> assertEquals(NAME, response.getName()));
		}
	
	
	@Test
	@DisplayName("Should throws an ObjectNotFoundException")
	void whenFindByIdThenReturnObjectNotFoundException() {
		when(wordRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);
			
		assertThrows(ObjectNotFoundException.class
				, () -> wordService.findById(ID)
				, CHECK_IF_THROWS_OBJECT_NOT_FOUND_EXCEPTION);
		}
	
	
	
	@Test
	@DisplayName("Should delete a word")
	void whenDeleteThenReturnSuccess() {
		when(wordRepository.findById(anyLong())).thenReturn(optionalWord);
		doNothing().when(wordRepository).deleteById(anyLong());
		wordService.delete(ID);
		verify(wordRepository, times(1)).deleteById(anyLong());
	}
	
	@Test
	@DisplayName("Should delete then return an ObjectNotFoundException")
	void whenDeleteThenReturnAnObjectNotFoundException() {
		when(wordRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);
		
		assertThrows(ObjectNotFoundException.class
				, () -> wordService.delete(ID)
				, CHECK_IF_THROWS_OBJECT_NOT_FOUND_EXCEPTION);
	}
	
	@Test
	@DisplayName("Should update a word")
	void whenUpdateReturnSuccess() {
		when(wordRepository.save(any())).thenReturn(word);
		when(wordRepository.findById(anyLong())).thenReturn(optionalWord);
		Word response = wordService.update(ID, registerWordDTO);
		assertAll(CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
				, () -> assertNotNull(response)
				, () -> assertEquals(Word.class, response.getClass())
				, () -> assertEquals(ID, response.getId())
				, () -> assertEquals(NAME, response.getName()));
		
		
	}
	
	@Test
	@DisplayName("Should update then throws a DataIntegrityViolationException")
	void whenUpdateReturnADataIntegrityViolationException() {
		registerWordDTO.setName(NAME_UPDATE);
		Optional<Word> otherOptional = Optional.of(new Word(ID, NAME_UPDATE, tags));
	
		when(wordRepository.findById(anyLong())).thenReturn(optionalWord);
		when(wordRepository.findByNameIgnoreCase(anyString())).thenReturn(otherOptional);
		
		try {
			wordService.update(ID, registerWordDTO);
		}catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
			assertEquals(PALAVRA_JA_CADASTRADA + ": "+NAME_UPDATE, e.getMessage());
		}
	}
	
	
	@Test
	@DisplayName("Should update Then throws an ObjectNotFoundException")
	void whenUpdateReturnAnObjectNotFoundException() {
		when(wordRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);	
		
		assertThrows(ObjectNotFoundException.class
				, () -> wordService.update(2L, registerWordDTO)
				, CHECK_IF_THROWS_OBJECT_NOT_FOUND_EXCEPTION);
	}
	


	// Inits Word's instances
	private void startWord() {
		word = new Word(ID, NAME, tags);
		registerWordDTO = new RegisterWordDTO(NAME, tags);
		optionalWord = Optional.of(new Word(ID, NAME, tags));
		pageable = PageRequest.of(0, 10);
		page = new PageImpl<>(List.of(word), pageable, 10);

	}

}
