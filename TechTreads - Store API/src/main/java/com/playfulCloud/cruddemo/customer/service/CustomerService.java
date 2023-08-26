package com.playfulCloud.cruddemo.customer.service;

import com.playfulCloud.cruddemo.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> findAll();
    Optional<Customer> findById(int id);
    Customer save(Customer customer);
    void deleteById(int id);
    Optional<Customer> findByEmail(String email);

}
