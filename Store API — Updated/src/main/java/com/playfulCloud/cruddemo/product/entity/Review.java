package com.playfulCloud.cruddemo.product.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "author")
    private String author;

    public Review(String comment, String author){
        this.comment = comment;
        this.author = author;
    }
}
