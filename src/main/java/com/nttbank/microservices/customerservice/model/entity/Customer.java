package com.nttbank.microservices.customerservice.model.entity;

import com.nttbank.microservices.customerservice.model.CustomerType;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customer")
public class Customer {
    @Id
    private String id;
    private CustomerType type;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String dateOfBirth;
}
