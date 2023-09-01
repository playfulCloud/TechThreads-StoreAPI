package com.playfulCloud.cruddemo.customer.service;

import com.playfulCloud.cruddemo.customer.entity.Customer;
import com.playfulCloud.cruddemo.customer.exception.UserNotFoundException;
import com.playfulCloud.cruddemo.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }


    @Override
    public Optional<Customer> findById(int id) {
        Optional<Customer> user = customerRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new UserNotFoundException("User with id: " + id + " doesnt exists!");
        }
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public String deleteById(int id) {
        customerRepository.deleteById(id);
        return "Success";
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

}
