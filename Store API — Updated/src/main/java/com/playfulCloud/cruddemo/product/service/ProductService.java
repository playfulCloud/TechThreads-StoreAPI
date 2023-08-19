package com.playfulCloud.cruddemo.product.service;

import com.playfulCloud.cruddemo.product.entity.Product;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(int id);

    Product save(Product product);

    void deleteById(int id);

    List<Product> findByKeyword(@Param("keyword") String keyword);


}
