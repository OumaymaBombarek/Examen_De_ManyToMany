package com.prueba.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "libro")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Libro  implements Serializable {

      private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;

    private LocalDate fechaPublicacion;


    @JoinTable(name = "libro_autor",
    joinColumns = { @JoinColumn(name = "autor_id") },
    inverseJoinColumns = { @JoinColumn(name = "libro_id") })


    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    private List <Autor> autores;

    public void addHijo(Autor autor) {
        this.autores.add(autor);
        autor.getLibros().add(this);
    }
    
    public void removeAutor(int autorId) {
         Autor autor = this.autores.stream().filter(e -> e.getId() == autorId).findFirst().orElse(null);
        if (autor != null) {
            this.autores.remove(autor);
            autor.getLibros().remove(this);
    
        }

}

}
