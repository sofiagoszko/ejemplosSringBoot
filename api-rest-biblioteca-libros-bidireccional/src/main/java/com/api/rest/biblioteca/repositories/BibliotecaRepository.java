package com.api.rest.biblioteca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.rest.biblioteca.entities.Biblioteca;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long>{

    void save(Optional<Biblioteca> bibliotecaOptional);
    
}
