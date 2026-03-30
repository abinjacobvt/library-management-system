package com.example.library.controller;

import com.example.library.model.BorrowRequest;
import com.example.library.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BorrowController {

    @Autowired
    private BorrowRepository repo;

    // USER: Borrow request
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/borrow")
    public String borrow(Authentication auth, String bookTitle) {

        BorrowRequest br = new BorrowRequest();

        br.setUsername(auth.getName());
        br.setBookTitle(bookTitle);

        br.setStatus("PENDING");

        repo.save(br);

        return "redirect:/books";
    }

    // ADMIN: View requests
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/requests")
    public String requests(Model model) {
        model.addAttribute("requests", repo.findAll());
        return "admin";
    }

    // ADMIN: Approve request
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/approve/{id}")
    public String approve(@PathVariable Long id) {
        BorrowRequest br = repo.findById(id).orElseThrow();
        br.setStatus("APPROVED");
        repo.save(br);
        return "redirect:/admin/requests";
    }

    // ADMIN: Reject request (NEW FEATURE)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/reject/{id}")
    public String reject(@PathVariable Long id) {
        BorrowRequest br = repo.findById(id).orElseThrow();
        br.setStatus("REJECTED");
        repo.save(br);
        return "redirect:/admin/requests";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-requests")
    public String myRequests(Model model, Authentication auth) {

        String username = auth.getName();

        model.addAttribute("requests",
                repo.findByUsername(username));

        return "my-requests";
    }
}