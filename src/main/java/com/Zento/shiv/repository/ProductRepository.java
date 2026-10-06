package com.Zento.shiv.repository;

import com.Zento.shiv.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product , Long> {
}