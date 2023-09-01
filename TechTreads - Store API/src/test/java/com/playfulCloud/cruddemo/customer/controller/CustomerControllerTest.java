package com.playfulCloud.cruddemo.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playfulCloud.cruddemo.basket.entity.Basket;
import com.playfulCloud.cruddemo.customer.authenticate.AuthenticationService;
import com.playfulCloud.cruddemo.customer.entity.Customer;
import com.playfulCloud.cruddemo.customer.role.Role;
import com.playfulCloud.cruddemo.customer.security.ApplicationConfig;
import com.playfulCloud.cruddemo.customer.security.JwtService;
import com.playfulCloud.cruddemo.customer.service.CustomerService;
import com.playfulCloud.cruddemo.product.entity.Product;
import com.playfulCloud.cruddemo.product.service.ProductService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(CustomerController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private ProductService productService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private ApplicationConfig config;
    @MockBean
    UserDetailsService service;
    @Autowired
    ObjectMapper mapper;
    Customer customerOne;
    Customer customerTwo;
    List<Customer> customerList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        customerOne = new Customer(
                1,
                "Jakub",
                "Ptaszkowski",
                "test@gmail.com",
                10, "test123",
                new Basket(),
                Role.USER);

        customerTwo = new Customer(
                2,
                "Daniel",
                "Ryszkowski",
                "test2@gmail.com",
                10, "test123",
                new Basket(),
                Role.USER);

        customerList.add(customerOne);
        customerList.add(customerTwo);


        Product product = new Product()
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindAll() throws Exception {
        when(customerService.findAll()).thenReturn(new ArrayList<Customer>(Arrays.asList(customerOne, customerTwo)));

        ResultActions response = this.mockMvc
                .perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerList)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUser() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.ofNullable(customerOne));

        this.mockMvc
                .perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerOne)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(customerOne.getEmail())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void register() throws Exception {

    }

    @Test
    void log() {
    }

    //TODO ZAPYTAĆ O IGNOROWANIE AUTHERITIES
    @Test
    void updateUser() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.ofNullable(customerOne));
        Optional<Customer> customerOptional = customerService.findById(1);

        assertTrue(customerOptional.isPresent());
        Customer customerToUpdate = customerOptional.get();

        customerToUpdate.setFirstName("Ignacy");
        customerToUpdate.setLastName("Łukowski");

        when(customerService.save(customerToUpdate)).thenReturn(customerToUpdate);

        ResultActions response = this.mockMvc
                .perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerToUpdate)));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(customerToUpdate.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(customerToUpdate.getLastName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateBalance() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.ofNullable(customerOne));
        Optional<Customer> customerOptional = customerService.findById(1);

        assertTrue(customerOptional.isPresent());
        Customer customerToUpdate = customerOptional.get();

        customerToUpdate.setBalance(150.0);

        when(customerService.save(customerToUpdate)).thenReturn(customerToUpdate);

        ResultActions response = this.mockMvc
                .perform(put("/api/users/balance/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerToUpdate)));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addToBasket() {
    }

    @Test
    void removeFromBasket() {
    }

    @Test
    void payForBasketItems() {

    }
}