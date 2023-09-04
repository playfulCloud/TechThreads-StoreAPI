package com.playfulCloud.cruddemo.order.service;

import com.playfulCloud.cruddemo.order.entity.Order;
import com.playfulCloud.cruddemo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findByEmail(String email) {
        return orderRepository.findByEmail(email);
    }
}
