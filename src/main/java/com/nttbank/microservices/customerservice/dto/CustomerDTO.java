package com.nttbank.microservices.customerservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
  private String id;

  @Pattern(regexp = "^(personal|business)$", message = "Customer type must be 'personal' or 'business'")
  @NotNull(message = "Customer type cannot be null")
  private String type;

  @Pattern(regexp = "^(vip|pyme|)$", message = "Customer profile must be 'vip' or 'pyme'")
  private String profile;

  @Size(min = 3, max = 90, message = "Customer name must be between 3 and 90 characters")
  @NotNull(message = "Customer name cannot be null")
  private String name;

  private String phone;
  private String email;
  private String address;

  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of birth must be in the format YYYY-MM-DD.")
  private String dateOfBirth;
}
