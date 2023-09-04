package com.playfulCloud.cruddemo.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playfulCloud.cruddemo.basket.entity.Basket;
import com.playfulCloud.cruddemo.customer.authenticate.AuthenticationRequest;
import com.playfulCloud.cruddemo.customer.authenticate.AuthenticationService;
import com.playfulCloud.cruddemo.customer.authenticate.RegisterRequest;
import com.playfulCloud.cruddemo.customer.entity.Customer;
import com.playfulCloud.cruddemo.customer.role.Role;
import com.playfulCloud.cruddemo.customer.security.ApplicationConfig;
import com.playfulCloud.cruddemo.customer.security.JwtService;
import com.playfulCloud.cruddemo.customer.service.CustomerService;
import com.playfulCloud.cruddemo.product.entity.Product;
import com.playfulCloud.cruddemo.product.entity.Review;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(CustomerController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @MockBean
    UserDetailsService service;
    @Autowired
    ObjectMapper mapper;
    Customer customerOne;
    Customer customerTwo;
    Product product;

    RegisterRequest request;
    AuthenticationRequest loginRequest;
    List<Customer> customerList = new ArrayList<>();
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

    @BeforeEach
    void setUp() {
        customerOne = new Customer(
                1,
                "Jakub",
                "Ptaszkowski",
                "test@gmail.com",
                200, "test123",
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
        List<Review> reviews = new ArrayList<>();
        product = new Product(
                1,
                "testTitle",
                "testDescirption",
                15,
                reviews
        );

        request = new RegisterRequest(
                "Jakub",
                "Ptaszkowski",
                "te32131st312312321@gmail.com",
                "test123"
        );

        loginRequest = new AuthenticationRequest(
                "test2@gmail.com",
                "test123"
                );


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
                        .content(mapper.writeValueAsString(customerList)));
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUser() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.ofNullable(customerOne));

        ResultActions response = this.mockMvc
                .perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerOne)));
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(customerOne.getEmail())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void register() throws Exception {
        when(customerService.findByEmail(customerOne.getEmail())).thenReturn(Optional.ofNullable(customerOne));

        ResultActions response = this.mockMvc
                .perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)));
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void log() throws Exception {
        customerService.save(customerTwo);
        ResultActions response = this.mockMvc
                .perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)));
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
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
    void deleteUser() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.ofNullable(customerOne));
        Optional<Customer> customerOptional = customerService.findById(1);

        assertTrue(customerOptional.isPresent());
        Customer customerToDelete = customerOptional.get();

        ResultActions response = this.mockMvc
                .perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerToDelete)));
        response
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
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
                        .content(mapper.writeValueAsString(150.0)));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(customerToUpdate.getBalance())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addToBasket() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.ofNullable(customerOne));
        Optional<Customer> customerOptional = customerService.findById(1);

        assertTrue(customerOptional.isPresent());
        Customer customerWithBasket = customerOptional.get();

        when(productService.findById(1)).thenReturn(Optional.ofNullable(product));
        Optional<Product> productOptional = productService.findById(1);
        assertTrue(productOptional.isPresent());

        List<String> elementsInBasket = new ArrayList<>(List.of(customerWithBasket.getBasket().getContent().split(",")));
        assertFalse(elementsInBasket.contains(String.valueOf(1)));

        when(customerService.save(customerWithBasket)).thenReturn(customerWithBasket);

        ResultActions response = this.mockMvc
                .perform(put("/api/users/1/basket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(1)));
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.basket.content", CoreMatchers.is(customerWithBasket.getBasket().getContent())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void removeFromBasket() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.ofNullable(customerOne));
        Optional<Customer> customerOptional = customerService.findById(1);

        assertTrue(customerOptional.isPresent());
        Customer customerWithBasket = customerOptional.get();

        when(productService.findById(1)).thenReturn(Optional.ofNullable(product));
        Optional<Product> productOptional = productService.findById(1);

        assertTrue(productOptional.isPresent());

        customerWithBasket.getBasket().setContent(customerWithBasket.getBasket().getContent() + 1 + ",");
        customerWithBasket.getBasket().setCharge(customerWithBasket.getBasket().getCharge() + product.getPrice());

        List<String> elementsInBasket = new ArrayList<>(List.of(customerWithBasket.getBasket().getContent().split(",")));
        assertTrue(elementsInBasket.contains(String.valueOf(1)));

        when(customerService.save(customerWithBasket)).thenReturn(customerWithBasket);

        ResultActions response = this.mockMvc
                .perform(put("/api/users/1/basket-remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(1)));
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.basket.content", CoreMatchers.is(customerWithBasket.getBasket().getContent())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void payForBasketItems() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.ofNullable(customerOne));
        Optional<Customer> customerOptional = customerService.findById(1);

        assertTrue(customerOptional.isPresent());
        Customer customerWithBasket = customerOptional.get();

        customerWithBasket.getBasket().setContent(customerWithBasket.getBasket().getContent() + 1 + ",");
        customerWithBasket.getBasket().setCharge(customerWithBasket.getBasket().getCharge() + product.getPrice());

        ResultActions response = this.mockMvc
                .perform(delete("/api/users/payment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(1)));
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.basket.content", CoreMatchers.is(customerWithBasket.getBasket().getContent())))
                .andDo(MockMvcResultHandlers.print());
    }
}