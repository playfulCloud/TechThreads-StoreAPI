package com.playfulCloud.cruddemo.basket.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="basket")
@Data
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="charge")
    private double charge;

    @Column(name = "content")
    private String content;

    public Basket() {
        this.content = "";
        this.charge = 0.0;
    }


}
