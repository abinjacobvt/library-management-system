package com.example.library.repository;

import com.example.library.model.BorrowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface BorrowRepository extends JpaRepository<BorrowRequest, Long> {
    List<BorrowRequest> findByUsername(String username);
}