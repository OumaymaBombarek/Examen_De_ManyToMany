package com.prueba.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.prueba.dao.AutorDao;
import com.prueba.entidades.Autor;
import com.prueba.entidades.Libro;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class autorServiceImpl implements AutorService{

    private final AutorDao autorDao;

    

    @Override
    public Autor findById(int idAutor) {
       return autorDao.findById(idAutor).get();
    }



    @Override
    public List<Libro> findAllLibrosByAutorId(int idAutor) {
        return autorDao.findAllLibrosByAutorId(idAutor);
    }



    @Override
    public Page<Autor> findAll(Pageable pageable) {

        return autorDao.findAll(pageable);
    }


    @Override
    public List<Autor> findAll(Sort sort) {
        return autorDao.findAll(sort);
    }



    @Override
    public Autor guardarAutor(Autor autor) {
      return autorDao.save(autor);
    }



    @Override
    public void eliminarAutor(int idAutor) {
        autorDao.deleteById(idAutor);
    }



    @Override
    public boolean autorExistente(int idAutor) {
        autorDao.existsById(idAutor);
        return true;
    }

}
