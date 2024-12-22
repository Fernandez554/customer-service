package com.nttbank.microservices.customerservice.service.impl;


import com.nttbank.microservices.customerservice.model.Customer;
import com.nttbank.microservices.customerservice.repo.ICustomerRepo;
import com.nttbank.microservices.customerservice.service.ICustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerRepo repo;

    public CustomerServiceImpl(ICustomerRepo repo) {
        this.repo = repo;
    }

    @Override
    public Mono<Customer> save(Customer t) {
       return repo.save(t);
    }

    @Override
    public Mono<Customer> update(Customer t) {
        return repo.save(t);
    }

    @Override
    public Flux<Customer> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<Customer> findById(String customerId) {
        return repo.findById(customerId);
    }

    @Override
    public Mono<Void> delete(String customerId) {
        return repo.deleteById(customerId);
    }
}
