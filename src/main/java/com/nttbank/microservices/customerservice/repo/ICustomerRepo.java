package com.nttbank.microservices.customerservice.repo;

import com.nttbank.microservices.customerservice.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * ICustomerRepo interface for interacting with the MongoDB database.
 */
public interface ICustomerRepo extends ReactiveMongoRepository<Customer, String> {

}
