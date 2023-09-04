package com.playfulCloud.cruddemo.customer.mail;

import com.playfulCloud.cruddemo.customer.authenticate.RegisterRequest;
import com.playfulCloud.cruddemo.order.orderResponse.OrderRequest;

public interface JavaMailService {
    void registerMail(final RegisterRequest request);

    void purchaseMail(final OrderRequest request);
}

