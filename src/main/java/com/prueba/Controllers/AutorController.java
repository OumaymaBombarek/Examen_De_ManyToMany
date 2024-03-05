package com.prueba.Controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.entidades.Autor;
import com.prueba.entidades.Libro;
import com.prueba.servicio.AutorService;
import com.prueba.servicio.LibroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class AutorController {

    private final LibroService libroService;
    private final AutorService autorService;

    // Cuestion 1:

    @PostMapping("/libros/{id}/autores")
    public ResponseEntity<Map<String, Object>> añadirAutor(@PathVariable(value = "id") Integer idLibro,
                                                           @RequestBody Autor autorRequest) {
    
        Map<String, Object> responseAsMap = new HashMap<>();
    
        try {
            Optional<Libro> optionalLibro = libroService.findById(idLibro);
    
            if (optionalLibro.isPresent()) {
                Libro libro = optionalLibro.get();
                int autorId = autorRequest.getId();
    
                if (autorId != 0) {
                    Autor autor = autorService.findById(autorId);
    
                    if (autor != null) {
                        libro.addHijo(autor);
                        libroService.saveLibro(libro);
    
                        String successMessage = "El autor se ha añadido al libro";
    
                        responseAsMap.put("libroResponse", libro);
                        responseAsMap.put("AutorResponse", autor);
                        responseAsMap.put("successMessage", successMessage);
    
                        return new ResponseEntity<>(responseAsMap, HttpStatus.ACCEPTED);
                    } else {
                        responseAsMap.put("Message", "Id autor es invalido");
                        return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    responseAsMap.put("Message", "libro not found");
                    return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            responseAsMap.put("Message", "Ocurrió algún error al añadir un autor al libro");
            return new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
        responseAsMap.put("Message", "Libro no encontrado");
        return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
    }


    // Cuestion2:
    @GetMapping("libros/{id}/autores")
    public ResponseEntity<Map<String, Object>> encontrarAutores(@PathVariable(value = "id", required = true) Integer idLibro){

        Map<String, Object> responseAsMap = new HashMap<>();

        try {

            List<Autor> autoresDeLibro = libroService.findAllAutoresByLibrosId(idLibro);
            if (!autoresDeLibro.isEmpty()) {
                responseAsMap.put("autores de Libro", autoresDeLibro);
                return new ResponseEntity<>(responseAsMap, HttpStatus.OK);
            } else {

                responseAsMap.put("Message", "No se encontraron autores para el libro con ID: " + idLibro);
                return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);

            }
            
        } catch (Exception e) {
            responseAsMap.put("Message", "Ocurrió un error al buscar autores para el libro con ID: " + idLibro);
            return new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            
        }



    }

    // cuestion 3
    @GetMapping("autores/{id}/libros")
    public ResponseEntity<Map<String, Object>> encontrarLibros(@PathVariable(value = "id", required = true) Integer idAutor){

        Map<String, Object> responseAsMap = new HashMap<>();

        try {

            List<Libro> librosDeAutor = autorService.findAllLibrosByAutorId(idAutor);
            if (!librosDeAutor.isEmpty()) {
                responseAsMap.put("autores de Libro", librosDeAutor);
                return new ResponseEntity<>(responseAsMap, HttpStatus.OK);
            } else {

                responseAsMap.put("Message", "No se encontraron los libros para el autor con ID: " + idAutor);
                return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);

            }
            
        } catch (Exception e) {
            responseAsMap.put("Message", "Ocurrió un error al buscar libros para el Autor con ID: " + idAutor);
            return new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        
    }

    //6- Recuperar todos los autores, con paginación y ordenamiento(Opcionalmente)


    @GetMapping("/autores")
    public ResponseEntity<List<Autor>> findAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<Autor>> responseEntity = null;
        Sort sortByName = Sort.by("name");
        List<Autor> autores = new ArrayList<>();

        // Comprobamos si han enviado page y size
        if (page != null && size != null) {
            // Queremos devolver los productos paginados
            Pageable pageable = PageRequest.of(page, size, sortByName);
            Page<Autor> pageAutor = autorService.findAll(pageable);
            autores = pageAutor.getContent();
            responseEntity = new ResponseEntity<List<Autor>>(autores, HttpStatus.OK);
        } else {
            // Solo ordenamiento

            autores = autorService.findAll(sortByName);
            responseEntity = new ResponseEntity<List<Autor>>(autores, HttpStatus.OK);

        }

        return responseEntity;
    }

    // 7-Encontrar un autor mediante su id

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findAutorById(
            @PathVariable(name = "id", required = true) Integer idAutor)  {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        try {
            Autor autor = autorService.findById(idAutor);

            if (autor != null) {
                String successMessage = "Autor con id " + idAutor + ", encontrado";
                responseAsMap.put("successMessage", successMessage);
                responseAsMap.put("Autor", autor);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
            } else {
                String errorMessage = "Autor con id " + idAutor + ", no encontrado";
                responseAsMap.put("error message", errorMessage);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e) {
            String error = "Se ha producido un error al buscar el autor con id " + idAutor +
                    ", y la causa mas probable es: " + e.getMostSpecificCause();
            responseAsMap.put("errorGrave", error);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return responseEntity;
    }

    // 8-Actualizar un autor 

      @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarAutor( @RequestBody  Autor autor,
            @PathVariable(name = "id", required = true) Integer idAutor) {

                Map<String, Object> responseAsMap = new HashMap<>();
                ResponseEntity<Map<String, Object>> responseEntity = null;
        
                try {
                    autor.setId(idAutor);
                    Autor autorActualizado = autorService.guardarAutor(autor);
                    String successMessage = "El autor se ha actualizado exitosamente ";
                    responseAsMap.put("Success Message", successMessage);
                    responseAsMap.put("autor Actualizado", autorActualizado);
                    responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
                } catch (DataAccessException e) {
                    String error = "Error al intentar actualizar el autor y la causa mas probable es: "
                            + e.getMostSpecificCause();
                    responseAsMap.put("error", error);
                    responseAsMap.put("Producto que se ha intentado actualizar", autor);
                    responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
                }
        
                return responseEntity;
        
            }


    // 9-Eliminar el autor por su Id

     @DeleteMapping("autores/{id}")
    public ResponseEntity<Map<String, Object>> deleteAutorById(
            @PathVariable(name = "id", required = true) Integer idAutor) {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        try {
           autorService.eliminarAutor(idAutor);;
            String successMessage = "Autor con id " + idAutor + ", eliminado exitosamente";
            responseAsMap.put("successMessage", successMessage);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
        } catch (DataAccessException e) {
            String error = "Error grave al intentar eliminar el autor con id " + idAutor
                    + ", y la causa mas probable es: " + e.getMostSpecificCause();
            responseAsMap.put("errorGrave", error);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
    
    // 11- Eliminar un libro correspondiente a unautor por su id

    @GetMapping("autores/{id}/fechaPublicacion/{fecha}")
    public ResponseEntity<Map<String, Object>> recuperarPorFecha(@PathVariable(value = "id", required = true) Integer idAutor,
    @PathVariable(name = "fecha", required = true) LocalDate fechaIndicada){

        Map<String, Object> responseAsMap = new HashMap<>();

        try {

            if (autorService.autorExistente(idAutor)){

            List<Libro> librosDeUnAutor = autorService.findAllLibrosByAutorId(idAutor);
            List<Libro> listfiltrado=  librosDeUnAutor.stream().filter(libro -> libro.getFechaPublicacion().isAfter(fechaIndicada))
            .collect(Collectors.toList());
            responseAsMap.put("autores de Libro", listfiltrado);
            return new ResponseEntity<>(responseAsMap, HttpStatus.OK);
            } else{
                responseAsMap.put("message", "Autor no encontrado.");
                return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
            }


        
        } catch (Exception e) {
            responseAsMap.put("Message", "Error");
            return new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }



      
    }







                
}




           

        


            