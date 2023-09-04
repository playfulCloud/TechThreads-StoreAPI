package com.playfulCloud.cruddemo.product.external;

import com.playfulCloud.cruddemo.product.entity.Product;
import com.playfulCloud.cruddemo.product.service.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;

@Component
@Data
public class ExternalApiDataFetcher {

    private final ProductService service;
    private final WebClient.Builder webClient;

    @Autowired
    public ExternalApiDataFetcher(ProductService service, WebClient.Builder webClient){
        this.service = service;
        this.webClient = webClient;
    }

    public void fetchData() {
        Product[] products = webClient
                .build()
                .get()
                .uri("https://fakestoreapi.com/products")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Product[].class)
                .block();

        saveFetchedProductsToDataBase(products);
    }

    public void saveFetchedProductsToDataBase(Product[] products) {
        for (Product product : products) {
            if(product.getDescription().length() >= 255){
                String[] sentance = product.getDescription().split("\\.");
                product.setDescription(sentance[0]);
            }
            service.save(product);
        }

    }

}
