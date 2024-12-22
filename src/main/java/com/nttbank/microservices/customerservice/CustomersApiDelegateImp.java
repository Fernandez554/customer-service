package com.nttbank.microservices.customerservice;

import com.nttbank.microservices.customerservice.api.CustomersApi;
import com.nttbank.microservices.customerservice.business.CustomerService;
import com.nttbank.microservices.customerservice.model.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomersApiDelegateImp implements CustomersApi {

    @Autowired
    CustomerService customerService;

    public Mono<ResponseEntity<Flux<Customer>>> findAll() {
        Flux<Customer> fx = customerService.findAll(); //Flux<Client>

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

}
