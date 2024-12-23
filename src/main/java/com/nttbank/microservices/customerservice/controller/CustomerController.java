package com.nttbank.microservices.customerservice.controller;

import com.nttbank.microservices.customerservice.model.Customer;
import com.nttbank.microservices.customerservice.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Controller", description = "Manage customers")
public class CustomerController {

  private final ICustomerService service;

  /**
   * Fetches a list of all customers.
   *
   * @return a {@link Mono} containing a {@link ResponseEntity} with the list of customers or a
   *     {@link ResponseEntity} with no content if no customers are found
   */
  @Operation(
      summary = "Fetch all customers",
      description = "Retrieves all customers from the system. "
          + "Returns an empty list if no customers are found."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Successfully retrieved the list of customers"),
      @ApiResponse(responseCode = "204", description = "No content available")
  })
  @GetMapping
  public Mono<ResponseEntity<Flux<Customer>>> findAll() {
    Flux<Customer> customerList = service.findAll();

    return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(customerList))
        .defaultIfEmpty(ResponseEntity.noContent().build());
  }

  /**
   * Fetches a single customer by ID.
   *
   * @param id the ID of the customer to retrieve
   * @return a {@link Mono} containing a {@link ResponseEntity} with the customer details or a
   *     {@link ResponseEntity} with status 404 if the customer is not found.
   */
  @Operation(
      summary = "Fetch customer by ID",
      description = "Fetches a single customer by ID. Returns 404 if the customer is not found."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the customer"),
      @ApiResponse(responseCode = "404", description = "Customer not found")
  })
  @GetMapping("/{customer_id}")
  public Mono<ResponseEntity<Customer>> findById(@Valid @PathVariable("customer_id") String id) {
    return service.findById(id)
        .map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Creates a new customer.
   *
   * @param customer the customer to create
   * @param req      the {@link ServerHttpRequest} to get the URI for the created resource
   * @return a {@link Mono} containing a {@link ResponseEntity} with the created customer and the
   *     URI of the newly created resource, or 404 if creation fails.
   */
  @Operation(
      summary = "Create a new customer",
      description = "Creates a new customer and returns the created customer "
          + "with a location header pointing to the resource."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created the customer"),
      @ApiResponse(responseCode = "404", description = "Creation failed")
  })
  @PostMapping
  public Mono<ResponseEntity<Customer>> save(@Valid @RequestBody Customer customer,
      final ServerHttpRequest req) {
    return service.save(customer).map(c -> ResponseEntity.created(
                URI.create(req.getURI().toString().concat("/").concat(c.getId())))
            .contentType(MediaType.APPLICATION_JSON).body(c))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Updates an existing customer by ID.
   *
   * @param id       the ID of the customer to update
   * @param customer the updated customer information
   * @return a {@link Mono} containing a {@link ResponseEntity} with the updated customer or a
   *     {@link ResponseEntity} with status 404 if the customer is not found.
   */
  @Operation(
      summary = "Update an existing customer",
      description = "Updates an existing customer by ID. Returns the updated customer or 404 "
          + "if the customer is not found."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated the customer"),
      @ApiResponse(responseCode = "404", description = "Customer not found")
  })
  @PutMapping("/{customer_id}")
  public Mono<ResponseEntity<Customer>> update(@Valid @PathVariable("customer_id") String id,
      @Valid @RequestBody Customer customer) {
    customer.setId(id);

    Mono<Customer> monoBody = Mono.just(customer);
    Mono<Customer> monoDb = service.findById(id);

    return monoDb.zipWith(monoBody, (db, c) -> {
      db.setId(id);
      db.setName(c.getName());
      db.setType(c.getType());
      db.setPhone(c.getPhone());
      db.setEmail(c.getEmail());
      db.setAddress(c.getAddress());
      db.setDateOfBirth(c.getDateOfBirth());
      return db;
    }).flatMap(service::update)
      .map(e -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Deletes a customer by ID.
   *
   * @param id the ID of the customer to delete
   * @return a {@link Mono} containing a {@link ResponseEntity} with status 204 if deleted, or a
   *     {@link ResponseEntity} with status 404 if the customer is not found.
   */
  @Operation(
      summary = "Delete a customer by ID",
      description = "Deletes a customer by ID. Returns status 204 if successfully deleted or "
          + "404 if the customer is not found."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully deleted the customer"),
      @ApiResponse(responseCode = "404", description = "Customer not found")
  })
  @DeleteMapping("/{customer_id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable("customer_id") String id) {
    return service.findById(id).flatMap(
            c -> service.delete(c.getId()).thenReturn(
                new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
