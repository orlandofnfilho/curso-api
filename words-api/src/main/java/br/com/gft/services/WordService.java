package br.com.gft.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.gft.dto.WordDTO;
import br.com.gft.entities.Word;
import br.com.gft.repositories.WordRepository;
import br.com.gft.services.exceptions.DataIntegratyViolationException;
import br.com.gft.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WordService {

	private final WordRepository wordRepository;

	private final ModelMapper mapper;

	public Word create(WordDTO obj) {
		Word newWord = mapper.map(obj, Word.class);
		findByWord(obj);
		return wordRepository.save(newWord);

	}

	public List<Word> findAll() {
		return wordRepository.findAll();
	}

	public Word findById(Long id) {
		Optional<Word> word = wordRepository.findById(id);
		return word.orElseThrow(() -> new ObjectNotFoundException("Palavra não encontrada" + id));
	}

	public void delete(Long id) {
		Optional<Word> word = wordRepository.findById(id);
		if (word.isEmpty()) {
			throw new ObjectNotFoundException("Palavra não encontrada");
		}
		wordRepository.deleteById(id);

	}

	public Word update(Long id, WordDTO obj) {
		validUpdate(id, obj);
		obj.setId(id);
		return wordRepository.save(mapper.map(obj, Word.class));

	}

	private void findByWord(WordDTO obj) {
		Optional<Word> wordSaved = wordRepository.findByWordIgnoreCase(obj.getWord());
		if (wordSaved.isPresent()) {
			throw new DataIntegratyViolationException("Palavra já cadastrada");
		}
	}

	private void validUpdate(Long id, WordDTO obj) {
		Optional<Word> wordSaved = wordRepository.findByWordIgnoreCase(obj.getWord());
		Optional<Word> wordFoundById = wordRepository.findById(id);
		if (wordSaved.isPresent()) {
			throw new DataIntegratyViolationException("Palavra já cadastrada");
		}
		if (wordFoundById.isEmpty()) {
			throw new ObjectNotFoundException("Palavra não encontrada");
		}
	}

}
