package com.playfulCloud.cruddemo.product.controller;

import com.playfulCloud.cruddemo.product.entity.Product;
import com.playfulCloud.cruddemo.product.entity.Review;
import com.playfulCloud.cruddemo.product.service.ProductService;
import com.playfulCloud.cruddemo.customer.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product product(@PathVariable int id) {

        Optional<Product> product = productService.findById(id);

        return product.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));
    }

    @GetMapping("/products/certain/{keyWord}")
    public List<Product> listOfProducts(@PathVariable String keyWord) {

        return productService.findByKeyword(keyWord);
    }


    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {

        productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable int id) {
        Optional<Product> foundProduct = productService.findById(id);

        Product found = foundProduct.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));

        if (product.getTitle() != null) found.setTitle(product.getTitle());
        else found.setTitle(found.getTitle());

        if (product.getDescription() != null) found.setDescription(product.getDescription());
        else found.setDescription(found.getDescription());

        productService.save(found);

        return ResponseEntity.ok(found);
    }


    @PutMapping("/products/price/{id}")
    public ResponseEntity<Product> updatePrice(@RequestBody double price, @PathVariable int id) {
        Optional<Product> foundProduct = productService.findById(id);

        Product product = foundProduct.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));

        product.setPrice(price);

        productService.save(product);

        return ResponseEntity.ok(product);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable int id) {

        Optional<Product> product = productService.findById(id);

        product.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));

        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{id}/reviews")
    public ResponseEntity<Product> addReviewToProduct(@PathVariable int id, @RequestBody String review) {

        Optional<Product> product = productService.findById(id);

        Product reviewToProduct = product.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        reviewToProduct.getReviews().add(new Review(review, authentication.getName()));
        productService.save(reviewToProduct);
        return ResponseEntity.ok(reviewToProduct);
    }

    @GetMapping("/products/{id}/reviews")
    public List<Review> getReviewsByProduct(@PathVariable int id) {
        Optional<Product> product = productService.findById(id);

        Product productReviews = product.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));

        return productReviews.getReviews();
    }


}
