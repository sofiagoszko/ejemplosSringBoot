/*
1. Paquete y dependencias
La clase está en el paquete com.api.rest.biblioteca.entities y utiliza las anotaciones de Jakarta Persistence (JPA) y Lombok.

2. Anotaciones y atributos
@Data
Proporcionada por Lombok, genera automáticamente métodos como getters, setters, toString, equals, y hashCode.

@Entity
Indica que esta clase es una entidad JPA, lo que significa que se puede mapear a una tabla en la base de datos.

@Table(name = "biblioteca")
Especifica el nombre de la tabla en la base de datos que se asociará con esta entidad.

3. Atributos de la entidad
@Id
Indica que el campo id es la clave primaria de la entidad.

@GeneratedValue(strategy = GenerationType.IDENTITY)
Define que el valor del campo id se generará automáticamente, generalmente por la base de datos.

@NotNull
Indica que el campo nombre no puede ser nulo. Esto asegura que cada biblioteca tenga un nombre.

@OneToMany(mappedBy = "biblioteca", cascade = CascadeType.ALL)
Relación: Esto indica que una Biblioteca puede tener muchos Libro.
mappedBy: El atributo mappedBy especifica que el lado propietario de la relación es la entidad Libro, que tiene la propiedad biblioteca. Esto es necesario para que JPA entienda cómo se mapea la relación.
cascade = CascadeType.ALL: Esto significa que cualquier operación (persistir, eliminar, etc.) que se realice en una Biblioteca también se aplicará a los Libro asociados. Por ejemplo, si eliminas una biblioteca, todos los libros asociados a esa biblioteca también se eliminarán.

private Set<Libro> libros = new HashSet<>();
Este atributo mantiene un conjunto de libros que pertenecen a la biblioteca. Se utiliza un HashSet para evitar duplicados y para permitir un acceso eficiente.
 */




package com.api.rest.biblioteca.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
//import lombok.Data;

//@Data
@Entity
@Table(name = "biblioteca")
public class Biblioteca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @OneToMany(mappedBy = "biblioteca", cascade = CascadeType.ALL) //si elimino una biblioteca, se eliminan sus libros
    private Set<Libro> libros = new HashSet<>(); //set implica que no hay duplicados

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
        for(Libro libro : libros){
            libro.setBiblioteca(this);
        }
    }

}
