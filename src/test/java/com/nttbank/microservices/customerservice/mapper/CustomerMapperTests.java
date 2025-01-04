package com.nttbank.microservices.customerservice.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.nttbank.microservices.customerservice.dto.CustomerDTO;
import com.nttbank.microservices.customerservice.model.Customer;
import org.junit.jupiter.api.Test;

public class CustomerMapperTests {

  @Test
  void customerDtoToCustomer_GivenNullShouldReturnNull() {

    CustomerDTO customerDTO = null;

    Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(customerDTO);

    assertNull(customer);
  }

  @Test
  void customerDtoToCustomer_ShouldMapCustomerDTOToCustomer() {

    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setType("business");
    customerDTO.setProfile("vip");
    customerDTO.setName("John Doe");
    customerDTO.setPhone("123-456-7890");
    customerDTO.setEmail("john.doe@example.com");
    customerDTO.setAddress("123 Street");
    customerDTO.setDateOfBirth("1990-01-01");

    Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(customerDTO);

    assertEquals(customerDTO.getType(), customer.getType());
    assertEquals(customerDTO.getProfile(), customer.getProfile());
    assertEquals(customerDTO.getName(), customer.getName());
    assertEquals(customerDTO.getPhone(), customer.getPhone());
    assertEquals(customerDTO.getEmail(), customer.getEmail());
    assertEquals(customerDTO.getAddress(), customer.getAddress());
    assertEquals(customerDTO.getDateOfBirth(), customer.getDateOfBirth());
  }
}
