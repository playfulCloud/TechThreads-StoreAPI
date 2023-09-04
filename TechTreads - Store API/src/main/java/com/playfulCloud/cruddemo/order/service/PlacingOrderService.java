package com.playfulCloud.cruddemo.order.service;

import com.playfulCloud.cruddemo.order.orderResponse.OrderRequest;
import com.playfulCloud.cruddemo.order.orderResponse.OrderResponse;

public interface PlacingOrderService {
    public OrderResponse placeOrder(OrderRequest orderRequest);
}
