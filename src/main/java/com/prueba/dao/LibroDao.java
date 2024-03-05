package com.prueba.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import com.prueba.entidades.Autor;
import com.prueba.entidades.Libro;

@Repository

public interface LibroDao extends JpaRepository<Libro,Integer> {

     @Query("SELECT l.autores FROM Libro l WHERE l.id = :idLibro")
    List<Autor> findAllAutoresByLibroId(int idLibro);

    @Query(value = "select p from Libro p left join fetch p.autores", 
     countQuery = "select count(p) from Libro p left join p.autores")

    public Page<Libro> findAll(Pageable pageable);

    
    @Query(value = "select p from Libro p left join fetch p.autores")
    public List<Libro> findAll(Sort sort);

    //@Query("SELECT DISTINCT l FROM Libro l LEFT JOIN FETCH l.autores WHERE l.id = :idLibro")
    List<Libro> findLibroWithAutoresById(int idLibro);
}
