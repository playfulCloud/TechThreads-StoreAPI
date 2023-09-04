package com.playfulCloud.cruddemo.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    List<Review> reviews;

    public Product(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public void addReview(Review review) {
        if (reviews == null) reviews = new ArrayList<>();

        reviews.add(review);
    }

    @Override
    public String toString(){
        return  "name: " + this.getTitle() + "price: " + this.getPrice();
    }
}
