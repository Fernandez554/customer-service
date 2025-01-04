package com.nttbank.microservices.customerservice.mapper;

import com.nttbank.microservices.customerservice.dto.CustomerDTO;
import com.nttbank.microservices.customerservice.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CustomerMapper {

  CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

  Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
