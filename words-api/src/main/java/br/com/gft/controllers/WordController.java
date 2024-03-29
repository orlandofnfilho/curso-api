package br.com.gft.controllers;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.gft.controllers.docs.WordControllerDocs;
import br.com.gft.dto.word.QueryWordDTO;
import br.com.gft.dto.word.RegisterWordDTO;
import br.com.gft.entities.Word;
import br.com.gft.services.WordService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/words")
public class WordController implements WordControllerDocs {

	private final WordService wordService;
	private final ModelMapper mapper;

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<QueryWordDTO> create(@RequestBody RegisterWordDTO obj) {
		Word word = wordService.create(obj);
		QueryWordDTO response = mapper.map(word, QueryWordDTO.class);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(word.getId()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<QueryWordDTO> findById(@PathVariable Long id) {
		QueryWordDTO obj = mapper.map(wordService.findById(id), QueryWordDTO.class);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USUARIO')")
	public ResponseEntity<Page<QueryWordDTO>> findAll(
			@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<QueryWordDTO> list = wordService.findAll(pageable).map(x -> mapper.map(x, QueryWordDTO.class));
		return ResponseEntity.ok().body(list);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<QueryWordDTO> update(@PathVariable Long id, @RequestBody RegisterWordDTO obj) {
		Word word = wordService.update(id, obj);
		return ResponseEntity.ok().body(mapper.map(word, QueryWordDTO.class));

	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<QueryWordDTO> delete(@PathVariable Long id) {
		wordService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
