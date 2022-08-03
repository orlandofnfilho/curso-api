package br.com.gft.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gft.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
	
	Optional<Tag> findByNameIgnoreCase(String name);

	Page<Tag> findAll(Pageable pageable);

}
