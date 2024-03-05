package com.prueba.Helppers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.prueba.entidades.Autor;
import com.prueba.entidades.Libro;
import com.prueba.servicio.AutorService;
import com.prueba.servicio.LibroService;


@Configuration
public class LoadSampleData {

     @Bean
    public CommandLineRunner saveSampleData(LibroService libroService, AutorService autorService){

        return datos -> {

            Autor autor1 = Autor.builder()
            .name("Juan")
            .build();

            Autor autor2 = Autor.builder()
            .name("Maria")
            .build();

            Autor autor3 = Autor.builder()
            .name("Layla")
            .build();

            Libro libro1 = Libro.builder()
                            .titulo("libro1")
                            .fechaPublicacion(LocalDate.of(2010, 02, 05))
                            .build();

            Libro libro2 = Libro.builder()
                            .titulo("libro2")
                            .fechaPublicacion(LocalDate.of(2011, 02, 05))
                            .build();

            Libro libro3 = Libro.builder()
                            .titulo("libro3")
                            .fechaPublicacion(LocalDate.of(2020, 06, 05))
                            .build();

            Libro libro4 = Libro.builder()
                            .titulo("libro2")
                            .fechaPublicacion(LocalDate.of(2008, 12, 05))
                            .build();

           
            autor1.setLibros(List.of(libro1,libro2));
            autor2.setLibros(List.of(libro3,libro4));
            autor3.setLibros(List.of(libro1,libro2,libro4));

            libro1.setAutores(List.of(autor1,autor3));
            libro2.setAutores(List.of(autor1,autor3));
            libro3.setAutores(List.of(autor2));
            libro4.setAutores(List.of(autor2,autor3));

            autorService.guardarAutor(autor1);
            autorService.guardarAutor(autor2);
            autorService.guardarAutor(autor3);

            libroService.saveLibro(libro1);
            libroService.saveLibro(libro2);
            libroService.saveLibro(libro3);
            libroService.saveLibro(libro4);

            
            


        };
    }

}
