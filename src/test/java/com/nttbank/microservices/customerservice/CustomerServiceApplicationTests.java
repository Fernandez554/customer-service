package com.nttbank.microservices.customerservice;

import com.nttbank.microservices.customerservice.controller.CustomerController;
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

@ExtendWith(MockitoExtension.class)
class CustomerServiceApplicationTests {


	@Mock
	private CustomerService customerService;

	WebTestClient client;

	private static final String BASE_URL = "/customers";

	@BeforeEach
	void setUp() {
		client = WebTestClient.bindToController(new CustomerController(customerService)).build();
	}

	@Test
	void findAll_ShouldReturnListOfBankAccounts() {
		// Arrange: Mock the service layer to return a list of accounts
		Customer customer1 = new Customer();
		customer1.setId("1234");

		Mockito.when(customerService.findAll()).thenReturn(Flux.just(customer1));

		// Act: Call the endpoint
		client.get()
				.uri(BASE_URL)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Customer.class)
				.hasSize(1)
				.contains(customer1);

		// Verify service interaction
		Mockito.verify(customerService, Mockito.times(1)).findAll();
	}

}
