package com.nttbank.microservices.customerservice.service;

import com.nttbank.microservices.customerservice.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {

  Mono<Customer> save(Customer t);

  Mono<Customer> update(Customer t);

  Flux<Customer> findAll();

  Mono<Customer> findById(String customerId);

  Mono<Void> delete(String customerId);
}
