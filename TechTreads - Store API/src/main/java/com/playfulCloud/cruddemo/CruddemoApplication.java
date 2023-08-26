package com.playfulCloud.cruddemo;

import com.playfulCloud.cruddemo.product.external.ExternalApiDataFetcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CruddemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CruddemoApplication.class, args);
        ExternalApiDataFetcher dataFetcher = context.getBean(ExternalApiDataFetcher.class);
        dataFetcher.fetchData();
    }

}
