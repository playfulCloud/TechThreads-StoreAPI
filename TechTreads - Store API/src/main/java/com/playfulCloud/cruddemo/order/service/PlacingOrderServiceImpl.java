package com.playfulCloud.cruddemo.order.service;

import com.playfulCloud.cruddemo.customer.mail.JavaMailServiceImpl;
import com.playfulCloud.cruddemo.order.entity.Order;
import com.playfulCloud.cruddemo.order.orderResponse.OrderRequest;
import com.playfulCloud.cruddemo.order.orderResponse.OrderResponse;
import com.playfulCloud.cruddemo.order.status.Status;
import com.playfulCloud.cruddemo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PlacingOrderServiceImpl implements PlacingOrderService {

    private final OrderService orderService;
    private final JavaMailServiceImpl mailService;
    private final ProductService productService;

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        var order = Order.builder()
                .id(0)
                .status(Status.UNPAID)
                .toPay(orderRequest.getToPay())
                .dateOfOrderPlaced(new Date())
                .street(orderRequest.getStreet())
                .city(orderRequest.getCity())
                .postalCode(orderRequest.getPostalCode())
                .email(orderRequest.getEmail())
                .build();

        orderService.save(order);
        mailService.purchaseMail(orderRequest);

        return (new OrderResponse("Your order has been accepted!"));
    }


}
