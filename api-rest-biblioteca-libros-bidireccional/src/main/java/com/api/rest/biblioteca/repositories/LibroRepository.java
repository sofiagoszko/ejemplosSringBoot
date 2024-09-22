package com.api.rest.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.rest.biblioteca.entities.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long>{
    
}
