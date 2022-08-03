package br.com.gft.controllers;

import static br.com.gft.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import br.com.gft.dto.TagDTO;
import br.com.gft.entities.Tag;
import br.com.gft.entities.Word;
import br.com.gft.services.TagService;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
	
	private static final String TAG_API_URL_PATH = "/api/v1/tags";

	private static final String CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY = "Check if not null, HttpStatus and response body";
	private static final Long ID = 1L;
	private static final String NAME = "Substantivo";
	private static final Integer INDEX = 0;

	@Mock
	private TagService tagService;

	@Mock
	private ModelMapper mapper;

	@InjectMocks
	private TagController tagController;
	
	private MockMvc mockMvc;

	private Tag tag;
	private TagDTO tagDTO;
	private Page<Tag> page;
	private Pageable pageable;
	private Set<Word> words = new HashSet<>();

	
	@BeforeEach
	void setUp() {
		startTag();
		mockMvc = MockMvcBuilders.standaloneSetup(tagController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
	}
	
	@Test
	@DisplayName("Should create a new tag")
	void whenCreateThenReturnCreated() throws URISyntaxException {
		//when
		when(tagService.create(any())).thenReturn(tag);
		when(mapper.map(any(), any())).thenReturn(tagDTO);
		
		try {
			mockMvc.perform(post(TAG_API_URL_PATH)
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(asJsonString(tagDTO)))
			    .andExpectAll(
			    		status().isCreated()
			    		, jsonPath("$.name", is(tagDTO.getName()))
			    );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            		
	}
	
	
	@Test
	@DisplayName("Should return a page of tags")
	void whenFindAllThenReturnAPageOfTagDTO() {
		//when
		when(tagService.findAll(pageable)).thenReturn(page);
		when(mapper.map(any(), any())).thenReturn(tagDTO);
		
		ResponseEntity<Page<TagDTO>> response = tagController.findAll(pageable);
		
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertNotNull(response.getBody())
				, () -> assertEquals(HttpStatus.OK, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass())
				, () -> assertEquals(PageImpl.class, response.getBody().getClass())
				, () -> assertEquals(TagDTO.class, response.getBody().getContent().get(INDEX).getClass())
				, () -> assertEquals(ID, response.getBody().getContent().get(INDEX).getId())
				, () -> assertEquals(NAME, response.getBody().getContent().get(INDEX).getName()));
	}
	
	
	@Test
	@DisplayName("Should return a tag found by id")
	void whenFindByIdThenReturnSuccess() {
		when(tagService.findById(ID)).thenReturn(tag);
		when(mapper.map(any(), any())).thenReturn(tagDTO);
		
		ResponseEntity<TagDTO> response = tagController.findById(ID);
		
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertNotNull(response.getBody())
				, () -> assertEquals(HttpStatus.OK, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass())
				, () -> assertEquals(ID, response.getBody().getId())
				, () -> assertEquals(NAME, response.getBody().getName()));
	}
	
	
	@Test
	@DisplayName("Should delete a tag by id")
	void whenDeleteThenReturnNoContent() {
		doNothing().when(tagService).delete(anyLong());
		
		ResponseEntity<TagDTO> response = tagController.delete(ID);
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass()));
		verify(tagService, times(1)).delete(anyLong());
	}
	
	@Test
	@DisplayName("Should return update then return success")
	void whenUpdateThenReturnSuccess() {
		when(tagService.update(anyLong(), any())).thenReturn(tag);
		
		ResponseEntity<TagDTO> response = tagController.update(ID, tagDTO);
		assertAll(CHECK_IF_NOT_NULL_HTTP_STATUS_AND_RESPONSE_BODY
				, () -> assertNotNull(response)
				, () -> assertNotNull(response.getBody())
				, () -> assertEquals(HttpStatus.OK, response.getStatusCode())
				, () -> assertEquals(ResponseEntity.class, response.getClass())
				, () -> assertEquals(ID, response.getBody().getId())
				, () -> assertEquals(NAME, response.getBody().getName()));
		
	}
	
	
	// Inits Tag's instances 
	private void startTag() {
		tag = new Tag(ID, NAME, words);
		tagDTO = new TagDTO(ID, NAME, words);
		pageable = PageRequest.of(0, 10);
		page = new PageImpl<>(List.of(tag), pageable, 10);
	}

}
