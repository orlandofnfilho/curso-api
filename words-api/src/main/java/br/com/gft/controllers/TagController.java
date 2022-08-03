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

import br.com.gft.controllers.docs.TagControllerDocs;
import br.com.gft.dto.TagDTO;
import br.com.gft.entities.Tag;
import br.com.gft.services.TagService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController implements TagControllerDocs {

	private final TagService tagService;
	private final ModelMapper mapper;

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<TagDTO> create(@RequestBody TagDTO obj) {
		Tag tag = tagService.create(obj);
		TagDTO response = mapper.map(tag, TagDTO.class);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tag.getId()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','USUARIO')")
	public ResponseEntity<TagDTO> findById(@PathVariable Long id) {
		TagDTO obj = mapper.map(tagService.findById(id), TagDTO.class);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMIN','USUARIO')")
	public ResponseEntity<Page<TagDTO>> findAll(
			@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<TagDTO> list = tagService.findAll(pageable).map(x -> mapper.map(x, TagDTO.class));
		return ResponseEntity.ok().body(list);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<TagDTO> update(@PathVariable Long id, @RequestBody TagDTO obj) {
		tagService.update(id, obj);
		return ResponseEntity.ok().body(obj);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<TagDTO> delete(@PathVariable Long id) {
		tagService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
