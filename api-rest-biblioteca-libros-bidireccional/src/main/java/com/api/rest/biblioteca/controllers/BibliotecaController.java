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
import com.api.rest.biblioteca.repositories.BibliotecaRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {
    

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @PostMapping
    public ResponseEntity<Biblioteca> guardarBiblioteca(@Valid @RequestBody Biblioteca biblioteca){
    
        Biblioteca bibliotecaGuardada = bibliotecaRepository.save(biblioteca); //El objeto biblioteca se guarda en la base de datos usando el método save del bibliotecaRepository

        /*
         ServletUriComponentsBuilder.fromCurrentRequest(): Obtiene la URI actual de la solicitud, que es útil para construir una URI que apunte al nuevo recurso
         .path("/{id}"): Agrega un segmento a la URI, indicando que la nueva ubicación incluirá el ID del recurso
         .buildAndExpand(bibliotecaGuardada.getId()): Aquí, {id} se reemplaza con el ID de la biblioteca recién guardada. Esto asegura que la URI resultante sea única para la nueva biblioteca
         .toUri(): Convierte el objeto UriComponents en un objeto URI estándar de Java.
         */
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bibliotecaGuardada.getId()).toUri();

        /*
         ResponseEntity.created(ubicacion): Crea una respuesta HTTP con el estado 201 (Created). Esto indica que se ha creado un nuevo recurso.
         .body(bibliotecaGuardada): Establece el cuerpo de la respuesta como el objeto bibliotecaGuardada, que contiene los datos de la biblioteca 
         recién creada. Esto permite al cliente ver qué datos se han guardado.
         */
        return ResponseEntity.created(ubicacion).body(bibliotecaGuardada);

        //URI significa Uniform Resource Identifier (Identificador Uniforme de Recursos). Es una cadena de caracteres que se utiliza para identificar un recurso en Internet de manera única
    }

    @PutMapping("/{id}")
    public ResponseEntity<Biblioteca> actualizarBiblioteca(@PathVariable Long id, @Valid @RequestBody Biblioteca biblioteca){
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id);

        if(!bibliotecaOptional.isPresent()){
            /*
             Si bibliotecaOptional no contiene un valor (es decir, la biblioteca no existe), 
             se devuelve una respuesta HTTP 422 (Unprocessable Entity) para indicar que la 
             solicitud es válida, pero no se puede procesar porque el recurso no existe.
             */
            return ResponseEntity.unprocessableEntity().build();
        }

        /*
         Aquí se establece el ID de la biblioteca que se va a actualizar. Esto asegura que la
         entidad que se está guardando tiene el mismo ID que la biblioteca existente.
         */
        biblioteca.setId(bibliotecaOptional.get().getId());
        bibliotecaRepository.save(biblioteca);

        /*
         Si la actualización se realiza correctamente, se devuelve una respuesta HTTP 204 (No Content),
         que indica que la solicitud se procesó correctamente pero no hay contenido que devolver.
         */
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Biblioteca> eliminarBiblioteca(@PathVariable Long id){
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id);

        if(!bibliotecaOptional.isPresent()){
            /*
             Si bibliotecaOptional no contiene un valor (es decir, la biblioteca no existe), 
             se devuelve una respuesta HTTP 422 (Unprocessable Entity) para indicar que la 
             solicitud es válida, pero no se puede procesar porque el recurso no existe.
             */
            return ResponseEntity.unprocessableEntity().build();
        }

        bibliotecaRepository.delete(bibliotecaOptional.get()); //si lo encuentra, lo elimina
        
        /*
         Si la eliminación se realiza correctamente, se devuelve una respuesta HTTP 204 (No Content),
         que indica que la solicitud se procesó correctamente pero no hay contenido que devolver.
         */
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Biblioteca> obtenerBibliotecaPorId(@PathVariable Long id) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id);

        if(!bibliotecaOptional.isPresent()){
            /*
             Si bibliotecaOptional no contiene un valor (es decir, la biblioteca no existe), 
             se devuelve una respuesta HTTP 422 (Unprocessable Entity) para indicar que la 
             solicitud es válida, pero no se puede procesar porque el recurso no existe.
             */
            return ResponseEntity.unprocessableEntity().build();
        }

        /*
         Si encontro la biblioteca, devuelve una respuesta HTTP con el estado 200 (OK) junto 
         con el objeto Biblioteca encontrado.
         */
        return ResponseEntity.ok(bibliotecaOptional.get());
    }

    /*
     devuelve una lista paginada de bibliotecas. Recibe un objeto de tipo Pageable como 
     parámetro, que contiene información sobre la paginación
     */
    @GetMapping
    public ResponseEntity<Page<Biblioteca>> listarBibliotecas(Pageable pageable) {
        
        /* 
         devuelve un objeto ResponseEntity que contiene una página (Page) de objetos 
         Biblioteca. ResponseEntity.ok() indica que la respuesta HTTP tendrá un código 
         de estado 200 (OK).


         bibliotecaRepository.findAll(pageable): Este método invoca al repositorio de 
         bibliotecas (bibliotecaRepository) para buscar todas las entradas en la base 
         de datos, aplicando paginación según lo especificado en el objeto Pageable
        */

        return ResponseEntity.ok(bibliotecaRepository.findAll(pageable));
    }
    
}
