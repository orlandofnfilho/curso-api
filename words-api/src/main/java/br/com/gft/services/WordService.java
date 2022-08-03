package br.com.gft.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.gft.dto.word.RegisterWordDTO;
import br.com.gft.entities.Tag;
import br.com.gft.entities.Word;
import br.com.gft.exceptions.DataIntegrityViolationException;
import br.com.gft.exceptions.ObjectNotFoundException;
import br.com.gft.repositories.TagRepository;
import br.com.gft.repositories.WordRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WordService {

    private static final String PALAVRA_JA_CADASTRADA = "Palavra já cadastrada: ";

	private static final String PALAVRA_NAO_ENCONTRADA = "Palavra não encontrada: ";

	private final WordRepository wordRepository;
	
	private final TagRepository tagRepository;

    private final ModelMapper mapper;
    
    private final TagService tagService;

    public Word create(RegisterWordDTO obj) {
        findByName(obj);
    	
    	Word newWord = mapper.map(obj, Word.class);
    	
    	linkExistingTags(newWord);
    	
        return wordRepository.save(newWord);

    }

	public Page<Word> findAll(Pageable pageable) {
        return wordRepository.findAll(pageable);
    }

    public Word findById(Long id) {
        Optional<Word> word = wordRepository.findById(id);
        return word.orElseThrow(() -> new ObjectNotFoundException(PALAVRA_NAO_ENCONTRADA + id));
    }

    public void delete(Long id) {
    	findById(id);
        wordRepository.deleteById(id);

    }

    public Word update(Long id, RegisterWordDTO obj) {
    	Word foundWord = findById(id);
    	
    	if (!foundWord.getName().equals(obj.getName())) {
    		findByName(obj);
    		foundWord.setName(obj.getName());
    	}
    		        
        linkExistingTags(foundWord);
        
        return wordRepository.save(foundWord);

    }
    
	private void findByName(RegisterWordDTO obj) {
		Optional<Word> wordgSaved = wordRepository.findByNameIgnoreCase(obj.getName());
		if (wordgSaved.isPresent()) {
			throw new DataIntegrityViolationException(PALAVRA_JA_CADASTRADA + obj.getName());
		}
	}
	
	private void linkExistingTags(Word word) {
		word.setTags(word.getTags().stream()
				.map(this::checkExistingTag)
				.collect(Collectors.toSet()));
    	
    	word.getTags().forEach(tag -> tag.addWord(word, true));
	}
	
	private Tag checkExistingTag(Tag tag) {
		Tag linkTag = new Tag();
		
		if(tag.getId() != null && tag.getId() == 0) tag.setId(null);
		
		if(tag.getId() != null) {
			linkTag = tagService.findById(tag.getId());
		} else {
			Optional<Tag> optionalTag = tagRepository.findByNameIgnoreCase(tag.getName());
			if(optionalTag.isPresent()) {
				linkTag = optionalTag.get();
			}
		}
		
		if(linkTag.getId() != null) {
			return linkTag;
		}
		
		return tag;
	}

}