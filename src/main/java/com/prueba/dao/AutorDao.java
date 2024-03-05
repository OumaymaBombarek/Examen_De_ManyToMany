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

public interface AutorDao extends JpaRepository<Autor,Integer> {

    @Query("SELECT l.libros FROM Autor l WHERE l.id = :idAutor")
    List<Libro> findAllLibrosByAutorId(int idAutor);
   
     @Query(value = "select p from Autor p left join fetch p.libros", 
     countQuery = "select count(p) from Autor p left join p.libros")

    public Page<Autor> findAll(Pageable pageable);

    
    @Query(value = "select p from Autor p left join fetch p.libros")
    public List<Autor> findAll(Sort sort);
    
}
