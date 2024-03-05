package com.prueba.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.prueba.dao.LibroDao;
import com.prueba.entidades.Autor;
import com.prueba.entidades.Libro;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LibroServiceImpl implements LibroService{

    private final LibroDao libroDao;

    @Override
    public Optional<Libro> findById(int idLibro) {
        return libroDao.findById(idLibro);
    }

    @Override
    public void saveLibro(Libro libro) {
       libroDao.save(libro);
    }


    @Override
    public List<Autor> findAllAutoresByLibrosId(int idLibro) {
      return libroDao.findAllAutoresByLibroId(idLibro);
    }

    @Override
    public Page<Libro> findAll(Pageable pageable) {
   
      return libroDao.findAll(pageable);
    }

    @Override
    public List<Libro> findAll(Sort sort) {
      
      return libroDao.findAll(sort);
    }

    @Override
    public List<Libro> findLibroWithAutoresById(int idLibro) {
     
     return libroDao.findLibroWithAutoresById(idLibro) ;
    }

    @Override
    public void eliminarLibro(int idLibro) {
      
      libroDao.deleteById(idLibro);;
    }

    @Override
    public boolean libroExistente(int idLibro) {

      libroDao.existsById(idLibro);
        return true;


    }

    @Override
    public Libro guardarLibro(Libro libro) {
      return libroDao.save(libro);
    }

   
    

  



}