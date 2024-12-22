package com.nttbank.microservices.customerservice.controller;

import com.nttbank.microservices.customerservice.model.Customer;
import com.nttbank.microservices.customerservice.service.ICustomerService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final ICustomerService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Customer>>> findAll() {
        Flux<Customer> customerList = service.findAll(); //Flux<Client>

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(customerList))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/{customer_id}")
    public Mono<ResponseEntity<Customer>> findById(@Valid @PathVariable("customer_id") String id) {
        return service.findById(id)
                .map(c -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Customer>> save(@Valid @RequestBody Customer customer, final ServerHttpRequest req) {
        return service.save(customer)
                .map(c -> ResponseEntity
                        .created(URI.create(req.getURI().toString().concat("/").concat(c.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{customer_id}")
    public Mono<ResponseEntity<Customer>> update(@Valid @PathVariable("customer_id") String id, @Valid @RequestBody Customer customer) {
        customer.setId(id);

        Mono<Customer> monoBody = Mono.just(customer);
        Mono<Customer> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, c) -> {
                    db.setId(id);
                    db.setType(c.getType());
                    db.setPhone(c.getPhone());
                    db.setEmail(c.getEmail());
                    db.setAddress(c.getAddress());
                    db.setDateOfBirth(c.getDateOfBirth());
                    return db;
                })
                .flatMap(service::update)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{customer_id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("customer_id") String id) {
        return service.findById(id)
                .flatMap(c -> service.delete(c.getId())
                        .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
