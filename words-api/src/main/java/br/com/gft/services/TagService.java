package br.com.gft.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.gft.dto.TagDTO;
import br.com.gft.entities.Tag;
import br.com.gft.exceptions.DataIntegrityViolationException;
import br.com.gft.exceptions.ObjectNotFoundException;
import br.com.gft.repositories.TagRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {

	private static final String ETIQUETA_JA_CADASTRADA = "Etiqueta já cadastrada: ";

	private static final String ETIQUETA_NAO_ENCONTRADA = "Etiqueta não encontrada: ";

	private final TagRepository tagRepository;

	private final ModelMapper mapper;

	public Tag create(TagDTO obj) {
		Tag newTag = mapper.map(obj, Tag.class);
		return tagRepository.save(newTag);

	}

	public Page<Tag> findAll(Pageable pageable) {
		return tagRepository.findAll(pageable);
	}

	public Tag findById(Long id) {
		Optional<Tag> tag = tagRepository.findById(id);
		return tag.orElseThrow(() -> new ObjectNotFoundException(ETIQUETA_NAO_ENCONTRADA + id));
	}

	public void delete(Long id) {
		Optional<Tag> tag = tagRepository.findById(id);
		if (tag.isEmpty()) {
			throw new ObjectNotFoundException(ETIQUETA_NAO_ENCONTRADA);
		}
		tagRepository.deleteById(id);

	}

	public Tag update(Long id, TagDTO obj) {
		validUpdate(id, obj);
		obj.setId(id);
		return tagRepository.save(mapper.map(obj, Tag.class));

	}

	private void findByName(TagDTO obj) {
		Optional<Tag> tagSaved = tagRepository.findByNameIgnoreCase(obj.getName());
		if (tagSaved.isPresent()) {
			throw new DataIntegrityViolationException(ETIQUETA_JA_CADASTRADA + obj.getName());
		}
	}

	private void validUpdate(Long id, TagDTO obj) {
		findByName(obj);
		findById(id);
	}

}
