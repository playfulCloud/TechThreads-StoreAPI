package com.playfulCloud.cruddemo.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playfulCloud.cruddemo.basket.entity.Basket;
import com.playfulCloud.cruddemo.customer.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    //TODO: Zmienić na big decimal
    @Column(name = "balance")
    private double balance;

    @Column(name = "pw")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="basket_id")
    private Basket basket;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
