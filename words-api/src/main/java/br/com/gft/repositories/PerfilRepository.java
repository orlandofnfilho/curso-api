package br.com.gft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gft.entities.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>{

}
