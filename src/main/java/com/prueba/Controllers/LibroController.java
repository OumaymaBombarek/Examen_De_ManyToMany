package com.prueba.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.entidades.Autor;
import com.prueba.entidades.Libro;
import com.prueba.servicio.AutorService;
import com.prueba.servicio.LibroService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class LibroController {

    private final LibroService libroService;
    private final AutorService autorService;

    // El metodo responde a una request del tipo
    // http://localhost:8080/productos?page=0&size=3
    // Si no se especifica page y size entonces que devuelva los productos ordenados

    // 4-por el nombre, por ejemplo

    @GetMapping("/libros")
    public ResponseEntity<List<Libro>> findAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<Libro>> responseEntity = null;
        Sort sortByName = Sort.by("titulo");
        List<Libro> libros = new ArrayList<>();

        // Comprobamos si han enviado page y size
        if (page != null && size != null) {
            // Queremos devolver los productos paginados
            Pageable pageable = PageRequest.of(page, size, sortByName);
            Page<Libro> pageLibro = libroService.findAll(pageable);
            libros = pageLibro.getContent();
            responseEntity = new ResponseEntity<List<Libro>>(libros, HttpStatus.OK);
        } else {
            // Solo ordenamiento

            libros = libroService.findAll(sortByName);
            responseEntity = new ResponseEntity<List<Libro>>(libros, HttpStatus.OK);

        }

        return responseEntity;
    }

    // 5-Recuperar un libro con sus autores 

    @GetMapping("libros/{id}")
    public ResponseEntity<Map<String, Object>> encontrarLibrosConAutores(
        @PathVariable(value = "id", required = true) Integer idLibro){

            Map<String, Object> responseAsMap = new HashMap<>();

            try {
                List<Libro> librosConAutores = libroService.findLibroWithAutoresById(idLibro);
                if (!librosConAutores.isEmpty()) {
                    responseAsMap.put("Libros con sus autores", librosConAutores);
                    return new ResponseEntity<>(responseAsMap, HttpStatus.OK);
                } else{
                    responseAsMap.put("Message", "No se encontraron libros con ID: " + idLibro);
                    return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);

                }
            } catch (Exception e) {
                
                responseAsMap.put("Message", "Ocurrió un error al buscar los libros con ID: " + idLibro);
                return new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }


        }

        //10- Eliminar un libro por su id

    
    @DeleteMapping("libros/{id}")
    public ResponseEntity<Map<String, Object>> deleteLibroById(
            @PathVariable(name = "id", required = true) Integer idLibro){

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        try {
           libroService.eliminarLibro(idLibro);
            String successMessage = "Libro con id " + idLibro + ", eliminado exitosamente";
            responseAsMap.put("successMessage", successMessage);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
        } catch (DataAccessException e) {
            String error = "Error grave al intentar eliminar el libro con id " + idLibro
                    + ", y la causa mas probable es: " + e.getMostSpecificCause();
            responseAsMap.put("errorGrave", error);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;


    }

    @DeleteMapping("libros/{idLibro}/autores/{idAutor}")

public ResponseEntity<Map<String, Object>> deleteAutorFromLibro(
    @PathVariable(name = "idLibro", required = true) Integer idLibro,
    @PathVariable(name = "idAutor", required = true) Integer idAutor){

    Map<String, Object> responseAsMap = new HashMap<>();

    try {
 
        if (autorService.autorExistente(idAutor) && libroService.libroExistente(idLibro)) {

            Optional<Libro> optionalLibro = libroService.findById(idLibro);
            Autor autor = autorService.findById(idAutor);

            if (optionalLibro.isPresent()) {

                Libro libro = optionalLibro.get();

               
                if (libro.getAutores().contains(autor)) {
                    libro.removeAutor(idAutor);  
                    libroService.saveLibro(libro);
                    responseAsMap.put("message", "Autor eliminado exitosamente del libro.");
                    return new ResponseEntity<>(responseAsMap, HttpStatus.OK);
                } else {
                    responseAsMap.put("message", "El autor no está asociado al libro.");
                    return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
                }
            } else {
                responseAsMap.put("message", "Libro no encontrado.");
                return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
            }
        } else {
            responseAsMap.put("message", "Autor o libro no encontrado.");
            return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        responseAsMap.put("message", "Ocurrió un error al intentar eliminar el autor del libro.");
        return new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}










}
