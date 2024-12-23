package com.nttbank.microservices.customerservice.repo;

import com.nttbank.microservices.customerservice.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICustomerRepo extends ReactiveMongoRepository<Customer, String> {

}
