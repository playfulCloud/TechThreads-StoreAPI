package com.playfulCloud.cruddemo.product.repository;

import com.playfulCloud.cruddemo.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.title LIKE %:keyword%")
    List<Product> findByKeyword(@Param("keyword") String keyword);

}
