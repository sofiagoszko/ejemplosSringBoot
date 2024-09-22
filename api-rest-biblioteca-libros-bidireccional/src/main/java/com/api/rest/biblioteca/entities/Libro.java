/*
 * 
Paquete y dependencias: La clase está en el paquete com.api.rest.biblioteca.entities y utiliza varias bibliotecas, como Jackson para la serialización/deserialización de 
JSON y Jakarta Persistence para la gestión de entidades.

Anotaciones:

@Data: Proporcionada por Lombok, genera automáticamente métodos como getters, setters, toString, equals, y hashCode.

@Entity: Indica que esta clase es una entidad JPA que se mapea a una tabla en la base de datos.

@Table(name = "libros"): Especifica el nombre de la tabla en la base de datos.

@Id: Indica que el campo id es la clave primaria.

@GeneratedValue(strategy = GenerationType.IDENTITY): Define que el valor de id se generará automáticamente (usualmente por la base de datos).

@NotNull: Indica que el campo nombre no puede ser nulo.

@ManyToOne: Define una relación de muchos a uno con la entidad Biblioteca.

@JoinColumn(name = "biblioteca_id"): Especifica la columna de la tabla que actúa como clave foránea.

@JsonProperty(access = Access.WRITE_ONLY): Indica que el campo biblioteca solo se puede escribir (no se incluirá en la serialización a JSON).

Carga diferida: El uso de FetchType.LAZY en la relación con Biblioteca significa que los datos de la biblioteca solo se cargarán de la base de datos cuando se acceda 
explícitamente a ellos.

 */


package com.api.rest.biblioteca.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "libros", uniqueConstraints = {@UniqueConstraint(columnNames = {"nombre"})}) //el nombre del libro es unico y no se puede repetir
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //muchos libros pertenecen a una biblioteca
    @JoinColumn(name = "biblioteca_id")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Biblioteca biblioteca;

}
