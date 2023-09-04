package com.playfulCloud.cruddemo.order.entity;

import com.playfulCloud.cruddemo.order.status.Status;
import com.playfulCloud.cruddemo.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@Entity
@Table(name = "order_to_do")

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "payment")
    private double toPay;

    @Column(name = "date_of_order")
    private Date dateOfOrderPlaced;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "email")
    private String email;

    @Column(name = "content")
    private String content;
}
