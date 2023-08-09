package org.project.springlibrary.controller;

import jakarta.validation.Valid;
import org.project.springlibrary.model.Book;
import org.project.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public String list(@RequestParam(name = "keyword", required = false) String searchString, Model model) {
        List<Book> books;
        if (searchString == null || searchString.isBlank()) {
            //recupero la lista dei libri dal database
            books = bookRepository.findAll();
        } else {
            //se ho il parametro searchString faccio la query con filtro
            //books = bookRepository.findByTitle(searchString);
            books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorsContainingIgnoreCase(searchString, searchString);
        }
        //passo la lista dei libri alla view
        model.addAttribute("bookList", books);
        model.addAttribute("searchInput", searchString == null ? "" : searchString);
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

    //controller che restituisce la pagina con form di creazione del nuovo libro
    @GetMapping("/create")
    public String create(Model model) {
        //aggiungo al model l'attributo book contenente un book vuoto
        model.addAttribute("book", new Book());
        return "/books/create";
    }

    //controller che gestisce la post nel form con i dati del libro
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("book") Book formBook, BindingResult bindingResult) {
        //verifico che isbn sia univoco
        if (!isUniqueIsbn(formBook)) {
            bindingResult.addError(new FieldError("book", "isbn", formBook.getIsbn(),
                    false, null, null, "isbn è un valore univoco"));
        }
        //verifico se in validazione ci sono stati errori
        if (bindingResult.hasErrors()) {
            //se ci sono stati
            return "/books/create";
        }
        //setto il timestamp di creazione
        formBook.setCreatedAt(LocalDateTime.now());
        //persisto formBook sul database
        bookRepository.save(formBook);//il metodo save fa sql se l'oggetto non esiste, altrimenti fa upgrade
        return "redirect:/books";
    }

    private boolean isUniqueIsbn(Book formBook) {
        Optional<Book> result = bookRepository.findByIsbn(formBook.getIsbn());
        return result.isEmpty();
    }
}
