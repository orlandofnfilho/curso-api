package br.com.gft.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.gft.entities.Word;

@Service
public interface WordRepository extends JpaRepository<Word, Long> {

	Optional<Word> findByWordIgnoreCase(String word);

}
