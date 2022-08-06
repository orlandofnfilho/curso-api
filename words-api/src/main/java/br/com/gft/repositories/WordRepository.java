package br.com.gft.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gft.entities.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

	Optional<Word> findByNameIgnoreCase(String name);

	Page<Word> findAll(Pageable pageable);

}
