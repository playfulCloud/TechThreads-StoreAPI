package com.playfulCloud.cruddemo.order.repository;

import com.playfulCloud.cruddemo.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findByEmail(String email);
}
