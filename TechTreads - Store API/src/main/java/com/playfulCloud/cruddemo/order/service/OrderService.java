package com.playfulCloud.cruddemo.order.service;

import com.playfulCloud.cruddemo.order.entity.Order;

import java.util.List;

public interface OrderService {
    public Order save(Order order);
    public List<Order> findByEmail(String email);
}
