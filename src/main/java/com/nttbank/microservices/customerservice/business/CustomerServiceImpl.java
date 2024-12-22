package com.nttbank.microservices.customerservice.business;


import com.nttbank.microservices.customerservice.model.CreateCustomerRequest;
import com.nttbank.microservices.customerservice.model.entity.Customer;
import com.nttbank.microservices.customerservice.repository.ICustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private ICustomerRepo repo;

    private CustomerMapper customerMapper;

//    @Override
//    public Mono<Customer> saveCustomer(CreateCustomerRequest createCustomerRequest) {
//        return repo.save(customerMapper.getCustomerOfCustomerRequest(createCustomerRequest));
//    }

    @Override
    public Flux<Customer> findAll() {
        return null;
    }
}
