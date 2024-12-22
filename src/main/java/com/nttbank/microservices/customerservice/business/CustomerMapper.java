package com.nttbank.microservices.customerservice.business;

import com.nttbank.microservices.customerservice.model.CreateCustomerRequest;
import com.nttbank.microservices.customerservice.model.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer getCustomerOfCustomerRequest(CreateCustomerRequest request){
        Customer customer= new Customer();
        customer.setType(request.getType());
        customer.setName(request.getName());
       return customer;
    }

}
