package org.project.springlibrary.controller;

import org.project.springlibrary.model.Book;
import org.project.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public String list(Model model) {
        //recupero la lista dei libri dal database
        List<Book> books = bookRepository.findAll();
        //passo la lista dei libri alla view
        model.addAttribute("bookList", books);
        //restituisco il nome template della view
        return "/books/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Integer bookId, Model model) {
        //cerco su database dettagli del libro con quell'id
        Optional<Book> result = bookRepository.findById(bookId);
        if (result.isPresent()) {
            //passo il libro alla view
            System.out.println(result.get().getFormattedCreatedAt());
            model.addAttribute("book", result.get());
            //ritorna il nome del template della view
            return "/books/detail";
        } else {
            //ritorno un 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Book with id" + bookId + "not found");
        }
    }
}
