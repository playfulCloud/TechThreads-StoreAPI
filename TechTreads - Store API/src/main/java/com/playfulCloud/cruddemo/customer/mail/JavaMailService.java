package com.playfulCloud.cruddemo.customer.mail;

import com.playfulCloud.cruddemo.customer.authenticate.RegisterRequest;

public interface JavaMailService {
    void registerMail(final RegisterRequest request);
}

