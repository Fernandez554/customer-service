package com.nttbank.microservices.customerservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttbank.microservices.customerservice.model.Customer;
import com.nttbank.microservices.customerservice.repo.ICustomerRepo;
import com.nttbank.microservices.customerservice.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

  @Mock
  private ICustomerRepo repo;

  @InjectMocks
  private CustomerServiceImpl customerService;

  @Mock
  private Customer customer;

  @BeforeEach
  void setUp() {
    customer = new Customer();
    customer.setId("1");
    customer.setName("John Doe");
    customer.setEmail("johndoe@example.com");
  }

  @Test
  void testSaveCustomer() {

    when(repo.save(customer)).thenReturn(Mono.just(customer));

    Mono<Customer> savedCustomerMono = customerService.save(customer);

    savedCustomerMono.subscribe(savedCustomer -> {
      assertEquals(customer.getId(), savedCustomer.getId());
      assertEquals(customer.getName(), savedCustomer.getName());
      assertEquals(customer.getEmail(), savedCustomer.getEmail());
    });

    verify(repo, times(1)).save(customer);
  }

  @Test
  void testFindCustomerById() {

    when(repo.findById("1")).thenReturn(Mono.just(customer));

    Mono<Customer> foundCustomerMono = customerService.findById("1");

    foundCustomerMono.subscribe(foundCustomer -> {
      assertEquals(customer.getId(), foundCustomer.getId());
      assertEquals(customer.getName(), foundCustomer.getName());
    });

    verify(repo, times(1)).findById("1");
  }

  @Test
  void testUpdateCustomer() {

    when(repo.save(customer)).thenReturn(Mono.just(customer));

    Mono<Customer> updatedCustomerMono = customerService.update(customer);

    updatedCustomerMono.subscribe(updatedCustomer -> {
      assertEquals(customer.getId(), updatedCustomer.getId());
      assertEquals(customer.getName(), updatedCustomer.getName());
    });

    verify(repo, times(1)).save(customer);
  }

  @Test
  void testDeleteCustomer() {

    when(repo.deleteById("1")).thenReturn(Mono.empty());

    Mono<Void> deleteResultMono = customerService.delete("1");

    deleteResultMono.subscribe(Assertions::assertNull);

    verify(repo, times(1)).deleteById("1");
  }

  @Test
  void testFindAllCustomers() {

    when(repo.findAll()).thenReturn(Flux.just(customer));

    Flux<Customer> customersFlux = customerService.findAll();

    StepVerifier.create(customersFlux)
        .expectNextMatches(customer -> customer.getId().equals(this.customer.getId()))
        .verifyComplete();

    verify(repo, times(1)).findAll();
  }
}
