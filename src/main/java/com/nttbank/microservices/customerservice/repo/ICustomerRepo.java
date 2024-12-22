package com.nttbank.microservices.customerservice.repo;

import com.nttbank.microservices.customerservice.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface ICustomerRepo extends ReactiveMongoRepository<Customer,String> {
}
