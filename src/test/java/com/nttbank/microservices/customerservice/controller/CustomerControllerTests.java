package com.nttbank.microservices.customerservice.controller;

import com.nttbank.microservices.customerservice.dto.CustomerDTO;
import com.nttbank.microservices.customerservice.mapper.CustomerMapper;
import com.nttbank.microservices.customerservice.model.Customer;
import com.nttbank.microservices.customerservice.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTests {


  @Mock
  private CustomerService customerService;

  @Mock
  private CustomerMapper customerMapper;

  WebTestClient client;

  private static final String BASE_URL = "/customers";
  private static final String CUSTOMER_ID_PATH = "/{customer_id}";

  private Customer customer;
  private CustomerDTO customerDTO;

  @BeforeEach
  void setUp() {
    client = WebTestClient.bindToController(new CustomerController(customerService, customerMapper))
        .build();
    customer = new Customer("1234", "personal", "vip", "John Doe",
        "123-456-7890", "john.doe@example.com",
        "123 Street", "1990-01-01");
    customerDTO = new CustomerDTO("1234", "business", "pyme", "John Doe",
        "123-456-7890", "john.doe@example.com",
        "123 Street", "1990-01-01");
  }

//  @Test
//  void findAll_ShouldReturnListOfCustomers() {
//
//    Mockito.when(customerService.findAll()).thenReturn(Flux.just(customer));
//
//    client.get().uri(BASE_URL)
//        .accept(MediaType.APPLICATION_JSON)
//        .exchange()
//        .expectStatus()
//        .isOk()
//        .expectHeader().contentType(MediaType.APPLICATION_JSON)
//        .expectBodyList(Customer.class)
//        .hasSize(1).contains(customer);
//
//    Mockito.verify(customerService, Mockito.times(1)).findAll();
//  }

  @Test
  void findById_ShouldReturnCustomer() {

    Mockito.when(customerService.findById(customer.getId())).thenReturn(Mono.just(customer));

    client.get().uri(BASE_URL + CUSTOMER_ID_PATH, customer.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo(customer.getId());
  }

  @Test
  void save_ShouldCreateCustomer() {
    Customer customer = new Customer("1234", "personal", "vip", "John Doe",
        "123-456-7890", "john.doe@example.com",
        "123 Street", "1990-01-01");

    Mockito.when(customerService.save(Mockito.any())).thenReturn(Mono.just(customer));
    Mockito.when(customerMapper.customerDtoToCustomer(Mockito.any())).thenReturn(customer);

    client.post().uri(BASE_URL)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(customerDTO)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().valueEquals("Location", "/customers/1234")
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo("1234")
        .jsonPath("$.name").isEqualTo("John Doe");

    Mockito.verify(customerService, Mockito.times(1)).save(Mockito.any());
  }

  @Test
  void delete_ShouldDeleteCustomer() {

    Mockito.when(customerService.findById(customer.getId())).thenReturn(Mono.just(customer));
    Mockito.when(customerService.delete(customer.getId())).thenReturn(Mono.empty());

    client.delete().uri(BASE_URL + CUSTOMER_ID_PATH, customer.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNoContent();

    Mockito.verify(customerService, Mockito.times(1))
        .delete(customer.getId());
  }

  @Test
  void update_ShouldUpdateCustomer() {

    Customer updatedCustomer = new Customer("1234", "business", "pyme",
        "John Doe Updated","987-654-3210", "john.doe.updated@example.com",
        "456 Avenue", "1985-05-05");

    Mockito.when(customerService.findById(customer.getId())).thenReturn(Mono.just(customer));
    Mockito.when(customerMapper.customerDtoToCustomer(Mockito.any(CustomerDTO.class)))
        .thenReturn(updatedCustomer);
    Mockito.when(customerService.update(Mockito.any(Customer.class)))
        .thenReturn(Mono.just(updatedCustomer));

    client.put().uri(BASE_URL + CUSTOMER_ID_PATH, customer.getId())
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(customerDTO)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo("1234")
        .jsonPath("$.name").isEqualTo("John Doe Updated")
        .jsonPath("$.type").isEqualTo("business")
        .jsonPath("$.phone").isEqualTo("987-654-3210")
        .jsonPath("$.email").isEqualTo("john.doe.updated@example.com");

    Mockito.verify(customerService, Mockito.times(1))
        .findById(customer.getId());
    Mockito.verify(customerService, Mockito.times(1))
        .update(Mockito.any(Customer.class));
  }

}
