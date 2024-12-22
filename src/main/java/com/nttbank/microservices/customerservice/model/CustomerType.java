package com.nttbank.microservices.customerservice.model;


public enum CustomerType {
    PERSONAL("personal"),
    BUSINESS("business");

    private final String value;

    CustomerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
