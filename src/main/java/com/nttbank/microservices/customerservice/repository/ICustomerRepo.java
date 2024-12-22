package com.nttbank.microservices.customerservice.repository;

import com.nttbank.microservices.customerservice.model.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepo extends ReactiveMongoRepository<Customer, String> {
}
