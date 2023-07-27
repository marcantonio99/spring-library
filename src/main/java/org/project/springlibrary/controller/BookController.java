package org.project.springlibrary.controller;

import org.project.springlibrary.model.Book;
import org.project.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
}
