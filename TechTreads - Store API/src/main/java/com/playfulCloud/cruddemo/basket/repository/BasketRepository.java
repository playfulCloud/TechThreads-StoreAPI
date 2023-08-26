package com.playfulCloud.cruddemo.basket.repository;

import com.playfulCloud.cruddemo.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BasketRepository extends JpaRepository<Basket,Integer> {
}
