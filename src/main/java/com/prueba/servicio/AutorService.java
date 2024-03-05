package com.prueba.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.prueba.entidades.Autor;
import com.prueba.entidades.Libro;

public interface AutorService {

    Autor findById(int idAutor);
    List<Libro> findAllLibrosByAutorId(int idAutor);
    public Page<Autor> findAll(Pageable pageable);
    public List<Autor> findAll(Sort sort);
    Autor guardarAutor(Autor autor);
    void eliminarAutor (int idAutor);
    boolean autorExistente(int idAutor);

}
