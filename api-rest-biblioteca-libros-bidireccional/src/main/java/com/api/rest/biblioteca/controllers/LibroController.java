package com.api.rest.biblioteca.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.rest.biblioteca.entities.Biblioteca;
import com.api.rest.biblioteca.entities.Libro;
import com.api.rest.biblioteca.repositories.BibliotecaRepository;
import com.api.rest.biblioteca.repositories.LibroRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/libros")
public class LibroController {
    

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @PostMapping
    public ResponseEntity<Libro> guardarLibro(@Valid @RequestBody Libro libro){
    
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(libro.getBiblioteca().getId());
       
        
        if(!bibliotecaOptional.isPresent()){
            /*
             Si bibliotecaOptional no contiene un valor (es decir, la biblioteca no existe), 
             se devuelve una respuesta HTTP 422 (Unprocessable Entity) para indicar que la 
             solicitud es válida, pero no se puede procesar porque el recurso no existe.
             */
            return ResponseEntity.unprocessableEntity().build();
        }

        libro.setBiblioteca(bibliotecaOptional.get());
        Libro libroGuardado = libroRepository.save(libro);

        /*
         ServletUriComponentsBuilder.fromCurrentRequest(): Obtiene la URI actual de la solicitud, que es útil para construir una URI que apunte al nuevo recurso
         .path("/{id}"): Agrega un segmento a la URI, indicando que la nueva ubicación incluirá el ID del recurso
         .buildAndExpand(libroGuardado.getId()): Aquí, {id} se reemplaza con el ID del libro recién guardada. Esto asegura que la URI resultante sea única para el nuevo libro
         .toUri(): Convierte el objeto UriComponents en un objeto URI estándar de Java.
         */
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(libroGuardado.getId()).toUri();

        /*
         ResponseEntity.created(ubicacion): Crea una respuesta HTTP con el estado 201 (Created). Esto indica que se ha creado un nuevo recurso.
         .body(libroGuardado): Establece el cuerpo de la respuesta como el objeto libroGuardado que contiene los datos del libro
         recién creado. Esto permite al cliente ver qué datos se han guardado.
         */
        return ResponseEntity.created(ubicacion).body(libroGuardado);

        //URI significa Uniform Resource Identifier (Identificador Uniforme de Recursos). Es una cadena de caracteres que se utiliza para identificar un recurso en Internet de manera única
    }


    @PutMapping("{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @Valid @RequestBody Libro libro){
    
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(libro.getBiblioteca().getId());
       
        
        if(!bibliotecaOptional.isPresent()){
            /*
             Si bibliotecaOptional no contiene un valor (es decir, la biblioteca no existe), 
             se devuelve una respuesta HTTP 422 (Unprocessable Entity) para indicar que la 
             solicitud es válida, pero no se puede procesar porque el recurso no existe.
             */
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Libro> libroOptional = libroRepository.findById(id);

        if(!libroOptional.isPresent()){
            /*
             Si libroOptional no contiene un valor (es decir, el libro no existe), 
             se devuelve una respuesta HTTP 422 (Unprocessable Entity) para indicar que la 
             solicitud es válida, pero no se puede procesar porque el recurso no existe.
             */
            return ResponseEntity.unprocessableEntity().build();
        }



        libro.setBiblioteca(bibliotecaOptional.get()); //se obtiene la biblioteca
        libro.setId(libroOptional.get().getId());
        libroRepository.save(libro); //se guarda el libro


        /*
         Si la actualización se realiza correctamente, se devuelve una respuesta HTTP 204 (No Content),
         que indica que la solicitud se procesó correctamente pero no hay contenido que devolver.
         */
        return ResponseEntity.noContent().build();


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Libro> eliminarLibro(@PathVariable Long id){
        Optional<Libro> libroOptional = libroRepository.findById(id);

        if(!libroOptional.isPresent()){
             /*
             Si libroOptional no contiene un valor (es decir, el libro no existe), 
             se devuelve una respuesta HTTP 422 (Unprocessable Entity) para indicar que la 
             solicitud es válida, pero no se puede procesar porque el recurso no existe.
             */
            return ResponseEntity.unprocessableEntity().build();
        }

        libroRepository.delete(libroOptional.get()); //si lo encuentra, lo elimina
        
        /*
         Si la eliminación se realiza correctamente, se devuelve una respuesta HTTP 204 (No Content),
         que indica que la solicitud se procesó correctamente pero no hay contenido que devolver.
         */
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<Page<Libro>> listarLibros(Pageable pageable) {
        
        /* 
         devuelve un objeto ResponseEntity que contiene una página (Page) de objetos 
         Libro. ResponseEntity.ok() indica que la respuesta HTTP tendrá un código 
         de estado 200 (OK).


         libroRepository.findAll(pageable): Este método invoca al repositorio de libros 
         (libroRepository) para buscar todas las entradas en la base 
         de datos, aplicando paginación según lo especificado en el objeto Pageable
        */

        return ResponseEntity.ok(libroRepository.findAll(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Libro> listarLibro(@PathVariable Long id) {
        Optional<Libro> libroOptional = libroRepository.findById(id);

        if(!libroOptional.isPresent()){
             /*
             Si libroOptional no contiene un valor (es decir, el libro no existe), 
             se devuelve una respuesta HTTP 422 (Unprocessable Entity) para indicar que la 
             solicitud es válida, pero no se puede procesar porque el recurso no existe.
             */
            return ResponseEntity.unprocessableEntity().build();
        }

        /*
         Si encontro el libro, devuelve una respuesta HTTP con el estado 200 (OK) junto 
         con el objeto Libro encontrado.
         */
        return ResponseEntity.ok(libroOptional.get());
    }

}

