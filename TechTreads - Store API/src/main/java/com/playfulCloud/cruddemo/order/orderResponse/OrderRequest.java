package com.playfulCloud.cruddemo.order.orderResponse;

import lombok.Data;

import java.util.Date;

@Data
public class OrderRequest {
    private double toPay;
    private Date dateOfOrderPlaced;
    private String street;
    private String city;
    private String postalCode;
    private String email;
    private String content;

    public OrderRequest(String street, String city, String postalCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.dateOfOrderPlaced = new Date();
    }
}
