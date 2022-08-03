package br.com.gft.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.opentest4j.MultipleFailuresError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.gft.dto.TagDTO;
import br.com.gft.entities.Tag;
import br.com.gft.entities.Word;
import br.com.gft.exceptions.DataIntegrityViolationException;
import br.com.gft.exceptions.ObjectNotFoundException;
import br.com.gft.repositories.TagRepository;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("TagService tests")
class TagServiceTest {

	private static final Long INVALID_ID = 2L;
	
	private static final String CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES = "Check if not null, object class and attributes values";
	private static final String CHECK_IF_THROWS_OBJECT_NOT_FOUND_EXCEPTION = "Check if throws ObjectNotFoundException";
	private static final String CHECK_IF_THROWS_DATA_INTEGRITY_VIOLATION_EXCEPTION = "Check if throws DataIntegrityViolationException";
	private static final String ETIQUETA_JA_CADASTRADA = "Etiqueta já cadastrada: ";
	private static final Long ID = 1L;
	private static final String NAME = "Substantivo";

	@Mock
	private TagRepository tagRepository;

	@Mock
	private ModelMapper mapper;

	@InjectMocks
	private TagService tagService;

	private Tag tag;
	private TagDTO tagDTO;
	private Optional<Tag> optionalTag;
	private Page<Tag> page;
	private Pageable pageable;
	private Set<Word> words = new HashSet<>();
	
	@BeforeEach
	void setUp() {
		startTag();
	}
	
	@Test
	@DisplayName("1. Should creates a new tag")
	void whenCreateThenReturnSuccess() {
		//when		
		when(tagRepository.save(any())).thenReturn(tag);
		
		//then
		assertTag(tagService.create(tagDTO));	
		
	}

	@Test
	@DisplayName("1.2. Should create then return a DataIntegrityViolationException")
	void whenCreateThenReturnAnDataIntegrityViolationException() {
		//when
		when(tagRepository.findByNameIgnoreCase(anyString())).thenReturn(optionalTag);
		
		//then
		assertThrows(DataIntegrityViolationException.class
				, () -> tagService.create(tagDTO)
				, CHECK_IF_THROWS_DATA_INTEGRITY_VIOLATION_EXCEPTION
		);
		
	}
	
	@Test
	@DisplayName("1.3. when create a Tag with existing name, should DataIntegrityViolationException return correct message")
	void whenCreateTagWithExistingNameShouldDataIntegrityViolationExceptionEmitCorrectMessage() {
		//when
		when(tagRepository.findByNameIgnoreCase(anyString())).thenReturn(optionalTag);
		
		//then
		try {
			tagService.create(tagDTO);
		    fail("expected a DataIntegrityViolationException");
		  } catch (DataIntegrityViolationException e) {
			  assertEquals(ETIQUETA_JA_CADASTRADA + NAME, e.getMessage());
		  }
		
	}
	
	@Test
	@DisplayName("2. Should return a page of Tag")
	void whenFindAllThenReturnAPageOfTags() {
		//when
		when(tagRepository.findAll(pageable)).thenReturn(page);
		
		//then
		Page<Tag> response = tagService.findAll(pageable);
		assertAll("Check if page not null and content"
				, () -> assertNotNull(response)
				, () -> assertEquals(List.of(tag), response.getContent())
				, () -> assertEquals(10, response.getSize())
				, () -> assertEquals(10, response.getTotalElements())
				, () -> assertEquals(1, response.getTotalPages())
		);
	}
	

	@Test
	@DisplayName("3. Should return an Tag instance found by id")
	void whenFindByIdThenReturnAnTagInstance() {
		//when
		when(tagRepository.findById(anyLong())).thenReturn(optionalTag);

		//then
		assertTag(tagService.findById(ID));	

	}
	
	
	@Test
	@DisplayName("3.2. Should throws an ObjectNotFoundException")
	void whenFindByIdThenReturnObjectNotFoundException() {
		//when
		when(tagRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);
		
		//then
		assertThrows(ObjectNotFoundException.class
				, () -> tagService.findById(ID)
				, CHECK_IF_THROWS_OBJECT_NOT_FOUND_EXCEPTION);
		}
	
	
	
	@Test
	@DisplayName("4. Should delete a Tag")
	void whenDeleteThenReturnSuccess() {
		//when
		when(tagRepository.findById(anyLong())).thenReturn(optionalTag);
		doNothing().when(tagRepository).deleteById(anyLong());
		
		//then
		tagService.delete(ID);
		assertAll(
				"Verify if findById and deleteById methods run"
				, () -> verify(tagRepository, times(1)).findById(anyLong())
				, () -> verify(tagRepository, times(1)).deleteById(anyLong())
		);
		
	}
	
	@Test
	@DisplayName("4.2. Should delete then return an ObjectNotFoundException")
	void whenDeleteThenReturnAnObjectNotFoundException() {
		//when
		when(tagRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);
		
		//then
		assertThrows(ObjectNotFoundException.class
				, () -> tagService.delete(ID)
				, CHECK_IF_THROWS_OBJECT_NOT_FOUND_EXCEPTION);
	}
	
	@Test
	@DisplayName("5. Should update a Tag")
	void whenUpdateReturnSuccess() {
		//when
		when(tagRepository.save(any())).thenReturn(tag);
		when(tagRepository.findById(anyLong())).thenReturn(optionalTag);
		
		//then
		assertTag(tagService.update(ID, tagDTO));
		
	}
	
	@Test
	@DisplayName("5.2. Should update then throws a DataIntegrityViolationException")
	void whenUpdateReturnADataIntegrityViolationException() {
		//when
		when(tagRepository.findByNameIgnoreCase(anyString())).thenReturn(optionalTag);
		
		//then
		assertThrows(DataIntegrityViolationException.class
				, () -> tagService.update(ID, tagDTO)
				, CHECK_IF_THROWS_DATA_INTEGRITY_VIOLATION_EXCEPTION
		);
	}
	
	
	@Test
	@DisplayName("5.3. Should update Then throws an ObjectNotFoundException")
	void whenUpdateReturnAnObjectNotFoundException() {
		//when
		when(tagRepository.findById(anyLong())).thenThrow(ObjectNotFoundException.class);	
		
		//then
		assertThrows(ObjectNotFoundException.class
				, () -> tagService.update(INVALID_ID, tagDTO)
				, CHECK_IF_THROWS_OBJECT_NOT_FOUND_EXCEPTION);
	}


	// Inits Tag's instances
	private void startTag() {
		tag = new Tag(ID, NAME, words);
		tagDTO = new TagDTO(ID, NAME, words);
		optionalTag = Optional.of(new Tag(ID, NAME, words));
		pageable = PageRequest.of(0, 10);
		page = new PageImpl<>(List.of(tag), pageable, 10);

	}
	
	// Assert Tag
	private void assertTag(Tag response) throws MultipleFailuresError {
		assertAll(
				CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
				, () -> assertNotNull(response)
				, () -> assertEquals(Tag.class, response.getClass())
				, () -> assertEquals(ID, response.getId())
				, () -> assertEquals(NAME, response.getName())
		);
	}

}
