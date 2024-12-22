package com.nttbank.microservices.customerservice.business;

import com.nttbank.microservices.customerservice.model.CreateCustomerRequest;
import com.nttbank.microservices.customerservice.model.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
//    Mono<Customer> save(Customer t);
//    Mono<Customer> update(Customer t);
    Flux<Customer> findAll();
//    Mono<Customer> findById(String id);
//    Mono<Void> delete(String id);
}
