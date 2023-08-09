package org.project.springlibrary.repository;

import org.project.springlibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    //metodo per cercare i libri con titolo uguale a quello che gli viene passato
    List<Book> findByTitle(String title);

    //metodo per cercare i libri il cui titolo o autore contiene una stringa passata
    List<Book> findByTitleContainingIgnoreCaseOrAuthorsContainingIgnoreCase(String title, String authors);

    //metodo per cercare un libro che ha un isbn
    Optional<Book> findByIsbn(String isbn);
    
}
