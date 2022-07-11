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

import br.com.gft.dto.WordDTO;
import br.com.gft.entities.Word;
import br.com.gft.services.WordService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/words")
public class WordController {

	private final WordService wordService;
	private final ModelMapper mapper;

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<WordDTO> create(@RequestBody WordDTO obj) {
		Word word = wordService.create(obj);
		WordDTO response = mapper.map(word, WordDTO.class);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(word.getId()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<WordDTO> findById(@PathVariable Long id) {
		WordDTO obj = mapper.map(wordService.findById(id), WordDTO.class);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USUARIO')")
	public ResponseEntity<Page<WordDTO>> findAll(
			@PageableDefault(size = 10, sort = "word", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<WordDTO> list = wordService.findAll(pageable).map(x -> mapper.map(x, WordDTO.class));
		return ResponseEntity.ok().body(list);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<WordDTO> update(@PathVariable Long id, @RequestBody WordDTO obj) {
		wordService.update(id, obj);
		return ResponseEntity.ok().body(obj);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<WordDTO> delete(@PathVariable Long id) {
		wordService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
