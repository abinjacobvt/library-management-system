package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {

    @Autowired
    private BookRepository repo;

    // LIST + SEARCH + PAGINATION
    @GetMapping("/books")
    public String books(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword
    ) {

        PageRequest pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Book> bookPage;

        if (keyword != null && !keyword.isEmpty()) {
            bookPage = repo.findByTitleContainingIgnoreCase(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            bookPage = repo.findAll(pageable);
        }

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());

        return "books";
    }

    // ADD BOOK
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/add")
    public String add(Book book) {
        repo.save(book);
        return "redirect:/books";
    }

    // DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/delete/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/books";
    }

    // UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/update/{id}")
    public String update(@PathVariable Long id, Book updatedBook) {
        Book book = repo.findById(id).orElseThrow();

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());

        repo.save(book);

        return "redirect:/books";
    }
}