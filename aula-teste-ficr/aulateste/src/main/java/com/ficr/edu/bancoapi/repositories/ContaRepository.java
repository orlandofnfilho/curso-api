package com.ficr.edu.bancoapi.repositories;

import com.ficr.edu.bancoapi.entities.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
}
