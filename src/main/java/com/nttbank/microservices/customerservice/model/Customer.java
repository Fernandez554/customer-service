package com.nttbank.microservices.customerservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
public class Customer {

  @EqualsAndHashCode.Include
  @Id
  private String id;

  @Pattern(regexp = "^(personal|business)$",
      message = "Customer type must be 'personal' or 'business'")
  @NotNull(message = "Customer type cannot be null")
  private String type;

  @Pattern(regexp = "^(vip|pyme|)$", message = "Customer profile must be 'vip' or 'pyme'")
  @Builder.Default
  private String profile = "";

  @Size(min = 3, max = 90, message = "Customer type must be between 3 and 90 characters")
  @NotNull(message = "Customer name cannot be null")
  private String name;

  private String phone;

  private String email;

  private String address;

  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",
      message = "Date of birth must be in the format YYYY-MM-DD.")
  private String dateOfBirth;
}
