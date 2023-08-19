package com.playfulCloud.cruddemo.customer.authenticate;


import com.playfulCloud.cruddemo.basket.entity.Basket;
import com.playfulCloud.cruddemo.customer.entity.Customer;
import com.playfulCloud.cruddemo.customer.exception.UserNotFoundException;
import com.playfulCloud.cruddemo.customer.repository.CustomerRepository;
import com.playfulCloud.cruddemo.customer.role.Role;
import com.playfulCloud.cruddemo.customer.security.JwtService;
import com.playfulCloud.cruddemo.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomerService customerService;
    private final PasswordEncoder password;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var customer = Customer.builder()
                .id(0)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .basket(new Basket())
                .password(password.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        customerService.save(customer);

        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var customer = customerRepository.findByEmail(request.getEmail()).orElseThrow( () -> new UserNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
