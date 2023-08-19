package com.playfulCloud.cruddemo.customer.controller;


import com.playfulCloud.cruddemo.product.entity.Product;
import com.playfulCloud.cruddemo.product.service.ProductService;
import com.playfulCloud.cruddemo.customer.authenticate.AuthenticationRequest;
import com.playfulCloud.cruddemo.customer.authenticate.AuthenticationResponse;
import com.playfulCloud.cruddemo.customer.authenticate.AuthenticationService;
import com.playfulCloud.cruddemo.customer.authenticate.RegisterRequest;
import com.playfulCloud.cruddemo.customer.entity.Customer;
import com.playfulCloud.cruddemo.customer.exception.UserNotFoundException;
import com.playfulCloud.cruddemo.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {


    private final CustomerService customerService;
    private final ProductService productService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;



    @GetMapping("/users")
    public List<Customer> findAll() {
        return customerService.findAll();
    }


    @GetMapping("/users/{id}")
    public Customer user(@PathVariable int id) {

        Optional<Customer> user = customerService.findById(id);

        return user.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {

        if(customerService.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User with this email already exists: " + request.getEmail());
        }else{

            return ResponseEntity.ok(authenticationService.register(request));

        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> log(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<Customer> updateUser(@RequestBody Customer customer, @PathVariable int id) {
        Optional<Customer> foundUser = customerService.findById(id);

        Customer found = foundUser.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));

        if (customer.getFirstName() != null) found.setFirstName(customer.getFirstName());
        else found.setFirstName(found.getFirstName());

        if (customer.getLastName() != null) found.setLastName(customer.getLastName());
        else found.setLastName(found.getLastName());

        if (customer.getEmail() != null) found.setEmail(customer.getEmail());
        else found.setEmail(found.getEmail());

        customerService.save(found);

        return ResponseEntity.ok(found);

    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<Customer> deleteUser(@PathVariable int id) {

        Optional<Customer> user = customerService.findById(id);

        user.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));

        customerService.deleteById(id);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/users/balance/{id}")
    public ResponseEntity<Customer> updateBalance(@RequestBody double balance, @PathVariable int id) {


        Optional<Customer> foundUser = customerService.findById(id);

        Customer customer = foundUser.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));


        customer.setBalance(balance);

        customerService.save(customer);

        return ResponseEntity.ok(customer);

    }

    @PutMapping("/users/{userId}/basket")
    public ResponseEntity<Customer> addToBasket(@RequestBody int productId, @PathVariable int userId) {

        Optional<Customer> foundUser = customerService.findById(userId);
        Customer customer = foundUser.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " doesnt exists!"));


        Optional<Product> foundProduct = productService.findById(productId);
        Product product =  foundProduct.orElseThrow(() -> new UserNotFoundException("Product with id: " + productId + " doesnt exists!"));


        List<String> elementsInBasket = new ArrayList<>(List.of(customer.getBasket().getContent().split(",")));

        if(elementsInBasket.contains(String.valueOf(productId))) throw new UserNotFoundException("Product: " + product.getName() + " already exists in you basket");

        customer.getBasket().setContent(customer.getBasket().getContent() + productId + ",");
        customer.getBasket().setCharge(customer.getBasket().getCharge() + product.getPrice());

        customerService.save(customer);

        return ResponseEntity.ok(customer);
    }

    @PutMapping("/users/{userId}/basket-remove")
    public ResponseEntity<Customer> removeFromBasket(@RequestBody int productId, @PathVariable int userId){

        Optional<Customer> foundUser = customerService.findById(userId);
        Customer customer = foundUser.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " doesnt exists!"));

        Optional<Product> foundProduct = productService.findById(productId);
        Product product = foundProduct.orElseThrow(() -> new UserNotFoundException("Product with id: " + productId + " doesnt exists!"));


        List<String> elementsInBasket = new ArrayList<>(List.of(customer.getBasket().getContent().split(",")));

        if(elementsInBasket.contains(String.valueOf(productId))) elementsInBasket.remove(String.valueOf(productId));

        StringBuilder tmp = new StringBuilder();
        for(String element : elementsInBasket) tmp.append(element).append(",");


        customer.getBasket().setContent(tmp.toString());
        customer.getBasket().setCharge(customer.getBasket().getCharge() - product.getPrice());

        customerService.save(customer);

        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("users/payment/{id}")
    public ResponseEntity<Customer>payForBasketItems (@PathVariable int id) {

        Optional<Customer> foundUser = customerService.findById(id);
        Customer customer = foundUser.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " doesnt exists!"));

        if(customer.getBasket().getContent().equals("")) throw new UserNotFoundException("Basket is empty");

        if(customer.getBalance() >= customer.getBasket().getCharge()){

            customer.setBalance(customer.getBalance() - customer.getBasket().getCharge());

            List<String> elementsInBasket = new ArrayList<>(List.of(customer.getBasket().getContent().split(",")));

            for(String item : elementsInBasket){
                productService.deleteById(Integer.parseInt(item));
            }

            customer.getBasket().setContent("");
            customer.getBasket().setCharge(0);

            customerService.save(customer);

            return ResponseEntity.ok(customer);
        }

        throw new UserNotFoundException("Insufficient funds: " + customer.getBalance());

    }




}


