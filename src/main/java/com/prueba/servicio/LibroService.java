package com.prueba.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.prueba.entidades.Autor;
import com.prueba.entidades.Libro;

public interface LibroService {

   Optional<Libro> findById(int idLibro);

   void saveLibro(Libro libro);

   List<Autor> findAllAutoresByLibrosId(int idLibro);

   Page<Libro> findAll(Pageable pageable);
   public List<Libro> findAll(Sort sort);

   List<Libro> findLibroWithAutoresById(int idLibro);

   void eliminarLibro (int idLibro);

   boolean libroExistente(int idLibro);

   Libro guardarLibro(Libro libro);

}
