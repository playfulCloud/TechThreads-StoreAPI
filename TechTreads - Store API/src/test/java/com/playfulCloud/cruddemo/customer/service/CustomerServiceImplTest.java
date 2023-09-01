package com.playfulCloud.cruddemo.customer.service;

import com.playfulCloud.cruddemo.basket.entity.Basket;
import com.playfulCloud.cruddemo.customer.entity.Customer;
import com.playfulCloud.cruddemo.customer.repository.CustomerRepository;
import com.playfulCloud.cruddemo.customer.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;// = mock(CustomerRepository.class);
    private CustomerService customerService;
    Customer customer;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository);
        customer = new Customer(1, "Jakub", "Ptaszkowski", "test@gmail.com", 10, "test123", new Basket(), Role.USER);
    }

    @Test
    void testFindAll() {
        // given
        when(customerRepository.findAll()).thenReturn(new ArrayList<Customer>(Collections.singleton(customer)));
        // when
        List<Customer> result = customerService.findAll();
        // then
        assertThat(result)
                .isNotNull()
                .asList()
                .isNotEmpty()
                .allMatch(e -> customer.equals(e));
    }

    @Test
    void testFindById() {
        when(customerRepository.findById(1)).thenReturn(Optional.ofNullable(customer));

        Optional<Customer> result = customerRepository.findById(1);

        assertThat(result.isPresent());
        assertThat(result.get()).isNotNull().isEqualTo(customer);
    }

    @Test
    void testSave() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer customer1 = customerRepository.save(customer);

        assertThat(customer1)
                .isNotNull()
                .isEqualTo(customer);
    }

    @Test
    void testDeleteById() {
        doAnswer(Answers.CALLS_REAL_METHODS).when(customerRepository).deleteById(any());


        assertThat(customerService.deleteById(1)).isEqualTo("Success");
    }

    @Test
    void testFindByEmail() {
        when(customerRepository.findByEmail("test@gmail.com")).thenReturn(Optional.ofNullable(customer));

        Optional<Customer> optionalCustomer = customerRepository.findByEmail("test@gmail.com");

        assertThat(optionalCustomer.isPresent());
        assertThat(optionalCustomer.get()).isNotNull().isEqualTo(customer);
    }
}